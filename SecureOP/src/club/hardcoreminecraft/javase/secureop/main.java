package club.hardcoreminecraft.javase.secureop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin {
  JavaPlugin plugin = this;
  
  public void onEnable() {
    getConfig().options().copyDefaults(true);
    saveDefaultConfig();
    getServer().getLogger().info("This server is being protected by secureOP plugin created by http://www.spigotmc.org/members/jeeperscreeper77.42792/Looking for a new hosting company? Use my referral link: https://clients.mcprohosting.com/aff.php?aff=16689");
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("op"))
      if (!(sender instanceof org.bukkit.command.BlockCommandSender)) {
        if (sender.hasPermission("SecureOP.op")) {
          if (args.length == 2)
            if (args[1].equalsIgnoreCase(getConfig().getString("OPpassword"))) {
              Player OPe = Bukkit.getPlayer(args[0]);
              if (OPe != null) {
                String OPEE = OPe.getName();
                Bukkit.getLogger().info("A player has been oped");
                if (this.plugin.getConfig().getBoolean("BroadcastOP"))
                  Bukkit.getServer().broadcastMessage(this.plugin.getConfig().getString("OpMsg").replaceAll("&", "§")
                      .replaceAll("%player%", OPEE).replaceAll("%sender%", sender.getName())); 
                OPe.setOp(true);
              } else if (this.plugin.getConfig().getBoolean("OPofflinePlayers")) {
                String OPEE = args[0];
                Bukkit.getLogger().info("Player " + OPEE + "has been oped");
                if (this.plugin.getConfig().getBoolean("BroadcastOP"))
                  Bukkit.getServer().broadcastMessage(this.plugin.getConfig().getString("OpMsg").replaceAll("&", "§")
                      .replaceAll("%player%", OPEE).replaceAll("%sender%", sender.getName())); 
                
                /* This method is deprecated. This may not work in future versions, but as of spigot 1.5 it is still
                 * an acceptable use of the api, as we cannot get the users uuid from a command line effectively. 
                 * Just be aware that if a user changes their name between the time you op them and when they rejoin, it could bork.
                 * */
               
                Bukkit.getServer().getOfflinePlayer(OPEE).setOp(true);        
              } else {
                sender.sendMessage("Player offline!");
              } 
            } else {
              if (getConfig().getBoolean("BanOnBadPassword")) {
                if (sender instanceof Player) {
                  Player p = (Player)sender;
                  Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "ban " + p.getName() + " " + 
                  this.plugin.getConfig().getString("BanMessage").replaceAll("&", "§"));
                } else {
                  sender.sendMessage(ChatColor.RED + "Error, bad password!");
                } 
              } else {
                sender.sendMessage(ChatColor.RED + "Error, bad password!");
                if (this.plugin.getConfig().getBoolean("MessageAdmins"))
                  for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.hasPermission("Secureop.receive"))
                      p.sendMessage(this.plugin.getConfig().getString("AdminMessageBadPass").replaceAll("&", "§")
                          .replaceAll("%sender%", sender.getName().replaceAll("%player%", args[0]))); 
                  }  
              } 
              if (this.plugin.getConfig().getString("BadCommand") != null)
                Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), this.plugin.getConfig().getString("BadCommand")); 
            }  
          sender.sendMessage(ChatColor.RED + "Error! Please use the command like this: /op (player) (password)");
        } else {
          if (this.plugin.getConfig().getBoolean("MessageAdmins"))
            for (Player p : Bukkit.getOnlinePlayers()) {
              if (p.hasPermission("Secureop.receive"))
                p.sendMessage(this.plugin.getConfig().getString("AdminMessageNoPerm").replaceAll("&", "§")
                    .replaceAll("%sender%", sender.getName())); 
            }  
          if (getConfig().getBoolean("KickWithoutPerms")) {
            if (sender instanceof Player) {
              ((Player)sender).kickPlayer(this.plugin.getConfig().getString("KickMessage").replaceAll("&", "§"));
            } else {
              sender.sendMessage(this.plugin.getConfig().getString("ErrorNoPerms").replaceAll("&", "§"));
            } 
          } else {
            sender.sendMessage(this.plugin.getConfig().getString("ErrorNoPerms").replaceAll("&", "§"));
          } 
          return false;
        } 
      } else {
        sender.sendMessage(this.plugin.getConfig().getString("ErrorNoPerms").replaceAll("&", "§"));
      }  
    return false;
  }
  
  @EventHandler
  public void Commands(PlayerCommandPreprocessEvent Event) {
    if ((Event.getMessage().toLowerCase().contains(":op")) && 
      this.plugin.getConfig().getBoolean("BlockBukkit")) {
      Event.setCancelled(true);
      if (getConfig().getBoolean("BanOnBadPassword")) {
    	 
        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "ban " + Event.getPlayer().getName() + this.plugin.getConfig().getString("BanMessage"));
      } else {
        Event.getPlayer().sendMessage(this.plugin.getConfig().getString("ErrorNoPerms").replaceAll("&", "§"));
      } 
    } 
  }
}
