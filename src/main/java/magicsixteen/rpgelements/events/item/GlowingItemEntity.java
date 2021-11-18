package magicsixteen.rpgelements.events.item;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static magicsixteen.rpgelements.util.MessagingHelper.messageAllPlayers;

public class GlowingItemEntity extends ItemEntity {
    int glowingTick = 0;

    public GlowingItemEntity(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public GlowingItemEntity(ItemEntity entity) {
        super(entity.world, entity.getPosX(), entity.getPosY(), entity.getPosZ());
        this.setItem(entity.getItem().copy());
        this.copyLocationAndAnglesFrom(entity);
    }

    @Override
    public void tick() {
        if(this.isGlowing()) {
            if(glowingTick < 0) {
                this.setGlowing(false);
                messageAllPlayers("Tried to remove glowing. [GlowingEntities][" +
                        Objects.requireNonNull(this.getItem().getItem().getRegistryName()).getNamespace() +
                        "]");
            }
            else {
                glowingTick --;
            }
        }
        super.tick();
    }

    public void addToWorld() {
        CompletableFuture.runAsync(() -> {  //Do this garbage on another thread.
            try {
                this.world.addEntity(this);
            } catch (Exception e) {
                LOGGER.error("Unable to add entity?: " + e);
            }
        });
    }

    public void setGlowingTick(int glowingTick) {
        if(glowingTick > 0) {
            this.setGlowing(true);
        }
        this.glowingTick = glowingTick;
    }

    public int getGlowingTick() {
        return glowingTick;
    }

    public static GlowingItemEntity mapIeToGe(GlowingItemEntity gEntity, ItemEntity entity) {
        double px = entity.getPosX();
        double py = entity.getPosY();
        double pz = entity.getPosZ();
        //gEntity.setDead();
        gEntity.setItem(entity.getItem().getStack());
        gEntity.setMotion(entity.getMotion());
        gEntity.setGlowing(entity.isGlowing());
        gEntity.setInvisible(entity.isInvisible());
        gEntity.setUniqueId(UUID.randomUUID());  //Maybe needs to be random?  Maybe needs to be same as original?
        gEntity.setPosition(px, py, pz);
        gEntity.setCustomNameVisible(entity.isCustomNameVisible());
        gEntity.setAir(entity.getAir());
        gEntity.setBoundingBox(entity.getBoundingBox());
        gEntity.setDefaultPickupDelay();
        //gEntity.setEntityId(entity.getEntityId());
        gEntity.setFire(entity.getFireTimer());
        gEntity.setInvulnerable(entity.isInvulnerable());
        gEntity.setNoGravity(entity.hasNoGravity());
        gEntity.setOnGround(entity.isOnGround());
        gEntity.setOwnerId(entity.getOwnerId());
        //messageAllPlayers("[" + px + "][" + py + "][" + pz + "][" + gEntity.getItem().getItem() + "]");
        return gEntity;
    }

    private ItemEntity mapGeToIe(GlowingItemEntity gEntity, ItemEntity entity) {
        double px = gEntity.getPosX();
        double py = gEntity.getPosY();
        double pz = gEntity.getPosZ();
        entity.setItem(gEntity.getItem());
        entity.setMotion(gEntity.getMotion());
        entity.setGlowing(gEntity.isGlowing());
        entity.setInvisible(gEntity.isInvisible());
        entity.setUniqueId(gEntity.getUniqueID());
        entity.setPosition(px, py, pz);
        entity.setCustomNameVisible(gEntity.isCustomNameVisible());
        entity.setAir(gEntity.getAir());
        entity.setBoundingBox(gEntity.getBoundingBox());
        entity.setDefaultPickupDelay();
        //entity.setEntityId(gEntity.getEntityId());
        entity.setFire(gEntity.getFireTimer());
        entity.setInvulnerable(gEntity.isInvulnerable());
        entity.setNoGravity(gEntity.hasNoGravity());
        entity.setOnGround(gEntity.isOnGround());
        entity.setOwnerId(gEntity.getOwnerId());
        return entity;
    }
}
