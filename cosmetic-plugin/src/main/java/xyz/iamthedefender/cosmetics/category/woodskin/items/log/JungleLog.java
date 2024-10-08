package xyz.iamthedefender.cosmetics.category.woodskin.items.log;

import com.cryptomorin.xseries.XMaterial;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.WoodSkin;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class JungleLog extends WoodSkin {
    @Override
    public ItemStack getItem() {
        return XMaterial.JUNGLE_LOG.parseItem();
    }

    @Override
    public String base64() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "jungle-log";
    }

    @Override
    public String getDisplayName() {
        return "Jungle Log";
    }

    @Override
    public List<String> getLore() {
        return List.of("&7Select the Jungle Log Wood Skin", "&7to be used when placing wood", "&7blocks.");
    }

    @Override
    public int getPrice() {
        return 55000;
    }

    @Override
    public RarityType getRarity() {
        return RarityType.LEGENDARY;
    }

    @Override
    public ItemStack woodSkin() {
        return XMaterial.JUNGLE_LOG.parseItem();
    }
}
