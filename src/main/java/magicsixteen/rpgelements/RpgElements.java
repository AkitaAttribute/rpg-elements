package magicsixteen.rpgelements;

import com.google.gson.Gson;
import magicsixteen.rpgelements.events.item.GlowingItemEntity;
import magicsixteen.rpgelements.util.GlowHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ConcurrentModificationException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static magicsixteen.rpgelements.events.item.GlowingItemEntity.mapIeToGe;
import static magicsixteen.rpgelements.util.MessagingHelper.messageAllPlayers;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("rpg-elements")
public class RpgElements {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    GlowHelper glowHelper = new GlowHelper();
    boolean debug = false;

    public RpgElements() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        //MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, PlayerEvent.ItemPickupEvent);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        String playerName = player.getName().getUnformattedComponentText();
        try {
            Minecraft mc = Minecraft.getInstance();
            mc.player.sendChatMessage("Welcome! [" + playerName + "]");
        } catch (Exception e) {
            LOGGER.info("Hmm, we had an issue:\t" + e);
        }
        LOGGER.info("[" + playerName + "] has logged in game.");
    }

    @SubscribeEvent
    public void onPlayerCraft(PlayerEvent.ItemCraftedEvent event) {
        ItemStack item = event.getCrafting();
        if(item.isDamageable()) {
            //item.;
        }
    }

    /*@SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {

    }*/

    /*@SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getInstance();

        RayTraceResult objectMouseOver = mc.objectMouseOver;
        if(objectMouseOver.hitInfo == RayTraceResult.Type.ENTITY) {

        }
    }*/

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        int glowDuration = 10;
        if(event.getEntity() instanceof PlayerEntity) {
            glowDuration = 600;
        }
        for(ItemEntity item : event.getDrops()) {
            glowHelper.addGlowing(item, glowDuration);
            messageAllPlayers("Attempted to add glowing. [Item][" + item + "][Glowing][" + item.isGlowing() + "]");
        }
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        DamageSource unknownSource, source = new EntityDamageSource("The Planet", event.getEntity());
        if(event.getSource() != null) {
            source = event.getSource();
        }
        LivingEntity receiver = (LivingEntity) event.getEntity();
        float healthAfterDamage = (receiver.getHealth() - event.getAmount());

        if(source.getTrueSource() instanceof PlayerEntity) {
            glowHelper.addGlowing(receiver, 10);
            if(debug) {
                messageAllPlayers("Attempted to add glowing. [Glowing][" + receiver.isGlowing() + "][UUID]["
                        + receiver.getUniqueID() + "]");
            }
            /*Minecraft mc = Minecraft.getInstance();
            if(mc.player != null) {
                mc.player.sendChatMessage("[" + source.getTrueSource().getName().getUnformattedComponentText()
                        + "] damaged [" + receiver.getName().getString() + "][-" + event.getAmount() + "]["
                        + healthAfterDamage + "/" + receiver.getMaxHealth() + "]");
            }*/
        }

        if(receiver instanceof PlayerEntity) {
            /*Minecraft mc = Minecraft.getInstance();
            if(mc.player != null) {
                if(source.getTrueSource() == null) {
                    mc.player.sendChatMessage("[" + unknownSource.getTrueSource().getName().getUnformattedComponentText()
                            + "]? damaged [" + receiver.getName().getUnformattedComponentText() + "][-" + event.getAmount()
                            + "][" + healthAfterDamage + "/" + receiver.getMaxHealth()
                            + "]");
                }
                else {
                    mc.player.sendChatMessage("[" + source.getTrueSource().getName().getString()
                            + "] damaged [" + receiver.getName().getUnformattedComponentText() + "][-" + event.getAmount()
                            + "][" + healthAfterDamage + "/" + receiver.getMaxHealth()
                            + "]");
                }
            }*/
        }
        /*LOGGER.info("[" + source.getTrueSource() + "] damaged [" + receiver + "]");*/
    }

    @SubscribeEvent
    public void onItemPickup(PlayerEvent.ItemPickupEvent event) {
        ItemStack itemStack = event.getStack();
        String itemNamespace = "";
        String itemPath = "";
        Item item = itemStack.getItem(); //Somehow translates to the item name (minecraft:sand is "sand" here)
        try {
            itemNamespace = Objects.requireNonNull(item.getRegistryName()).getNamespace();
            itemPath = Objects.requireNonNull(item.getRegistryName()).getPath();
        } catch (Exception e) {
            LOGGER.info("Unable to get item namespace or path.");
        }
        String itemId = itemNamespace + ":" + itemPath;
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        String playerName = player.getName().getUnformattedComponentText();  //Just player name.

        Minecraft mc = Minecraft.getInstance();
        if(mc.player != null) {
            //mc.player.sendChatMessage("[" + playerName + "][" + player.getPlayerIP() + "] picked up [" + itemId + "][" + itemStack.getCount() + "]");
            LOGGER.info("[" + playerName + "] picked up [" + itemId + "]");
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("rpg-elements", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }

    @SubscribeEvent
    public void onLivingEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        if(event.getEntityLiving().isGlowing()) {
            Entity entity = event.getEntityLiving();
            if (glowHelper.removeGlowing(entity)) {
                messageAllPlayers("Attempted to remove glowing. [Glowing][" + entity.isGlowing() + "][UUID]["
                        + entity.getUniqueID() + "]");
            }
        }
    }

    @SubscribeEvent
    public void onEntityJoinedWorldEvent(EntityJoinWorldEvent event) {
        CompletableFuture.runAsync(() -> {
            if(event.getEntity() instanceof ItemEntity && !(event.getEntity() instanceof GlowingItemEntity)) {
                ItemEntity entity = (ItemEntity) event.getEntity();
                if(!entity.getItem().toString().contains("air")) {
                    World world = entity.world;
                    GlowingItemEntity gEntity = new GlowingItemEntity(entity, glowHelper);
                    if(world.chunkExists(gEntity.chunkCoordX,gEntity.chunkCoordZ)) {
                        event.setCanceled(true);
                        gEntity = mapIeToGe(gEntity, entity);
                        try {
                            world.addEntity(gEntity);
                        } catch (Exception e) {
                            LOGGER.error("Unable to add entity?: " + e);
                        }
                    }
                }
            }
        });
    }
}
