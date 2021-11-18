package magicsixteen.rpgelements.util;

import magicsixteen.rpgelements.events.item.GlowingItemEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static magicsixteen.rpgelements.util.MessagingHelper.messageAllPlayers;

public class GlowHelper {
    private static final Logger LOGGER = LogManager.getLogger();
    private HashMap<UUID, Long> glowingEntities = new HashMap<>();
    private HashMap<UUID, Integer> glowingTracker = new HashMap<>();

    public void addGlowing(Entity entity, int seconds) {
        long moddedTimeStamp = getCurrentTimeStampPlusSeconds(seconds);
        glowingEntities.put(entity.getUniqueID(), moddedTimeStamp);
        entity.setGlowing(true);
        messageAllPlayers("Tried to add glowing. [GlowingEntities][" +  glowingEntities.size() + "]");
    }

    public void addTracker(UUID uuid, int duration) {
        glowingTracker.put(uuid, duration);
    }

    public int getDuration(UUID uuid) {
        if(glowingTracker.containsKey(uuid)) {
            int duration = glowingTracker.get(uuid);
            try {
                glowingTracker.remove(uuid);
            }
            catch (Exception e) {
                LOGGER.error("Failed to remove?  Already Removed?:\t" + e);
            }
            return duration;
        }
        return 0;
    }

    public boolean removeGlowing(Entity entity) {
        UUID uuid = entity.getUniqueID();
        long currentTimeStamp = getCurrentTimeStamp();
        try {
            if(!glowingEntities.isEmpty()) {
                if(glowingEntities.containsKey(uuid)) {
                    if (glowingEntities.get(uuid) < currentTimeStamp) {
                        entity.setGlowing(false);
                        if(entity.isGlowing()) {
                            messageAllPlayers("Tried to remove glowing. [GlowingEntities][" +  glowingEntities.size() + "]");
                            return false;
                        }
                        else {
                            glowingEntities.remove(uuid);
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.info("Caught Exception During Removal: [" + e + "]");
        }
        return false;
    }

    private long getCurrentTimeStamp() {
        return Instant.now().getEpochSecond();
    }

    private long getCurrentTimeStampPlusSeconds(int seconds) {
        return Instant.now().plusSeconds(seconds).getEpochSecond();
    }

    public void updateGlowingRecord(ItemEntity rawEntity, GlowingItemEntity rawGEntitiy) {
        UUID gEntitiy = rawGEntitiy.getUniqueID();
        UUID entity = rawEntity.getUniqueID();
        if(!glowingEntities.containsKey(gEntitiy) && glowingEntities.containsKey(entity)) {
            messageAllPlayers("Key not found, manually swapping... [" + rawEntity.getItem() + "]->[" + rawGEntitiy.getItem() + "]");
            glowingEntities.put(gEntitiy, glowingEntities.get(entity));
            glowingEntities.remove(entity);
        }
    }

    public boolean checkIfRegistered(UUID uuid) {
        return glowingEntities.containsKey(uuid);
    }
}
