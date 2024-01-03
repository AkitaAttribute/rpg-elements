package magicsixteen.rpgelements.events.mob;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GlowingCreatureEntity extends Monster {

    public GlowingCreatureEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }
}
