package magicsixteen.rpgelements.events.item;

import net.minecraft.entity.item.ItemEntity;

import java.time.Instant;
import java.util.Objects;
import static magicsixteen.rpgelements.util.MessagingHelper.messageAllPlayers;

public class GlowingItemEntity extends ItemEntity {
    long duration = 0;

    public GlowingItemEntity(ItemEntity entity) {
        super(entity.world, entity.getPosX(), entity.getPosY(), entity.getPosZ());
        this.setItem(entity.getItem().copy());
        this.copyLocationAndAnglesFrom(entity);
    }

    @Override
    public void tick() {
        if(this.isGlowing()) {
            if(duration < getCurrentTimeStamp()) {
                this.setGlowing(false);
                messageAllPlayers("Tried to remove glowing. [GlowingEntities][" +
                        Objects.requireNonNull(this.getItem().getItem().getRegistryName()).getPath() +
                        "]");
            }
        }
        super.tick();
    }

    public void setGlowingTick(int duration) {
        if(duration > 0) {
            this.setGlowing(true);
        }
        this.duration = getCurrentTimeStampPlusSeconds(duration);
    }

    private long getCurrentTimeStamp() {
        return Instant.now().getEpochSecond();
    }

    private long getCurrentTimeStampPlusSeconds(int seconds) {
        return Instant.now().plusSeconds(seconds).getEpochSecond();
    }
}
