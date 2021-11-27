package magicsixteen.rpgelements.registry;

import magicsixteen.rpgelements.enchantments.GlowingEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantmentRegistry {
    public static final DeferredRegister<Enchantment> ENCHANTMENT = DeferredRegister
            .create(ForgeRegistries.ENCHANTMENTS, "magic-sixteen");

    public static final RegistryObject<Enchantment> ORACLE_STRIKE = ENCHANTMENT.register("oracle_strike", GlowingEnchantment::new);
}
