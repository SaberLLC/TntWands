package pw.saber.tntwands.listener;

import com.cryptomorin.xseries.XMaterial;
import com.massivecraft.factions.*;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import itemnbtapi.NBTItem;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pw.saber.tntwands.TntWands;
import pw.saber.tntwands.util.CC;
import pw.saber.tntwands.util.ItemCreation;

public class InteractionListener implements Listener {

    @EventHandler
    public void onWandUse(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);

        ItemStack hand = player.getItemInHand();

        if (hand.getType() != XMaterial.valueOf(TntWands.getInstance().getConfig().getString("TntWand.Item.Type")).parseMaterial())
            return;

        NBTItem nbtItem = new NBTItem(hand);
        if (!nbtItem.hasKey("Uses")) return;

        int uses = nbtItem.getInteger("Uses");
        if (uses == -1 || uses > 0) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

                Block block = e.getClickedBlock();
                if (block == null) return;

                if (block.getType() == XMaterial.CHEST.parseMaterial() || block.getType() == XMaterial.TRAPPED_CHEST.parseMaterial()) {
                    if (!fPlayer.getFaction().isNormal()) {
                        player.sendMessage(CC.translate(TntWands.getInstance().getConfig().getString("Messages.Must-Be-In-Faction")));
                        return;
                    }

                    Faction faction = fPlayer.getFaction();
                    int limit = faction.getTntBankLimit();

                    Faction factionAt = Board.getInstance().getFactionAt(new FLocation(e.getClickedBlock().getLocation()));

                    PermissableAction permissableAction = PermissableAction.TNTBANK;
                    Access access = factionAt.getAccess(fPlayer, permissableAction);

                    if (access == Access.DENY) {
                        player.sendMessage(CC.translate(TntWands.getInstance().getConfig().getString("Messages.No-Perms").replace("{faction}", factionAt.getTag())));
                        e.setCancelled(true);
                        return;
                    }

                    int tntAmount = 0;
                    Chest chest = (Chest) block.getState();
                    Inventory inventory = chest.getInventory();
                    for (int i = 0; i < inventory.getSize(); i++) {
                        ItemStack item = inventory.getItem(i);
                        if (item != null && item.getType() == XMaterial.TNT.parseMaterial()) {
                            tntAmount += item.getAmount();
                            inventory.removeItem(item);
                        }
                    }

                    if (tntAmount > 0) {
                        player.sendMessage(CC.translate(TntWands.getInstance().getConfig().getString("Messages.Successful-Transaction").replace("{amount}", tntAmount + "")));
                        if (limit >= faction.getTnt() + tntAmount) {
                            faction.setTnt(faction.getTnt() + tntAmount);
                        } else {
                            player.sendMessage(CC.translate(TntWands.getInstance().getConfig().getString("Messages.Tnt-Bank-Limit-Reached").replace("{amount}", tntAmount + "")));
                        }
                        if (uses - 1 > 0) {
                            player.setItemInHand(ItemCreation.createTntWand(uses - 1));
                        } else if (uses != -1) {
                            player.setItemInHand(null);
                        }
                    }
                    e.setCancelled(true);
                }
            }
        }
    }
}
