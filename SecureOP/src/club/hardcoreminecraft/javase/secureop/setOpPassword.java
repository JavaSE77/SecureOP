package club.hardcoreminecraft.javase.secureop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class setOpPassword implements commandHandler{

	
	private Plugin plugin;
	private CommandSender sender;
	private senderMessenger messenger;
	private String cmd;
	private String[] args;
	
	public setOpPassword (Plugin plugin, CommandSender sender, Command cmd, String[] args) {
		this.plugin = plugin;
		this.sender = sender;
		this.cmd = cmd.getName();
		this.args = args;
		this.messenger = new senderMessenger(plugin, sender);
	}
	
	public setOpPassword (Plugin plugin, Player sender, String cmd, String[] args) {
		this.plugin = plugin;
		this.sender = sender;
		this.cmd = cmd;
		this.args = args;
		this.messenger = new senderMessenger(plugin, sender);
	}
	
	
	@Override
	public void handleCommand() {
		//Log a message to the console just because it's good practise
		plugin.getLogger().info(sender.getName() + " issued the setOpPassword command. This command has been filtered from server logs to prevent passwords being shown in plain text.");
		
	  	//block command blocks from using our command. Command blocks should never store passwords! Using this with
	  	//a command block is a horrible idea
	    if (sender instanceof org.bukkit.command.BlockCommandSender) {
	    	 messenger.noPerms(cmd);
	    	 return;
	    }
	    //check if the sender does not have permission. 
	    if (!(sender.hasPermission("SecureOP.setPassword"))) {
	      	//messages all staff with the permission node
	      	messenger.messageStaffPerm(cmd);  
	        
	      	//if true, player has been kicked. Else send a message
	        if (!messenger.kickPlayer()) {
	      	  messenger.noPerms(cmd);
	        }
	        return;
	    }
	    //check if command usage is correct
      	if(args.length != 2) {
	          //if the usage is incorrect, send message about how to use
	        sender.sendMessage(ChatColor.RED + "Error! Please use the command like this: /setOpPassword (current password) (new password)");
	        return;
      	}
		
      	//now we check the password
      	passwordHandler handler = new passwordHandler(args[0], plugin);
      	if(handler.verifyPassword()) {
      		String newHash = handler.securePassword(args[1]);
      		handler.setPassword(newHash);
      		sender.sendMessage(ChatColor.BLUE + "You successfuly set the OP password.");
      	} else {
      		//password failed
      		handleBadPass();
      	}
      	
	} 
	
	
	private void handleBadPass() {
        if (main.plugin.getConfig().getBoolean("BanOnBadPassword")) {
            if (sender instanceof Player) {
            	//get the sender and ban them.
              Player p = (Player)sender;
              //We use the /ban command to ban the player just in case you have a custom ban plugin. Your custom
              //ban plugin should have an alias of /ban. If it does not, you can make one
              Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "ban " + p.getName() + " " + 
              plugin.getConfig().getString("BanMessage").replaceAll("&", "§"));
              messenger.messageStaffPass(args[0], cmd);
            } else {
            	//If the sender is not a player, we cannot ban them, so just give them the error message
          	  messenger.badPassword(args[0], cmd);
            } 
          } else {
        	  //Send the player bad password error, and message staff
          	messenger.badPassword( args[0], cmd);
          } 
          //This is the command that gets run when a player fails the op command. 
          //Check and make sure the config setting is enabled by doing a null check
          if (main.plugin.getConfig().getString("BadCommand") != null) {
            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), main.plugin.getConfig().getString("BadCommand")); 
          }
	}

}
