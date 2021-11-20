package me.pray.roulette.Logic;

import me.pray.roulette.Roulette;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.UUID;

public class SpinLogic {

    private Roulette plugin;
    private GameTracker tracker;

    public SpinLogic(Roulette plugin, GameTracker tracker) {
        this.plugin = plugin;
        this.tracker = tracker;
    }

    public void rollMachine(UUID uuid) {
        tracker.getIsRollingMachine().add(uuid);

        Player player = Bukkit.getPlayer(uuid);
        Inventory inv = tracker.getInvOfPlayer(uuid);

        int win = (int) (Math.random() * 5);
        int bet = tracker.getBetAmountOfPlayer(uuid);

        EconomyResponse response;

        if (win == 0) {
            // 0 = win
            response = Roulette.econ.depositPlayer(Bukkit.getOfflinePlayer(uuid), bet);
        } else {
            // 1, 2, 3, 4 = lost
            response = Roulette.econ.withdrawPlayer(Bukkit.getOfflinePlayer(uuid), bet);
        }


        int rolling = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                flip(inv, player);
                player.playNote(player.getLocation(), Instrument.STICKS, Note.natural(1, Note.Tone.A));
            }
        }, 0L, 5L);

        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                Bukkit.getScheduler().cancelTask(rolling);
                tracker.getIsRollingMachine().remove(uuid);

                //flipping to correct block based off of win or lose
                if (win == 0) {
                    if (inv.getItem(40).getType() == Material.BLACK_CONCRETE) {
                        flip(inv, player);
                    }
                    plugin.getServer().broadcastMessage(format("&6ROULETTE &8» &f" + player.getDisplayName() + " &7has just won &adouble &7their money in the &6Roulette Game &7(/roulette)"));
                } else {
                    if (inv.getItem(40).getType() == Material.RED_CONCRETE) {
                        flip(inv, player);
                    }
                    player.sendMessage(format("&cYou lost $" + (int) response.amount));
                }
            }
        }, 160L);

    }

    private void flip(Inventory inv, Player player) {
        ItemStack lostMoney = newItem(Material.BLACK_CONCRETE, "&cLost your money", null);
        ItemStack doubleMoney = newItem(Material.RED_CONCRETE, "&aDouble your money", null);

        for (int i = 36; i < 45; i++) {
            if (inv.getItem(i).getType() == Material.RED_CONCRETE) {
                inv.setItem(i, lostMoney);
            } else {
                inv.setItem(i, doubleMoney);
            }
        }

        player.updateInventory();
    }

    public String format(String string) {
        return string.replace("&", "§");
    }

    public ItemStack newItem(Material mat, String name, ArrayList<String> lore) {
        ItemStack is = new ItemStack(mat);
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(format(name));
        if (meta != null) {
            meta.setLore(lore);
        }
        is.setItemMeta(meta);

        return is;
    }
}
