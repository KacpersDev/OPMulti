package dev.kacperm.opmulti.gui;

import dev.kacperm.opmulti.multi.MultiType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MultiGUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof MultiGUI gui)) {
            return;
        }

        event.setCancelled(true);

        if (event.getClickedInventory() == null || !event.getClickedInventory().equals(event.getInventory())) {
            return;
        }

        MultiType type = gui.getTypeAt(event.getSlot());
        if (type == null) {
            return;
        }

        gui.toggle(type);

        if (event.getWhoClicked() instanceof Player player) {
            player.updateInventory();
        }
    }
}
