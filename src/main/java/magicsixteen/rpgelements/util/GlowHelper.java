package magicsixteen.rpgelements.util;

import magicsixteen.rpgelements.events.item.GlowingItemEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.*;

import static magicsixteen.rpgelements.util.MessagingHelper.messageAllPlayers;

public class GlowHelper {
    private static GlowHelper instance;
    private static final Logger LOGGER = LogManager.getLogger();
    private HashMap<UUID, Long> glowingEntities = new HashMap<>();
    // private ArrayList<UUID> entityIds = new ArrayList<>();
    // private ArrayList<Long> glowDurations = new ArrayList<>();

    public static GlowHelper getInstance() {
        if(instance == null) {
            instance = new GlowHelper();
        }
        return instance;
    }

    public void addGlowing(Entity entity, int seconds) {
        long moddedTimeStamp = getCurrentTimeStampPlusSeconds(seconds);
        glowingEntities.put(entity.getUUID(), moddedTimeStamp);
        setGlowColor(entity);
        entity.setGlowingTag(true);
        // messageAllPlayers("Tried to add glowing. [GlowingEntities][" +  entityIds.size() + "]");
    }

    public boolean removeGlowing(Entity entity) {
        UUID uuid = entity.getUUID();
        long currentTimeStamp = getCurrentTimeStamp();

        try {
            if(!glowingEntities.isEmpty()) {
                if(glowingEntities.containsKey(uuid)) {
                    if (glowingEntities.get(uuid) < currentTimeStamp) {
                        entity.setGlowingTag(false);
                        if(entity.isCurrentlyGlowing()) {
                            //messageAllPlayers("Tried to remove glowing. [GlowingEntities][" +  entityIds.size() + "]");
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

    private void setGlowColor(Entity entity) {
        Scoreboard scoreboard = entity.level().getScoreboard();
        PlayerTeam team = scoreboard.addPlayerTeam(entity.getUUID().toString());
        if (entity instanceof Enemy) {
            team.setColor(ChatFormatting.RED);
        } else if (entity instanceof Animal || entity instanceof Npc || entity instanceof WaterAnimal) {
            team.setColor(ChatFormatting.GREEN);
        }
//        else if (entity instanceof NeutralMob) {
//            LivingEntity lastHurtBy = ((NeutralMob) entity).getLastHurtByMob();
//            if ((lastHurtBy != null)) {
//                if (((NeutralMob) entity).canAttack(lastHurtBy)) {
//                    if (entity instanceof OwnableEntity) {
//                        if (lastHurtBy.equals(((OwnableEntity) entity).getOwner())) {
//                            team.setColor(ChatFormatting.GREEN);
//                        }
//                    }
//                    else {
//                        team.setColor(ChatFormatting.RED);
//                    }
//                } else {
//                    team.setColor(ChatFormatting.GREEN);
//                }
//            }
//        }

        scoreboard.addPlayerToTeam(entity.getScoreboardName(), team);
    }
}
