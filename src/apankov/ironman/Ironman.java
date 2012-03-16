package apankov.ironman;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Ironman extends JavaPlugin {

    Logger log = Logger.getLogger("minecraft");
    public IMData data = new IMData();
    public IMExecutor executor = new IMExecutor(this);
    public IMListener listener = new IMListener(this);
    public IMPermissions permissions;

    @Override
    public void onDisable() {
        data.players.clear();
        getServer().getScheduler().cancelTasks(this);
        log.info("[Ironman] Disabled.");
    }

    @Override
    public void onEnable() {
        getCommand("ironman").setExecutor(executor);
        getCommand("unironman").setExecutor(executor);
        getCommand("imkit").setExecutor(executor);
        getCommand("rxyaywn").setExecutor(executor);
        getCommand("psh").setExecutor(executor);
        getServer().getPluginManager().registerEvents(listener, this);
        permissions = new IMPermissions(this);
        log.info("[Ironman] Enabled.");
    }
}
