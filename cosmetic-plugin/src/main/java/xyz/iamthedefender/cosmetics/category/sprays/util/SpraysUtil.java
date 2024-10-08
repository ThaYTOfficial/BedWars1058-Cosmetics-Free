

package xyz.iamthedefender.cosmetics.category.sprays.util;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.hakan.core.HCore;
import com.hakan.core.particle.Particle;
import com.hakan.core.particle.type.ParticleType;
import com.hakan.core.utils.ColorUtil;
import org.bukkit.util.Vector;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.configuration.ConfigManager;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.handler.IArenaHandler;
import xyz.iamthedefender.cosmetics.category.shopkeeperskins.ShopKeeperHandler1058;
import xyz.iamthedefender.cosmetics.util.DebugUtil;
import xyz.iamthedefender.cosmetics.api.util.Utility;
import xyz.iamthedefender.cosmetics.api.util.config.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Rotation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;
import xyz.iamthedefender.cosmetics.util.FileUtil;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpraysUtil
{
    public static HashMap<String, Long> cooldown = new HashMap<>();

    /**
     * Spawn a spray for a player in an item frame.
     *
     * @param player The player for whom to spawn the spray.
     * @param itemFrame The item frame in which to display the spray.
     */
    public static void spawnSprays(Player player, ItemFrame itemFrame, boolean isPreview) {
        HCore.syncScheduler().run(() -> {
            MapView view = Bukkit.createMap(player.getWorld());
            String spray = Cosmetics.getInstance().getApi().getSelectedCosmetic(player, CosmeticsType.Sprays);
            ConfigManager config = ConfigUtils.getSprays();
            if (SpraysUtil.cooldown.containsKey(player.getName()) && !isPreview) {
                // Player is in cooldown
                long cooldownEndTime = SpraysUtil.cooldown.get(player.getName());
                if (cooldownEndTime > System.currentTimeMillis() && cooldownEndTime != 0) {
                    player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0f, 1.0f);
                    player.sendMessage(ColorUtil.colored(Utility.getMSGLang(player, "cosmetics.spray-msg")));
                    return;
                }
                SpraysUtil.cooldown.remove(player.getName());
            }
            DebugUtil.addMessage("Playing " + spray + " Spray for " + player.getDisplayName() + " is preview: " + isPreview);
           if(!isPreview){
               IArenaHandler arenaHandler = Cosmetics.getInstance().getHandler().getArenaUtil().getArenaByPlayer(player);
               if(arenaHandler == null) return;
               SpraysUtil.cooldown.put(player.getName(), System.currentTimeMillis() + 3000L);
           }
           DebugUtil.addMessage("Check 1");

            view.removeRenderer(view.getRenderers().get(0));
            final CustomRenderer renderer = new CustomRenderer();

            String sprayUrl = config.getString(CosmeticsType.Sprays.getSectionKey() + "." + spray + ".url");
            String sprayFile = config.getString(CosmeticsType.Sprays.getSectionKey() + "." + spray + ".file");

            DebugUtil.addMessage("Playing " + spray + " Spray for " + player.getDisplayName());

            if (sprayFile == null) {
                sprayFile = spray + "." + FileUtil.getFileExtension(sprayUrl);
            } else {
                sprayFile = config.getString(CosmeticsType.Sprays.getSectionKey() + "." + spray + ".file");
            }


            File file = new File(Cosmetics.getInstance().getHandler().getAddonPath() + "/" + Cosmetics.getInstance().getConfig().getString("Spray-Dir") + "/" + sprayFile);
            if (!renderer.load(file)) {
                player.sendMessage(ColorUtil.colored("&cLooks like there's an error rendering the Spray, contact the admin!"));
                Logger.getLogger("Minecraft").log(Level.SEVERE, "Could not load the File for the " + spray + ". Check if the File in Sprays.yml is valid: " + file.getPath());
            } else {
                addRendererAndShowSpray(player, itemFrame, renderer, view, isPreview);
            }
        });
    }

    /**
     * Add the renderer to the map view and display the spray.
     *
     * @param player The player for whom to display the spray.
     * @param itemFrame The item frame in which to display the spray.
     * @param renderer The custom renderer to add.
     * @param view The map view to render.
     */
    private static void addRendererAndShowSpray(Player player, ItemFrame itemFrame, CustomRenderer renderer, MapView view, boolean isPreview) {
        ItemStack map = Cosmetics.getInstance().getApi().getVersionSupport().applyRenderer(renderer, view);
        itemFrame.setItem(map);
        itemFrame.setRotation(Rotation.NONE);
        if(isPreview){
            player.getInventory().addItem(map);
            player.getInventory().setItem(0, map);
        }

        Particle particle = new Particle(ParticleType.ASH, 10, new Vector());
        HCore.playParticle(player, itemFrame.getLocation(), particle);

        for (final Entity entity : itemFrame.getNearbyEntities(1.0, 1.0, 1.0)) {
            if (entity.getType() == EntityType.ARMOR_STAND && entity.hasMetadata("HOLO_ITEM_FRAME")) {
                entity.remove();
            }
        }
    }

}


