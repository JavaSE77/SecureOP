package club.hardcoreminecraft.javase.secureop;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class playerCommandListener implements Listener {

	  //Command blocks are not allowed to use /op, so we only have to worry about players and console using :op.
	  //console :op will be ignored.
	  @EventHandler
	  public void Commands(PlayerCommandPreprocessEvent Event) {
		  //check if the incoming command contains /:op like in bukkit:op or minecraft:op
	    if ((Event.getMessage().toLowerCase().contains(":op")) && 
	      main.plugin.getConfig().getBoolean("BlockBukkit")) {
	      Event.setCancelled(true);
	      if (main.plugin.getConfig().getBoolean("BanOnBadPassword")) {
	    	 //We will treat any attempt to bypass the password as a bad password. Check to see if player should be banned.
	        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "ban " + Event.getPlayer().getName() + " " + main.plugin.getConfig().getString("BanMessage"));
	      } else {
	    	  //Else we should send them the bad password command
	        Event.getPlayer().sendMessage(main.plugin.getConfig().getString("ErrorNoPerms").replaceAll("&", "§"));
	      } 
	    } else 
	    	if ((Event.getMessage().toLowerCase().contains(":deop")) && 
	  	      main.plugin.getConfig().getBoolean("BlockBukkit")) {
		      Event.setCancelled(true);
		      if (main.plugin.getConfig().getBoolean("BanOnBadPassword")) {
		    	 //We will treat any attempt to bypass the password as a bad password. Check to see if player should be banned.
		        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "ban " + Event.getPlayer().getName() + " " + main.plugin.getConfig().getString("BanMessage"));
		      } else {
		    	  //Else we should send them the bad password command
		        Event.getPlayer().sendMessage(main.plugin.getConfig().getString("ErrorNoPerms").replaceAll("&", "§"));
		      } 
		    } 
	    
	    //Check if interceptOPcmd is true. See if we need to intercept 
	    //This should always be true. Setting this to false is a security vulnerability
	    if(main.plugin.getConfig().getBoolean("interceptOPcmd")) {
	    	

    		String[] message = Event.getMessage().split(" ");
	    	String prefix = message[0];
	    	String[] args = Arrays.copyOfRange(message, 1, message.length);
	    	if(prefix.equalsIgnoreCase("/deop")) {
	    		commandHandler cmdHandler = new deopcmd(main.plugin, Event.getPlayer(), prefix, args);
	    		cmdHandler.handleCommand();
	    		Event.setCancelled(true);
	    		
	    	} else 
	    	if(prefix.equalsIgnoreCase("/op")) {
	    		commandHandler cmdHandler = new opcmd(main.plugin, Event.getPlayer(), prefix, args);
	    		cmdHandler.handleCommand();
	    		Event.setCancelled(true);
	    		
	    	}
	    	
	    }
	    
	  }
	
}
