package magicsixteen.rpgelements.util;

import net.minecraft.entity.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.*;

public class GlowHelper {
    private static final Logger LOGGER = LogManager.getLogger();
    private HashMap<UUID, Long> glowingEntities = new HashMap<>();
    public void addGlowing(Entity entity, int seconds) {
        entity.setGlowing(true);
        long moddedTimeStamp = getCurrentTimeStampPlusSeconds(seconds);
        glowingEntities.put(entity.getUniqueID(), moddedTimeStamp);
    }

    public boolean removeGlowing(Entity entity) {
        UUID uuid = entity.getUniqueID();
        long currentTimeStamp = getCurrentTimeStamp();
        try {
            if(!glowingEntities.isEmpty()) {
                if(glowingEntities.containsKey(uuid)) {
                    if (glowingEntities.get(uuid) < currentTimeStamp) {
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

    private long getCurrentTimeStamp() {
        return Instant.now().getEpochSecond();
    }

    private long getCurrentTimeStampPlusSeconds(int seconds) {
        return Instant.now().plusSeconds(seconds).getEpochSecond();
    }
}
