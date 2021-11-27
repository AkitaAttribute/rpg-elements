package magicsixteen.rpgelements.enchantments;

import magicsixteen.rpgelements.RpgElements;
import magicsixteen.rpgelements.util.GlowHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;

import static magicsixteen.rpgelements.util.MessagingHelper.messageAllPlayers;

public class GlowingEnchantment extends Enchantment {
    private GlowHelper glowHelper = new GlowHelper();
    private static final Logger LOGGER = LogManager.getLogger();

    public GlowingEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
        try (InputStream input = RpgElements.class.getClassLoader().getResourceAsStream("mod.properties")) {
            //Properties properties = new Properties();
            //properties.load(input);
            //String modId = properties.getProperty("mod.id");
            setRegistryName("oracle_strike");
        }
        catch (Exception e) {
            LOGGER.error("Unable to find mod id.");
        }
    }

    public GlowingEnchantment() {
        super(Rarity.COMMON, EnchantmentType.WEAPON, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND});
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return true;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel)
    {
        return 1;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel)
    {
        return this.getMinEnchantability(enchantmentLevel) + 10;
    }

    @Override
    public int getMaxLevel()
    {
        return 1;
    }

    @Override
    public boolean canApply(ItemStack stack)
    {
        return stack.getItem().isDamageable();
    }

    @Override
    public void onEntityDamaged(LivingEntity user, Entity target, int level)
    {
        glowHelper.addGlowing(target, 10);

        messageAllPlayers("Attempted to add glowing. [Glowing][" + target.isGlowing() + "][UUID]["
                + target.getUniqueID() + "]");

    }

    @Override
    public boolean isTreasureEnchantment()
    {
        return true;
    }
}
