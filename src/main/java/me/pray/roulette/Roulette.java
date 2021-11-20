package me.pray.roulette;

import me.pray.roulette.commands.RouletteCommand;
import me.pray.roulette.events.CloseRouletteInv;
import me.pray.roulette.events.EnteringCustomAmount;
import me.pray.roulette.events.RouletteInvClick;
import me.pray.roulette.logic.GameTracker;
import me.pray.roulette.logic.InventoryManager;
import me.pray.roulette.logic.SpinLogic;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Roulette extends JavaPlugin {

    GameTracker tracker = new GameTracker(this);
    SpinLogic spinLogic = new SpinLogic(this, tracker);
    InventoryManager manager = new InventoryManager(tracker);

    public static final Logger log = Logger.getLogger("Minecraft");
    public static Economy econ = null;
    public static Permission perms = null;
    public static Chat chat = null;

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();

        registerCommandsAndEvents();
    }

    private void registerCommandsAndEvents() {
        getCommand("roulette").setExecutor(new RouletteCommand(tracker, manager));
        getServer().getPluginManager().registerEvents(new RouletteInvClick(tracker, spinLogic), this);
        getServer().getPluginManager().registerEvents(new EnteringCustomAmount(this, tracker, manager), this);
        getServer().getPluginManager().registerEvents(new CloseRouletteInv(this, tracker), this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

}
