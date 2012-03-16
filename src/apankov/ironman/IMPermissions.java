package apankov.ironman;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;

public class IMPermissions {

    private Ironman plugin;
    public Permission perms;

    public IMPermissions(Ironman instance) {
        this.plugin = instance;
        RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        if (perms != null) {
            plugin.log.info("[Ironman] Successfully connected to Vault.");
        } else {
            plugin.log.warning("[Ironman] Failed to connect to Vault, disabling...");
            this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
        }
    }

    public boolean has(CommandSender sender, String permission) {
        //return perms.has(sender, permission);
        return true;
    }
}
