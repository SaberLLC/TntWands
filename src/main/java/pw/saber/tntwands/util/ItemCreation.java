package pw.saber.tntwands.util;

import com.cryptomorin.xseries.XMaterial;
import itemnbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pw.saber.tntwands.TntWands;

import java.util.ArrayList;
import java.util.List;

public class ItemCreation {

    public static ItemStack createTntWand(int uses) {
        ItemStack wandItem = new ItemStack(XMaterial.valueOf(TntWands.getInstance().getConfig().getString("TntWand.Item.Type")).parseMaterial());
        ItemMeta wandMeta = wandItem.getItemMeta();
        if (uses == -1) {
            wandMeta.setDisplayName(CC.translate(TntWands.getInstance().getConfig().getString("TntWand.Item.Display-Name-Infinite")));
        } else {
            wandMeta.setDisplayName(CC.translate(TntWands.getInstance().getConfig().getString("TntWand.Item.Display-Name").replace("{uses}", uses + "")));
        }

        List<String> conifgLore = TntWands.getInstance().getConfig().getStringList("TntWand.Item.Lore");
        List<String> lore = new ArrayList<>();
        for (String s : conifgLore) {
            lore.add(CC.translate(s).replace("{uses}", uses + ""));
        }
        wandMeta.setLore(lore);
        wandItem.setItemMeta(wandMeta);

        NBTItem nbtItem = new NBTItem(wandItem);
        nbtItem.setInteger("Uses", uses);
        return nbtItem.getItem();
    }

}
