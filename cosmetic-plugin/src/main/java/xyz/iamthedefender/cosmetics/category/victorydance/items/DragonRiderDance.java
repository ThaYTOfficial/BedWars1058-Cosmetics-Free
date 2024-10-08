package xyz.iamthedefender.cosmetics.category.victorydance.items;

import com.cryptomorin.xseries.XEntity;
import com.cryptomorin.xseries.XMaterial;
import com.tomkeuper.bedwars.BedWars;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.util.NMS;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.VictoryDance;
import xyz.iamthedefender.cosmetics.api.handler.IArenaHandler;
import xyz.iamthedefender.cosmetics.category.shopkeeperskins.ShopKeeperHandler1058;
import xyz.iamthedefender.cosmetics.category.victorydance.util.UsefulUtilsVD;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class DragonRiderDance extends VictoryDance {
    @Override
    public ItemStack getItem() {
        return XMaterial.DRAGON_EGG.parseItem();
    }

    @Override
    public String base64() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "dragon-rider";
    }

    @Override
    public String getDisplayName() {
        return "Dragon Rider";
    }

    @Override
    public List<String> getLore() {
        return List.of("&7Ride a Dragon capable of", "&7destroying the map.");
    }

    @Override
    public int getPrice() {
        return 100000;
    }

    @Override
    public RarityType getRarity() {
        return RarityType.LEGENDARY;
    }

    @Override
    public void execute(Player winner) {
        // spawn the dragon and set the winner as the passenger
        EnderDragon dragon = (EnderDragon) winner.getWorld().spawnEntity(winner.getLocation(), EntityType.ENDER_DRAGON);
        dragon.setPassenger(winner);
        dragon.setNoDamageTicks(Integer.MAX_VALUE);
        // create an armorstand as the fake target
        ArmorStand stand = (ArmorStand) winner.getWorld().spawnEntity(winner.getLocation(), EntityType.ARMOR_STAND);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setMetadata("FAKE_TARGET", new FixedMetadataValue(Cosmetics.getInstance(), ""));
        // create a task to move the dragon towards the fake target
        new BukkitRunnable() {
            public void run() {
                IArenaHandler arena = Cosmetics.getInstance().getHandler().getArenaUtil().getArenaByPlayer(winner);
                if(arena == null){
                    cancel();
                    stand.remove();
                    dragon.remove();
                    return;
                }

                Creature creature = (Creature) dragon;
                creature.setTarget(stand);
                Location original = winner.getEyeLocation();
                Vector direction = winner.getEyeLocation().clone().getDirection().normalize().multiply(20);
                Location newLocation = original.add(direction);
                dragon.setVelocity(dragon.getVelocity().add(direction));
                stand.teleport(newLocation);
                for (Block block : UsefulUtilsVD.getBlocksInRadius(dragon.getLocation(), 10, false)) {
                    block.setType(Material.AIR);
                }
                if (dragon.getPassenger() != winner){
                    dragon.setPassenger(winner);
                }
                Fireball fireball = winner.getWorld().spawn(original, Fireball.class);
                fireball.setDirection(direction);
            }
        }.runTaskTimer(Cosmetics.getInstance(), 0L, 10L);
    }
}
