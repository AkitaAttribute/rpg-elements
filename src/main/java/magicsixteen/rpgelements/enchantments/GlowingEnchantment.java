package magicsixteen.rpgelements.enchantments;

import magicsixteen.rpgelements.RpgElements;
import magicsixteen.rpgelements.util.GlowHelper;
//import net.minecraft.enchantment.Enchantment;
//import net.minecraft.enchantment.EnchantmentType;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.inventory.EquipmentSlotType;
//import net.minecraft.item.ItemStack;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.network.chat.Component;

import java.io.InputStream;
import java.util.Properties;

import static magicsixteen.rpgelements.util.MessagingHelper.messageAllPlayers;

public class GlowingEnchantment extends Enchantment {
    private GlowHelper glowHelper = GlowHelper.getInstance();
    private static final Logger LOGGER = LogManager.getLogger();

    public GlowingEnchantment(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
        try (InputStream input = RpgElements.class.getClassLoader().getResourceAsStream("mod.properties")) {
            //Properties properties = new Properties();
            //properties.load(input);
            //String modId = properties.getProperty("mod.id");

            //setRegistryName("oracle_strike");
        }
        catch (Exception e) {
            LOGGER.error("Unable to find mod id.");
        }
    }

    public GlowingEnchantment() {
        super(Rarity.COMMON, Enchantments.SHARPNESS.category, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return true;
    }


    public int getMinCost(int enchantmentLevel)
    {
        return 1;
    }


    public int getMaxCost(int enchantmentLevel)
    {
        return this.getMinCost(enchantmentLevel) + 10;
    }

    @Override
    public int getMaxLevel()
    {
        return 1;
    }


    public boolean canEnchant(ItemStack stack)
    {
        return stack.getItem().isDamageable(stack);
    }


    public void doPostAttack(LivingEntity user, Entity target, int level)
    {
        glowHelper.addGlowing(target, 10);

//        messageAllPlayers("Attempted to add glowing. [Glowing][" + target.isCurrentlyGlowing() + "][UUID]["
//                + target.getUUID() + "]");

    }

    @Override
    public Component getFullname(int eLevel) {
        return Component.nullToEmpty("Oracle's Mark").copy().withStyle(ChatFormatting.GRAY);
    }



//    public boolean isTreasureOnly()
//    {
//        return true;
//    }
}
