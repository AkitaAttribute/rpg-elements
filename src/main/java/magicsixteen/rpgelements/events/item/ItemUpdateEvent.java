package magicsixteen.rpgelements.events.item;

import net.minecraft.entity.item.ItemEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class ItemUpdateEvent extends ItemEvent {

    /**
     * Creates a new event for an EntityItem.
     *
     * @param itemEntity The EntityItem for this event
     */
    public ItemUpdateEvent(ItemEntity itemEntity) {
        super(itemEntity);
    }

}
