package me.pray.roulette.Events;

import me.pray.roulette.Logic.GameTracker;
import me.pray.roulette.Roulette;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.UUID;

public class CloseRouletteInv implements Listener {

    private Roulette plugin;
    private GameTracker tracker;

    public CloseRouletteInv(Roulette plugin, GameTracker tracker) {
        this.plugin = plugin;
        this.tracker = tracker;
    }

    @EventHandler
    public void onRouletteInvClose(InventoryCloseEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (event.getInventory() != tracker.getInvOfPlayer(uuid)) return;
        if (tracker.getEnteringBets().contains(uuid)) return;
        if (tracker.getIsRollingMachine().contains(event.getPlayer().getUniqueId())) {
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    event.getPlayer().openInventory(tracker.getInvOfPlayer(uuid));
                }
            }, 1L);
        } else {
            tracker.getBetAmounts().remove(uuid);
            tracker.getBettingInvs().remove(uuid);
        }

    }
}
