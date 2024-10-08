package xyz.iamthedefender.cosmetics.category.victorydance.items;

import com.cryptomorin.xseries.XMaterial;
import com.hakan.core.HCore;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.VictoryDance;
import xyz.iamthedefender.cosmetics.category.shopkeeperskins.ShopKeeperHandler1058;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class NightShiftDance extends VictoryDance {
    @Override
    public ItemStack getItem() {
        return XMaterial.CLOCK.parseItem();
    }

    @Override
    public String base64() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "night-shift";
    }

    @Override
    public String getDisplayName() {
        return "Night Shift";
    }

    @Override
    public List<String> getLore() {
        return List.of("&7Set it to day, set it to night,", "&7repeat, like a god");
    }

    @Override
    public int getPrice() {
        return 10000;
    }

    @Override
    public RarityType getRarity() {
        return RarityType.RARE;
    }

    @Override
    public void execute(Player winner) {
        HCore.syncScheduler().every(8L).run((runnable) -> {
                if(Cosmetics.getInstance().getHandler().getArenaUtil().getArenaByPlayer(winner) == null) {
                    runnable.cancel();
                    return;
                }

                long time = winner.getWorld().getTime() + 1000;
                if (time > 24000) {
                    time = 0;
                }
                winner.getWorld().setTime(time);

        });
    }
}
