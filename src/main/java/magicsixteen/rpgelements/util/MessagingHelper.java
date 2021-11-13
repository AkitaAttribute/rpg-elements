package magicsixteen.rpgelements.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;

import java.util.Objects;
import java.util.UUID;

public class MessagingHelper {
    public static void messageAllPlayers(String message) {
        Objects.requireNonNull(Minecraft.getInstance().world).getPlayers().forEach(player -> {
            player.sendMessage(new StringTextComponent(message), UUID.randomUUID());
        });
    }
}
