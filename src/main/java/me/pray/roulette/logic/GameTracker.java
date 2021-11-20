package me.pray.roulette.logic;

import me.pray.roulette.Roulette;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class GameTracker {

    private Roulette plugin;

    private HashMap<UUID, Inventory> bettingInventories = new HashMap<>();
    private HashMap<UUID, Integer> betAmount = new HashMap<UUID, Integer>();
    private ArrayList<UUID> isEnteringBet = new ArrayList<>();
    private ArrayList<UUID> isRollingMachine = new ArrayList<>();


    public GameTracker(Roulette plugin) {
        this.plugin = plugin;
    }

    public int getBetAmountOfPlayer(UUID playerUUID) {
        return getBetAmounts().getOrDefault(playerUUID, 0);
    }

    public void setBettingAmount(UUID uuid, int value) {

        boolean positive = value >= 0;

        if (!positive) {
            getBetAmounts().put(uuid, 0);
        } else {
            getBetAmounts().put(uuid, value);
        }
    }

    public Inventory getInvOfPlayer(UUID uuid) {
        return getBettingInvs().get(uuid);
    }

    public void addToRollAmountInInv(Inventory inv, Player player, int amount) {
        if (inv.getItem(13).getItemMeta() == null) return;

        ItemStack item = inv.getItem(13);
        ItemMeta meta = item.getItemMeta();

        boolean positive = amount > 0;

        String title = meta.getDisplayName();
        String message = ChatColor.stripColor(title.replace("Roll ", "").replace("(", "").replace("$", "").replace(")", "").replace("?", ""));
        int initialAmount;
        if (message.isEmpty()) {
            initialAmount = getBetAmountOfPlayer(player.getUniqueId());
        } else {
            initialAmount = Integer.parseInt(message);
        }

        if (!positive) {
            if ((initialAmount + amount) <= 0) {
                meta.setDisplayName(format("&9Roll &7($0)"));
                item.setItemMeta(meta);
                return;
            }
        }

        meta.setDisplayName(format("&9Roll &7($" + (initialAmount + amount) + ")"));
        item.setItemMeta(meta);
    }

    public void setRollAmountInInv(Inventory inv, Player player, int amount) {
        if (inv.getItem(13).getItemMeta() == null) return;

        ItemStack item = inv.getItem(13);
        ItemMeta meta = item.getItemMeta();

        if (amount <= 0) {
            meta.setDisplayName(format("&9Roll &7($0)"));
            item.setItemMeta(meta);
            return;
        }

        meta.setDisplayName(format("&9Roll &7($" + amount + ")"));
        item.setItemMeta(meta);

        player.updateInventory();
    }

    public HashMap<UUID, Inventory> getBettingInvs() {
        return bettingInventories;
    }

    public HashMap<UUID, Integer> getBetAmounts() {
        return betAmount;
    }

    public ArrayList<UUID> getEnteringBets() {
        return isEnteringBet;
    }

    public ArrayList<UUID> getIsRollingMachine() {
        return isRollingMachine;
    }

    public ArrayList<UUID> getIsEnteringBet() {
        return isEnteringBet;
    }

    public String format(String string) {
        return string.replace("&", "§");
    }

}
