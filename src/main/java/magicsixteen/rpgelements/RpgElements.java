package magicsixteen.rpgelements;

import magicsixteen.rpgelements.enchantments.GlowingEnchantment;
import magicsixteen.rpgelements.events.item.GlowingItemEntity;
import magicsixteen.rpgelements.registry.EnchantmentRegistry;
import magicsixteen.rpgelements.util.GlowHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.entity.ChunkEntities;
import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.entity.item.ItemEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.entity.player.ServerPlayerEntity;
//import net.minecraft.inventory.EquipmentSlotType;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.DamageSource;
//import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
//import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

import static magicsixteen.rpgelements.util.MessagingHelper.messageAllPlayers;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("rpg_elements")
public class RpgElements {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    GlowHelper glowHelper = GlowHelper.getInstance();
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
        EnchantmentRegistry.ENCHANTMENT.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        String playerName = player.getName().getString();
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                mc.player.sendSystemMessage(sendSystemChat("Welcome! [" + playerName + "]"));
            }
            //mc.player.sendChatMessage("Welcome! [" + playerName + "]");
        } catch (Exception e) {
            LOGGER.info("Hmm, we had an issue:\t" + e);
        }
        LOGGER.info("[" + playerName + "] has logged in game.");
    }

    public Component sendSystemChat(String message) {
        return Component.nullToEmpty(message);
    }

    @SubscribeEvent
    public void onPlayerCraft(PlayerEvent.ItemCraftedEvent event) {
        ItemStack item = event.getCrafting();
        if(item.isDamageableItem()) {
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
        if(event.getEntity() instanceof Player) {
            glowDuration = 600;
        }

        ArrayList<ItemEntity> entities = new ArrayList<>(event.getDrops());
        event.getDrops().clear();

        ArrayList<GlowingItemEntity> gEntities = new ArrayList<>();

        final int temp = glowDuration;
        entities.forEach(item -> {
            GlowingItemEntity gEntity = new GlowingItemEntity(item);
            gEntity.setGlowingTick(temp);
            gEntities.add(gEntity);
        });

        event.getDrops().addAll(gEntities);
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        DamageSource unknownSource, source = new DamageSource(event.getSource().typeHolder(), event.getEntity());
        if(event.getSource() != null) {
            source = event.getSource();
        }
        LivingEntity receiver = (LivingEntity) event.getEntity();
        float healthAfterDamage = (receiver.getHealth() - event.getAmount());

        if(source.getEntity() instanceof Player) {
            /*glowHelper.addGlowing(receiver, 10);
            if(debug) {
                messageAllPlayers("Attempted to add glowing. [Glowing][" + receiver.isGlowing() + "][UUID]["
                        + receiver.getUniqueID() + "]");
            }*/


            /*Minecraft mc = Minecraft.getInstance();
            if(mc.player != null) {
                mc.player.sendChatMessage("[" + source.getTrueSource().getName().getUnformattedComponentText()
                        + "] damaged [" + receiver.getName().getString() + "][-" + event.getAmount() + "]["
                        + healthAfterDamage + "/" + receiver.getMaxHealth() + "]");
            }*/
        }

        if(receiver instanceof Player) {
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
            itemNamespace = Objects.requireNonNull(item.getDescriptionId());
            itemPath = Objects.requireNonNull(item.getDescription().getString());
        } catch (Exception e) {
            LOGGER.info("Unable to get item namespace or path.");
        }
        String itemId = itemNamespace + ":" + itemPath;
        Player player = event.getEntity();
        String playerName = player.getName().getString();  //Just player name.

        Minecraft mc = Minecraft.getInstance();
        if(mc.player != null) {
            //mc.player.sendChatMessage("[" + playerName + "][" + player.getPlayerIP() + "] picked up [" + itemId + "][" + itemStack.getCount() + "]");
            LOGGER.info("[" + playerName + "] picked up [" + itemId + "]");
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        //LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
        LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("rpg_elements", "helloworld", () -> {
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
    public void onServerStarting(ServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
//    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
//    public static class RegistryEvents {
//        @SubscribeEvent
//        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
//            // register a new block here
//            LOGGER.info("HELLO from Register Block");
//        }
//    }

    @SubscribeEvent
    public void onLivingEntityUpdate(LivingEvent.LivingTickEvent event) {
        if(event.getEntity() != null) {
            if (event.getEntity().isCurrentlyGlowing()) {
                //messageAllPlayers("Ticking glowing entities found.");
                //event.getEntity().setGlowingTag(false);
                glowHelper.removeGlowing(event.getEntity());
//                boolean result = glowHelper.removeGlowing(event.getEntity());
//                if (result) {
//                    messageAllPlayers("Attempted to remove glowing. [Glowing][" + event.getEntity().isCurrentlyGlowing() + "][UUID]["
//                            + event.getEntity().getName().getString() + "]");
//                }
            }
        }
    }

    /*@SubscribeEvent
    public static void onRegisterEnchantment(RegistryEvent.Register<Enchantment> event)
    {
        LOGGER.debug("Uhhh, can we get this event going?");
        try {
            ForgeRegistries.ENCHANTMENTS.register(new GlowingEnchantment(Enchantment.Rarity.COMMON,
                    EnchantmentType.WEAPON,
                    new EquipmentSlotType[]{EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND}));

            LOGGER.debug("Attempted to register Glowing Enchantment.");
        }
        catch (Exception e) {
            LOGGER.error("Unable to register enchantment:\t" + e);
        }
    }*/
}
