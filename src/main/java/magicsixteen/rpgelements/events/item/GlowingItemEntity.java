package magicsixteen.rpgelements.events.item;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.OutlineLayerBuffer;
import net.minecraft.client.renderer.RenderTypeBuffers;
import net.minecraft.entity.item.ItemEntity;

import java.time.Instant;

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

    /*private void changeGlowColor() {
        IRenderTypeBuffer irendertypebuffer;
        if (this.isGlowing()) {
            OutlineLayerBuffer outlinelayerbuffer = new OutlineLayerBuffer();
            irendertypebuffer = outlinelayerbuffer;
            int i2 = entity.getTeamColor();
            int j2 = 255;
            int k2 = i2 >> 16 & 255;
            int l2 = i2 >> 8 & 255;
            int i3 = i2 & 255;
            outlinelayerbuffer.setColor(k2, l2, i3, 255);
        } else {
            irendertypebuffer = irendertypebuffer$impl;
        }

        this.renderEntity(entity, d0, d1, d2, partialTicks, matrixStackIn, irendertypebuffer);
    }*/
}
