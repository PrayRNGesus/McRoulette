package me.pray.roulette.logic;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class InventoryManager {

    private GameTracker tracker;

    public InventoryManager(GameTracker tracker) {
        this.tracker = tracker;
    }

    public void openDefaultInv(Player player, Inventory inv, String amount) {

        if (amount == null) {
            amount = "???";
        }

        ArrayList<String> howToPlayLore = new ArrayList<>();
        howToPlayLore.add(" ");
        howToPlayLore.add(format("&7Simply put the money of which you"));
        howToPlayLore.add(format("&7want to gamble with."));
        howToPlayLore.add(" ");
        howToPlayLore.add(format("&7You will simply double it, or lose it."));
        inv.setItem(4, newItem(Material.BOOK, "&eHOW TO PLAY", howToPlayLore));

        ArrayList<String> oneHundredBetLore = new ArrayList<>();
        oneHundredBetLore.add(" ");
        oneHundredBetLore.add(format("&7This bet will take $100 out of your"));
        oneHundredBetLore.add(format("&7balance and add it to the roulette machine."));
        inv.setItem(10, newItem(Material.LIME_STAINED_GLASS_PANE, "&aBet $100 &7(Click to add $100 More)", oneHundredBetLore));

        ArrayList<String> customAmountLore = new ArrayList<>();
        customAmountLore.add(" ");
        customAmountLore.add(format("&7This bet will take a custom amount of your"));
        customAmountLore.add(format("&7balance and add it to the roulette machine."));
        inv.setItem(11, newItem(Material.LIME_STAINED_GLASS_PANE, "&aBet $" + amount + " &7(Set a Custom Amount)", customAmountLore));

        ArrayList<String> rollLore = new ArrayList<>();
        rollLore.add(" ");
        rollLore.add(format("&7Click here to test your luck"));
        rollLore.add(format("&7with the roulette machine."));
        inv.setItem(13, newItem(Material.COMPASS, "&9Roll &7($" + amount + ")", rollLore));

        ArrayList<String> removeOneHundredLore = new ArrayList<>();
        removeOneHundredLore.add(" ");
        removeOneHundredLore.add(format("&7This will remove $100 from your"));
        removeOneHundredLore.add(format("&7bet and add it to the roulette machine"));
        inv.setItem(15, newItem(Material.RED_STAINED_GLASS_PANE, "&cRemove $100", removeOneHundredLore));

        ArrayList<String> clearBetsLore = new ArrayList<>();
        clearBetsLore.add(" ");
        clearBetsLore.add(format("&7This will refund all your money"));
        clearBetsLore.add(format("&7incase you changed your mind"));
        inv.setItem(16, newItem(Material.RED_STAINED_GLASS_PANE, "&cRemove All Bets", clearBetsLore));

        for (int i = 27; i < 36; i++) {
            if (i == 31) {
                inv.setItem(i, newItem(Material.BLUE_STAINED_GLASS_PANE, " ", null));
                continue;
            }
            inv.setItem(i, newItem(Material.GRAY_STAINED_GLASS_PANE, " ", null));
        }

        for (int i = 36; i < 45; i++) {
            if (isEven(i)) {
                inv.setItem(i, newItem(Material.BLACK_CONCRETE, "&cLost your money", null));
            } else {
                inv.setItem(i, newItem(Material.RED_CONCRETE, "&aDouble your money", null));
            }
        }

        for (int i = 45; i < 54; i++) {
            if (i == 49) {
                inv.setItem(i, newItem(Material.BLUE_STAINED_GLASS_PANE, " ", null));
                continue;
            }
            inv.setItem(i, newItem(Material.GRAY_STAINED_GLASS_PANE, " ", null));
        }

        tracker.setBettingAmount(player.getUniqueId(), tracker.getBetAmountOfPlayer(player.getUniqueId()));

        player.openInventory(inv);
    }

    public String format(String string) {
        return string.replace("&", "§");
    }

    public boolean isEven(int value) {
        return value % 2 == 0;
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
