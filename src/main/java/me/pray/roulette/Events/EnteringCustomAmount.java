package me.pray.roulette.Events;

import me.pray.roulette.Logic.GameTracker;
import me.pray.roulette.Logic.InventoryManager;
import me.pray.roulette.Roulette;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class EnteringCustomAmount implements Listener {

    private Roulette plugin;
    private GameTracker tracker;
    private InventoryManager manager;

    public EnteringCustomAmount(Roulette plugin, GameTracker tracker, InventoryManager manager) {
        this.plugin = plugin;
        this.tracker = tracker;
        this.manager = manager;
    }

    @EventHandler
    public void onEnteringCustomAmount(AsyncPlayerChatEvent event) {
        if (!tracker.getEnteringBets().contains(event.getPlayer().getUniqueId())) return;
        event.setCancelled(true);
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        int newAmount;

        if (event.getMessage().equalsIgnoreCase("cancel")) {
            player.sendMessage(format("&cRoulette has been cancelled!"));
            player.sendTitle("", "", 0, 1, 0);
            tracker.getEnteringBets().remove(uuid);
            tracker.getBetAmounts().remove(uuid);
            tracker.getBettingInvs().remove(uuid);
            return;
        }

        try {
            String msg = event.getMessage();
            newAmount = Integer.parseInt(msg.replace("$", ""));

            if (newAmount <= 0) {
                newAmount = 0;
            }

            String finalNewAmount = Integer.toString(newAmount);
            tracker.setBettingAmount(uuid, newAmount);

            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    manager.openDefaultInv(player, tracker.getInvOfPlayer(uuid), finalNewAmount);
                    player.sendTitle("", "", 0, 1, 0);
                    tracker.getEnteringBets().remove(uuid);
                }
            }, 1L);

        } catch (NumberFormatException nfe) {
            player.sendMessage(format("&cPlease enter a valid amount of money, or type \"cancel\" to cancel this process!"));
        }
    }

    public String format(String string) {
        return string.replace("&", "§");
    }

}
