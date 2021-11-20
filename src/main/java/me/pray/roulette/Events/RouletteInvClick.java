package me.pray.roulette.Events;

import me.pray.roulette.Logic.GameTracker;
import me.pray.roulette.Logic.SpinLogic;
import me.pray.roulette.Roulette;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class RouletteInvClick implements Listener {

    private GameTracker tracker;
    private SpinLogic sl;

    public RouletteInvClick(GameTracker tracker, SpinLogic sl) {
        this.tracker = tracker;
        this.sl = sl;
    }

    @EventHandler
    public void onRouletteInvClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (tracker.getInvOfPlayer(event.getWhoClicked().getUniqueId()) == null) return;

        Inventory inv = tracker.getInvOfPlayer(event.getWhoClicked().getUniqueId());

        if (event.getClickedInventory() != inv) return;
        if (event.getCurrentItem() == null) return;

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();

        if (tracker.getIsRollingMachine().contains(uuid)) return;

        switch (event.getSlot()) {
            case 10:
                player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
                tracker.setBettingAmount(uuid, tracker.getBetAmountOfPlayer(uuid) + 100);
                tracker.addToRollAmountInInv(inv, player, 100);
                break;
            case 11:
                tracker.getEnteringBets().add(uuid);
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
                player.sendTitle(format("&aEnter amount..."), format("&c\"cancel\" &fto cancel!"), 20, 2000, 20);
                player.sendMessage(format("&aPlease enter a custom bet amount, or type &c\"cancel\" &ato cancel!"));
                break;
            case 13:
                if (tracker.getBetAmountOfPlayer(uuid) == 0) {
                    player.sendMessage(format("&cYou cannot bet $0!"));
                    break;
                }

                int bal = (int) Roulette.econ.getBalance(Bukkit.getOfflinePlayer(uuid));
                int betAmount = tracker.getBetAmountOfPlayer(uuid);

                if (bal < betAmount) {
                    player.sendMessage(format("&cYou cannot bet " + betAmount + ", because you only have " + bal));
                    break;
                }

                sl.rollMachine(uuid);
                break;
            case 15:
                player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
                tracker.setBettingAmount(uuid, tracker.getBetAmountOfPlayer(uuid) - 100);
                tracker.addToRollAmountInInv(inv, player, -100);
                break;
            case 16:
                player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
                tracker.setBettingAmount(uuid, 0);
                tracker.setRollAmountInInv(inv, player, 0);
                break;
        }
    }

    public String format(String string) {
        return string.replace("&", "§");
    }

}
