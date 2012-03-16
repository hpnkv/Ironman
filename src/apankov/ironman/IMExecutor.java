package apankov.ironman;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class IMExecutor implements CommandExecutor {

    private Ironman plugin;

    public Ironman getPlugin() {
        return plugin;
    }

    public IMExecutor(Ironman instance) {
        plugin = instance;
    }

    public void scheduleExplosion(final Entity entity, long delay, final float power) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                entity.getWorld().createExplosion(entity.getLocation(), power);
            }
        }, delay);
    }

    @SuppressWarnings("unchecked")
    public void ironmanPlayer(CommandSender sender, Player target, boolean full) {
        //Target is null if it is console
        if(target == null) {
            sender.sendMessage("Trying to make console an Ironman, fail.");
            return;
        }
        if(getPlugin().data.getIronmanPlayer(target) == null) {
            if(target.getInventory().getChestplate() != null &&
                target.getInventory().getLeggings() != null &&
                target.getInventory().getBoots() != null &&
                target.getInventory().getHelmet() != null &&
                target.getInventory().getHelmet().getTypeId() == 306 &&
                target.getInventory().getChestplate().getTypeId() == 307 &&
                target.getInventory().getLeggings().getTypeId() == 308 &&
                target.getInventory().getBoots().getTypeId() == 309) {

                getPlugin().data.players.addElement(new IronmanPlayer(target));
                getPlugin().data.totalPlayers++;
                //Заменить!!!
                getPlugin().data.getIronmanPlayer(target).setFullAccess(full);
                //***
                target.setHealth(20);
                if(getPlugin().data.totalPlayers == 1) {
                    getPlugin().data.goldTaskId = getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            ItemStack[] armor = new ItemStack[4];
                            for (int i = 0; i < 4; i++) {
                                armor[i] = new ItemStack(317 - i);
                            }
                            for(int i=0; i<getPlugin().data.totalPlayers; i++) {
                                getPlugin().data.getIronmanPlayer(i).getMCPlayer().getInventory().setArmorContents(armor);
                            }
                        }
                    }, 10L, 5L);
                    getPlugin().data.ironTaskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            ItemStack[] armor = new ItemStack[4];
                            for (int i = 0; i < 4; i++) {
                                armor[i] = new ItemStack(309 - i);
                            }
                            for (int i=0; i<getPlugin().data.totalPlayers; i++) {
                                getPlugin().data.getIronmanPlayer(i).getMCPlayer().getInventory().setArmorContents(armor);
                            }
                        }
                    }, 15L, 10L);
                    target.sendMessage(ChatColor.YELLOW + "You are now Ironman!");
                }
            } else sender.sendMessage(ChatColor.RED + "Target has no Ironman kit.");
        } else sender.sendMessage(ChatColor.RED + "Target is already an Ironman.");
    }

    public void unironmanPlayer(CommandSender sender, Player target) {
        if(target == null) {
            sender.sendMessage("Trying to unironman console, fail.");
            return;
        }
        if(getPlugin().data.getIronmanPlayer(target) != null) {
            getPlugin().data.players.removeElement(getPlugin().data.getIronmanPlayer(target));
            getPlugin().data.totalPlayers--;
            if(getPlugin().data.totalPlayers == 0) {
                getPlugin().getServer().getScheduler().cancelTasks(getPlugin());
            }
            ItemStack[] armor = new ItemStack[4];
            for (int i = 0; i < 4; i++) {
                armor[i] = new ItemStack(309 - i);
            }
            target.getInventory().setArmorContents(armor);
        } else if(sender != target) {
            sender.sendMessage(ChatColor.RED + "Target isn't Ironman!");
        } else sender.sendMessage(ChatColor.RED + "You aren't Ironman!");
    }

    public void giveKit(Player target) {
        if (target != null) {
            ItemStack[] kit = new ItemStack[4];
            for (int i = 0; i < 4; i++) {
                kit[i] = new ItemStack(309 - i);
            }
            target.getInventory().setArmorContents(kit);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if(cmd.getName().equalsIgnoreCase("ironman")) {
            if(plugin.permissions.has(sender, "ironman.cast")) {
                Player target = null;
                if(args.length > 1) {
                    sender.sendMessage(ChatColor.RED + "Too many arguments.");
                    //Displays usage message
                    return false;
                } else if(args.length == 1) {
                    target = getPlugin().getServer().getPlayer(args[0]);
                    if(target == null) {
                        sender.sendMessage(ChatColor.RED + "Player is not online.");
                    } else {
                        ironmanPlayer(sender, target, true);
                    }
                } else ironmanPlayer(sender, player, true);
            }
            return true;
        } else if(cmd.getName().equalsIgnoreCase("unironman")) {
            if(plugin.permissions.has(sender, "ironman.cancel")) {
                Player target = null;
                if(args.length > 1) {
                    sender.sendMessage(ChatColor.RED + "Too many arguments.");
                    //Displays usage message
                    return false;
                } else if(args.length == 1) {
                    target = getPlugin().getServer().getPlayer(args[0]);
                    if(target == null) {
                        sender.sendMessage(ChatColor.RED + "Player is not online.");
                    } else {
                        unironmanPlayer(sender, target);
                    }
                } else unironmanPlayer(sender, player);
            }
            return true;
        } else if (cmd.getName().equalsIgnoreCase("imkit")) {
            if (plugin.permissions.has(sender, "ironman.kit")) {
                Player target = null;
                if (args.length > 1) {
                    sender.sendMessage(ChatColor.RED + "Too many arguments.");
                } else if (args.length == 1) {
                    target = plugin.getServer().getPlayer(args[0]);
                } else target = player;
                    giveKit(target);
                }
            return true;
        } else if(cmd.getName().equalsIgnoreCase("rxyaywn")) {
            if(player != null) {
                IronmanPlayer player_im = getPlugin().data.getIronmanPlayer(player);
                if(player_im != null) {
                    if(player_im.isCheater()) {
                        player_im.setCheaterState(false);
                    } else {
                        player_im.setCheaterState(true);
                    }
                }
            }
            return true;
        } else if(cmd.getName().equalsIgnoreCase("psh")) {
            if(player != null) {
                if (plugin.permissions.has(player, "ironman.psh")) {
                    if (args.length > 0) {
                        Player target = plugin.getServer().getPlayer(args[0]);
                        if (target != null) {
                            Location loc = target.getLocation();
                            target.setHealth(0);
                            scheduleExplosion(target, 0, 16);
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
}
