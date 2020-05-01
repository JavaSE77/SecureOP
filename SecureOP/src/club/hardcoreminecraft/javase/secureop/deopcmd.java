package club.hardcoreminecraft.javase.secureop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class deopcmd {
	  public static void handleDeopCMD(CommandSender sender, Command cmd, String label, String[] args) {
		  	
		  	//block command blocks from using our command. Command blocks should never store passwords! Using this with
		  	//a command block is a horrible idea
		    if (!(sender instanceof org.bukkit.command.BlockCommandSender)) {
		  	  //check if the sender has permission. 
		      if (sender.hasPermission("SecureOP.deop")) {
		      	if(args.length == 2) {
		  
		      	  //compare arg[1] with the password in the config
		          if (args[1].equalsIgnoreCase(main.plugin.getConfig().getString("OPpassword"))) {
		          
		          	//set the target player, and conform that they are not null.
		            Player target = Bukkit.getPlayer(args[0]);
		            if (target != null) {
		          	
		          	  //inform logger
		              String targetName = target.getName();
		              Bukkit.getLogger().info("Player"+ targetName +" has been oped by " + sender.getName());
		              
		              //if we are supposed to broadcast op, then do so. This will broadcast to everyone. Unless you want this, you
		              //should disable it.
		              if (main.plugin.getConfig().getBoolean("BroadcastOP"))
		                Bukkit.getServer().broadcastMessage(main.plugin.getConfig().getString("DeopMsg").replaceAll("&", "§")
		                    .replaceAll("%player%", targetName).replaceAll("%sender%", sender.getName())); 
		              target.setOp(false);
		              
		            } else if (main.plugin.getConfig().getBoolean("OPofflinePlayers")) {
		          	  //if we are allowed to op offline players; then get the target
		              String offlineTarget = args[0];
		              //inform the logger
		              Bukkit.getLogger().info("Offline player " + offlineTarget + " has been oped by " + sender.getName());
		              if (main.plugin.getConfig().getBoolean("BroadcastOP"))
		                  //if we are supposed to broadcast op, then do so. This will broadcast to everyone. Unless you want this, you
		                  //should disable it.
		                Bukkit.getServer().broadcastMessage(main.plugin.getConfig().getString("DeopMsg").replaceAll("&", "§")
		                    .replaceAll("%player%", offlineTarget).replaceAll("%sender%", sender.getName())); 
		              
		              /* This method is deprecated. This may not work in future versions, but as of spigot 1.5 it is still
		               * an acceptable use of the api, as we cannot get the users uuid from a command line effectively. 
		               * Just be aware that if a user changes their name between the time you op them and when they rejoin, it could bork.
		               * */
		             
		              Bukkit.getServer().getOfflinePlayer(offlineTarget).setOp(false);        
		            } else {
		          	  //else inform sender
		              sender.sendMessage(ChatColor.DARK_RED + "Player offline! You are not allowed to set the operator status of offline players, " +
		            "as specified in the config.yml");
		            } 
		          } else {
		            if (main.plugin.getConfig().getBoolean("BanOnBadPassword")) {
		              if (sender instanceof Player) {
		              	//get the sender and ban them.
		                Player p = (Player)sender;
		                //We use the /ban command to ban the player just in case you have a custom ban plugin. Your custom
		                //ban plugin should have an alias of /ban. If it does not, you can make one
		                Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "ban " + p.getName() + " " + 
		                main.plugin.getConfig().getString("BanMessage").replaceAll("&", "§"));
		                main.messageStaffPass(sender, args[0], cmd.getName());
		              } else {
		              	//If the sender is not a player, we cannot ban them, so just give them the error message
		              	main.badPassword(sender, args[0], cmd.getName());
		              } 
		            } else {
		          	  //Send the player bad password error, and message staff
		          	  main.badPassword(sender, args[0], cmd.getName());
		            } 
		            //This is the command that gets run when a player fails the op command. 
		            //Check and make sure the config setting is enabled by doing a null check
		            if (main.plugin.getConfig().getString("BadCommand") != null)
		              Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), main.plugin.getConfig().getString("BadCommand")); 
		          }  
		      } else {
		          //if the usage is incorrect, send message about how to use
		        sender.sendMessage(ChatColor.RED + "Error! Please use the command like this: /deop (player) (password)");
		      }
		      } else {
		      	//break everything up into functions
		      	//messages all staff with the permission node
		      	main.messageStaffPerm(sender, args[0], cmd.getName());  
		        
		      	//if true, player has been kicked. Else send a message
		        if (!main.kickPlayer(sender))
		      	  main.noPerms(sender, args[0], cmd.getName());
		        
		        return;
		      } 
		    } else main.noPerms(sender, args[0], cmd.getName());
		  }
}
