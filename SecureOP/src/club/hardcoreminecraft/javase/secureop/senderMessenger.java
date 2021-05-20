package club.hardcoreminecraft.javase.secureop;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class senderMessenger {


	private Plugin plugin;
	private CommandSender sender;
	
	public senderMessenger(Plugin plugin, CommandSender sender) {
		this.plugin = plugin;
		this.sender = sender;
	}
	  
	
	/**
	 * Method passes successful command messages to all staff on the server
	 * @param CommandSender - %sender%, String - %targeted_player%, String %command%
	 * */	
	  public void messageStaffPass(String target, String cmd) {
		  //We will call this method regardless if we are supposed to message staff. We check in the function
		 if (plugin.getConfig().getBoolean("MessageAdmins"))
			 //For all online players, get the ones with staff perms and message them
	      for (Player p : Bukkit.getOnlinePlayers()) {
	          if (p.hasPermission("Secureop.receive"))
	            p.sendMessage(plugin.getConfig().getString("AdminMessageBadPass").replaceAll("&", "§")
	                .replaceAll("%sender%", sender.getName()).replaceAll("%player%", target).replaceAll("%command%", cmd)); 
	        }  
	  }
	  
	  
	  /**
	   * Method passes failed command messages to all staff on the server
	   * @param CommandSender - %sender%, String %command%
	   * */	
	  public void messageStaffPerm(String cmd) {
		  //We will call this method regardless if we are supposed to message staff. We check in the function
		 if (plugin.getConfig().getBoolean("MessageAdmins"))
			 //For all online players, get the ones with staff perms and message them
	      for (Player p : Bukkit.getOnlinePlayers()) {
	          if (p.hasPermission("Secureop.receive"))
	            p.sendMessage(plugin.getConfig().getString("AdminMessageNoPerm").replaceAll("&", "§")
	                .replaceAll("%sender%", sender.getName()).replaceAll("%command%", cmd)); 
	        }  
	  }
	  
	  
	  /**
	   * Method kicks sender from the server
	   * @param CommandSender - %sender%
	   * */	
	  public boolean kickPlayer() {
	      if (plugin.getConfig().getBoolean("KickWithoutPerms")) {
	          if (sender instanceof Player) {
	            ((Player)sender).kickPlayer(plugin.getConfig().getString("KickMessage").replaceAll("&", "§"));
	            return true;
	          } 
	        }
		  
		return false;
	  }
	  
	  
	  /**
	   * Method sends no permission message to player
	   * @param CommandSender - %sender%, String %command%
	   * */	
	  public void noPerms(String cmd) {
		  sender.sendMessage(plugin.getConfig().getString("ErrorNoPerms").replaceAll("&", "§")
		  .replaceAll("%sender%", sender.getName()).replaceAll("%command%", cmd));
	  }
	  
		
		/**
		 * Method sends bad password message
		 * @param CommandSender - %sender%, String - %targeted_player%, String %command%
		 * */	
	  public void badPassword(String target, String cmd) {
		  sender.sendMessage(plugin.getConfig().getString("BadPassword").replaceAll("&", "§"));
	  	messageStaffPass(target,cmd);
	  }
	
}
