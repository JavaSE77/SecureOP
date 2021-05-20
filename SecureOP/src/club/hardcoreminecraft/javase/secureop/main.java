package club.hardcoreminecraft.javase.secureop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
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
	  
      Logger coreLogger = (Logger)LogManager.getRootLogger();
      coreLogger.addFilter(new Log4JFilter());

	  //register new events: 		
	plugin = getPlugin(main.class);
	PluginManager pluginManager = getServer().getPluginManager();
	pluginManager.registerEvents(new playerCommandListener (), this);
	
	//register commands
	this.getCommand("op").setExecutor(this);
	this.getCommand("deop").setExecutor(this);
	this.getCommand("setoppassword").setExecutor(this);
	
	
	//setup the config
    getConfig().options().copyDefaults(true);
    saveDefaultConfig();
    getServer().getLogger().info("This server is being protected by secureOP plugin. Download updates at: https://www.spigotmc.org/resources/secure-op.12953/");
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	  //make sure that we are being passed the op command
    if (cmd.getName().equalsIgnoreCase("op")) {
    	//Call the op command handler. Broken into functions to make this easier to read.
		  commandHandler cmdHandler = new opcmd(plugin, sender, cmd, args);
		  cmdHandler.handleCommand();
  } else 
	  if(cmd.getName().equalsIgnoreCase("deop")) {
	  //Call the deop function. Copy pasta from the op function, just with the deop function instead
		  commandHandler cmdHandler = new deopcmd(plugin, sender, cmd, args);
		  cmdHandler.handleCommand();
  } else 
	  if(cmd.getName().equalsIgnoreCase("setoppassword")) {
	  //Call the password set handler
		  commandHandler cmdHandler = new setOpPassword(plugin, sender, cmd, args);
		  cmdHandler.handleCommand();
  }
    return false;
  }
  


  
  
}
