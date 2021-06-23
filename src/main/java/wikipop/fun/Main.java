package wikipop.fun;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {
    FileConfiguration config = this.getConfig();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this,this);
        Bukkit.getConsoleSender().sendMessage("Plugin Started");
        getConfig().addDefault("pauseMessage", "&cNie możesz wejść na serwer, trwają prace techniczne!");
        getConfig().addDefault("przerwaState", false);
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    @Override
        public void onDisable() {
            // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(config.getBoolean("przerwaState")){
            if(!e.getPlayer().hasPermission("wikipop.fun.admin")){
                String kickMessage = getConfig().getString("pauseMessage");
                kickMessage = ChatColor.translateAlternateColorCodes('&', kickMessage);
                p.kickPlayer(kickMessage);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;

        if(command.getName().equalsIgnoreCase("przerwa")){
            if(p.hasPermission("wikipop.fun.przerwa")){
                if(args.length == 1){
                    if(args[0].equalsIgnoreCase("on")){
                        config.set("przerwaState", true);
                        saveConfig();
                        for (Player target : Bukkit.getServer().getOnlinePlayers()) {
                            if (!(target.hasPermission("wikipop.fun.admin") || target.isOp())){
                                String kickMessage = getConfig().getString("pauseMessage");
                                kickMessage = ChatColor.translateAlternateColorCodes('&', kickMessage);
                                target.kickPlayer(kickMessage);
                            }
                        }
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aZarządzono przerwe"));
                        return true;
                    }else if(args[0].equalsIgnoreCase("off")){
                        config.set("przerwaState", false);
                        saveConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aZarządzono koniec przerwy"));
                        return true;
                    }else if(args[0].equalsIgnoreCase("status")){
                        if(config.getBoolean("przerwaState") == true){
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aPrzerwa jest włączona"));
                        }else{
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aPrzerwa jest wyłączona"));
                        }
                        return true;
                    }else {
                        return false;
                    }
                }
                return false;
            }else{
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNie masz do to tego uprawnień"));
            }
        }
        return true;
    }
}
