package me.pray.roulette.commands;

import me.pray.roulette.logic.GameTracker;
import me.pray.roulette.logic.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class RouletteCommand implements CommandExecutor {

    private GameTracker tracker;
    private InventoryManager manager;

    public RouletteCommand(GameTracker tracker, InventoryManager manager) {
        this.tracker = tracker;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(format("&4Only players can use this!"));
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("roulette.use")) {
            player.sendMessage(format("&cNo permission."));
            return true;
        }

        if (tracker.getIsEnteringBet().contains(player.getUniqueId())) {
            player.sendMessage(format("&cPlease enter a valid amount or type \"cancel\" before continuing!"));
            return true;
        }

        Inventory rouletteInv = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Roulette");

        tracker.getBettingInvs().put(player.getUniqueId(), rouletteInv);

        manager.openDefaultInv(player, rouletteInv, Integer.toString(tracker.getBetAmountOfPlayer(player.getUniqueId())));

        return true;
    }

    public String format(String string) {
        return string.replace("&", "§");
    }
}
