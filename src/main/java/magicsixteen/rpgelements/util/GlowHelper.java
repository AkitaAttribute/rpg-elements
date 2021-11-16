package magicsixteen.rpgelements.util;

import net.minecraft.entity.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GlowHelper {
    private static final Logger LOGGER = LogManager.getLogger();
    private ConcurrentHashMap<UUID, Long> glowingEntities = new ConcurrentHashMap<>();
    public void addGlowing(Entity entity, int seconds) {
        long moddedTimeStamp = getCurrentTimeStampPlusSeconds(seconds);
        glowingEntities.put(entity.getUniqueID(), moddedTimeStamp);
        entity.setGlowing(true);
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
}
