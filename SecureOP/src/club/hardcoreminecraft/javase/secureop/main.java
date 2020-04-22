package club.hardcoreminecraft.javase.secureop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;



public class main extends JavaPlugin {
	static main plugin;
  
  public void onEnable() {

	  //register new events: 		
	plugin = getPlugin(main.class);
	PluginManager pluginManager = getServer().getPluginManager();
	pluginManager.registerEvents(new playerCommandListener(), this);
	
	//register commands
	this.getCommand("op").setExecutor(this);
	
	//setup the config
    getConfig().options().copyDefaults(true);
    saveDefaultConfig();
    getServer().getLogger().info("This server is being protected by secureOP plugin created by http://www.spigotmc.org/members/jeeperscreeper77.42792/Looking for a new hosting company? Use my referral link: https://clients.mcprohosting.com/aff.php?aff=16689");
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	  //make sure that we are being passed the op command
    if (cmd.getName().equalsIgnoreCase("op"))
    	
    	//block command blocks from using our command. Command blocks should never store passwords! Using this with
    	//a command block is a horrible idea
      if (!(sender instanceof org.bukkit.command.BlockCommandSender)) {
    	  //check if the sender has permission. 
        if (sender.hasPermission("SecureOP.op")) {
        	if(args.length == 2) {
    
        	  //compare arg[1] with the password in the config
            if (args[1].equalsIgnoreCase(getConfig().getString("OPpassword"))) {
            
            	//set the target player, and conform that they are not null.
              Player target = Bukkit.getPlayer(args[0]);
              if (target != null) {
            	
            	  //inform logger
                String targetName = target.getName();
                Bukkit.getLogger().info("Player"+ targetName +" has been oped by " + sender.getName());
                
                //if we are supposed to broadcast op, then do so. This will broadcast to everyone. Unless you want this, you
                //should disable it.
                if (plugin.getConfig().getBoolean("BroadcastOP"))
                  Bukkit.getServer().broadcastMessage(plugin.getConfig().getString("OpMsg").replaceAll("&", "§")
                      .replaceAll("%player%", targetName).replaceAll("%sender%", sender.getName())); 
                target.setOp(true);
                
              } else if (plugin.getConfig().getBoolean("OPofflinePlayers")) {
            	  //if we are allowed to op offline players; then get the target
                String offlineTarget = args[0];
                //inform the logger
                Bukkit.getLogger().info("Offline player " + offlineTarget + " has been oped by " + sender.getName());
                if (plugin.getConfig().getBoolean("BroadcastOP"))
                    //if we are supposed to broadcast op, then do so. This will broadcast to everyone. Unless you want this, you
                    //should disable it.
                  Bukkit.getServer().broadcastMessage(plugin.getConfig().getString("OpMsg").replaceAll("&", "§")
                      .replaceAll("%player%", offlineTarget).replaceAll("%sender%", sender.getName())); 
                
                /* This method is deprecated. This may not work in future versions, but as of spigot 1.5 it is still
                 * an acceptable use of the api, as we cannot get the users uuid from a command line effectively. 
                 * Just be aware that if a user changes their name between the time you op them and when they rejoin, it could bork.
                 * */
               
                Bukkit.getServer().getOfflinePlayer(offlineTarget).setOp(true);        
              } else {
            	  //else inform sender
                sender.sendMessage(ChatColor.DARK_RED + "Player offline! You are not allowed to set the operator status of offline players, " +
              "as specified in the config.yml");
              } 
            } else {
              if (getConfig().getBoolean("BanOnBadPassword")) {
                if (sender instanceof Player) {
                	//get the sender and ban them.
                  Player p = (Player)sender;
                  //We use the /ban command to ban the player just in case you have a custom ban plugin. Your custom
                  //ban plugin should have an alias of /ban. If it does not, you can make one
                  Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "ban " + p.getName() + " " + 
                  plugin.getConfig().getString("BanMessage").replaceAll("&", "§"));
                  messageStaff(sender, args[0]);
                } else {
                	//If the sender is not a player, we cannot ban them, so just give them the error message
                  sender.sendMessage(plugin.getConfig().getString("BadPassword").replaceAll("&", "§"));
                  messageStaff(sender, args[0]);
                } 
              } else {
            	  //Send the player bad password error, and message staff
            	  sender.sendMessage(plugin.getConfig().getString("BadPassword").replaceAll("&", "§"));
                if (plugin.getConfig().getBoolean("MessageAdmins"))
                	messageStaff(sender, args[0]);
              } 
              //This is the command that gets run when a player fails the op command. 
              //Check and make sure the config setting is enabled by doing a null check
              if (plugin.getConfig().getString("BadCommand") != null)
                Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), plugin.getConfig().getString("BadCommand")); 
            }  
        }
            //if the usage is incorrect, send message about how to use
          sender.sendMessage(ChatColor.RED + "Error! Please use the command like this: /op (player) (password)");
        } else {
        	//Check the config and see if we need to message admins on no perms. 
          if (plugin.getConfig().getBoolean("MessageAdmins"))
            for (Player p : Bukkit.getOnlinePlayers()) {
              if (p.hasPermission("Secureop.receive"))
                p.sendMessage(plugin.getConfig().getString("AdminMessageNoPerm").replaceAll("&", "§")
                    .replaceAll("%sender%", sender.getName())); 
            }  
          
          if (getConfig().getBoolean("KickWithoutPerms")) {
            if (sender instanceof Player) {
              ((Player)sender).kickPlayer(plugin.getConfig().getString("KickMessage").replaceAll("&", "§"));
            } else {
              sender.sendMessage(plugin.getConfig().getString("ErrorNoPerms").replaceAll("&", "§"));
            } 
          } else {
            sender.sendMessage(plugin.getConfig().getString("ErrorNoPerms").replaceAll("&", "§"));
          } 
          return false;
        } 
      } else {
        sender.sendMessage(plugin.getConfig().getString("ErrorNoPerms").replaceAll("&", "§"));
      }  
    return false;
  }
  

  
  
  public void messageStaff(CommandSender sender, String target) {
	  //We will call this method regardless if we are supposed to message staff. We check in the function
	 if (plugin.getConfig().getBoolean("MessageAdmins"))
		 //For all online players, get the ones with staff perms and message them
      for (Player p : Bukkit.getOnlinePlayers()) {
          if (p.hasPermission("Secureop.receive"))
            p.sendMessage(plugin.getConfig().getString("AdminMessageBadPass").replaceAll("&", "§")
                .replaceAll("%sender%", sender.getName()).replaceAll("%player%", target)); 
        }  
  }
  
}
