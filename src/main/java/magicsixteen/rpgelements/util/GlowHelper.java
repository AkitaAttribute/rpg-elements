package magicsixteen.rpgelements.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static magicsixteen.rpgelements.util.MessagingHelper.messageAllPlayers;

public class GlowHelper {
    private static final Logger LOGGER = LogManager.getLogger();
    //private ArrayList<Entity> glowingEntities = new ArrayList<>();
    private HashMap<UUID, Long> glowingEntities = new HashMap<>();
    private boolean thingsGlowing;

    public void addGlowing(Entity entity, int seconds) {
        entity.setGlowing(true);
        long moddedTimeStamp = getCurrentTimeStampPlusSeconds(seconds);

        //messageAllPlayers("Adding [" + entity.toString() + "]");
        messageAllPlayers("Attempted to add glowing. [Glowing][" + entity.isGlowing() + "][UUID]["
                + entity.getUniqueID() + "]");
        glowingEntities.put(entity.getUniqueID(), moddedTimeStamp);
    }

    public boolean removeGlowing(Entity entity) {
        UUID uuid = entity.getUniqueID();
        long currentTimeStamp = getCurrentTimeStamp();
        try {
            if(!glowingEntities.isEmpty()) {
                if(glowingEntities.containsKey(uuid)) {
                    if (glowingEntities.get(uuid) < currentTimeStamp) {
                        //entity.setGlowing(false);
                        //messageAllPlayers("Removing [" + entity.toString() + "]");
                        glowingEntities.remove(uuid);
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.info("Caught Exception During Removal: [" + e + "]");
        }
        return false;
    }

    /*
        While list has items in it, run.  If empty, stop the thread.  When added, check if thread is running.
        When added, start glowing.  Thread is to check if time has passed every second.  Maybe in the future,
        we can change it to check on x intervals where x is the length at which the entity is supposed to glow for.
        This could make it more efficient.
     */
    /*private void doGlowing() {
        thingsGlowing = true;
        CompletableFuture.runAsync(() -> {
            messageAllPlayers("Glowing thread started.");
            String currentTimeStamp;
            while (!glowingEntities.isEmpty()) {
                currentTimeStamp = getCurrentTimeStamp();
                try {
                    List<Entity> entities = glowingEntities.get(currentTimeStamp);
                    if(entities != null) {
                        //messageAllPlayers("Glowing entities found: [" + entities.toString() + "]");
                        String finalCurrentTimeStamp = currentTimeStamp;
                        entities.forEach(entity -> {
                            messageAllPlayers("Attempting to remove: [" + finalCurrentTimeStamp + "][" + entity.toString() + "]");
                            try {

                                Minecraft.getInstance().world.getEntityByID(entity.getEntityId()).setGlowing(false);
                            } catch (Exception e) {
                                messageAllPlayers("Error removing glowing from entity. [" + e.getMessage() + "]");
                            }
                            //entity.setGlowing(false);
                            entities.remove(entity);
                        });
                    }
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    LOGGER.debug("Error sleeping:\t" + e);
                }
                //item.setGlowing(false);
            }
            thingsGlowing = false;
            messageAllPlayers("Glowing thread completed.");
        });
    }*/

    private long getCurrentTimeStamp() {
        return Instant.now().getEpochSecond();
    }

    private long getCurrentTimeStampPlusSeconds(int seconds) {
        return Instant.now().plusSeconds(seconds).getEpochSecond();
    }
}
