package club.hardcoreminecraft.javase.secureop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class opcmd implements commandHandler {
	
	private Plugin plugin;
	private CommandSender sender;
	private senderMessenger messenger;
	private String cmd;
	private String[] args;
	
	public opcmd (Plugin plugin, CommandSender sender, Command cmd, String[] args) {
		this.plugin = plugin;
		this.sender = sender;
		this.cmd = cmd.getName();
		this.args = args;
		this.messenger = new senderMessenger(plugin, sender);
	}
	
	public opcmd (Plugin plugin, Player sender, String cmd, String[] args) {
		this.plugin = plugin;
		this.sender = sender;
		this.cmd = cmd;
		this.args = args;
		this.messenger = new senderMessenger(plugin, sender);
	}
	

	@Override
	public void handleCommand() {
		//Log a message to the console just because it's good practise
		plugin.getLogger().info(sender.getName() + " issued the op command. This command has been filtered from server logs to prevent passwords being shown in plain text.");
		
	  	//block command blocks from using our command. Command blocks should never store passwords! Using this with
	  	//a command block is a horrible idea
	    if (sender instanceof org.bukkit.command.BlockCommandSender) {
	    	 messenger.noPerms(cmd);
	    	 return;
	    }
	    //check if the sender does not have permission. 
	    if (!(sender.hasPermission("SecureOP.op"))) {
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
	        sender.sendMessage(ChatColor.RED + "Error! Please use the command like this: /op (player) (password)");
	        return;
      	}
      	
      	//now we check the password
      	passwordHandler handler = new passwordHandler(args[1], plugin);
      	if(handler.verifyPassword()) {

          	//set the target player, and confirm that they are not null.
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
            	//Player is online. Op them.
            	opOnlinePlayer(target);
              
            } else if (main.plugin.getConfig().getBoolean("OPofflinePlayers")) {
            	//op an offline player. Warning this could bork
            	opOfflinePlayer();
            } else {
          	  //else inform sender
              sender.sendMessage(ChatColor.DARK_RED + "Player offline! You are not allowed to set the operator status of offline players, " +
            "as specified in the config.yml");
            } 
      		
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
	
	private void opOnlinePlayer(Player target) {
      	
    	  //inform logger
        String targetName = target.getName();
        Bukkit.getLogger().info("Player"+ targetName +" has been oped by " + sender.getName());
        
        //if we are supposed to broadcast op, then do so. This will broadcast to everyone. Unless you want this, you
        //should disable it.
        if (main.plugin.getConfig().getBoolean("BroadcastOP"))
          Bukkit.getServer().broadcastMessage(main.plugin.getConfig().getString("OpMsg").replaceAll("&", "§")
              .replaceAll("%player%", targetName).replaceAll("%sender%", sender.getName())); 
        target.setOp(true);
	}
	
	private void opOfflinePlayer() {
    	  //if we are allowed to op offline players; then get the target
        String offlineTarget = args[0];
        //inform the logger
        Bukkit.getLogger().info("Offline player " + offlineTarget + " has been oped by " + sender.getName());
        if (main.plugin.getConfig().getBoolean("BroadcastOP"))
            //if we are supposed to broadcast op, then do so. This will broadcast to everyone. Unless you want this, you
            //should disable it.
          Bukkit.getServer().broadcastMessage(main.plugin.getConfig().getString("OpMsg").replaceAll("&", "§")
              .replaceAll("%player%", offlineTarget).replaceAll("%sender%", sender.getName())); 
        
        /* This method is deprecated. This may not work in future versions, but as of spigot 1.5 it is still
         * an acceptable use of the api, as we cannot get the users uuid from a command line effectively. 
         * Just be aware that if a user changes their name between the time you op them and when they rejoin, it could bork.
         * */
       
        Bukkit.getServer().getOfflinePlayer(offlineTarget).setOp(true);     
	}
	

}
