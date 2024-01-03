package magicsixteen.rpgelements.util;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.UUID;

public class MessagingHelper {
    private static final Logger LOGGER = LogManager.getLogger();
    public static void messageAllPlayers(String message) {
        try {
            Objects.requireNonNull(Minecraft.getInstance().level).players().forEach(player -> {
                // player.sendMessage(new StringTextComponent(message), UUID.randomUUID());
                player.sendSystemMessage(Component.nullToEmpty(message));
            });
        } catch (Exception e) {
            LOGGER.error("Error sending message to players: [" + e + "]");
            LOGGER.debug(message);
        }
    }
}
