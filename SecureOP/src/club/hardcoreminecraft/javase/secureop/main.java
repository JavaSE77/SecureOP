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
	pluginManager.registerEvents(new playerCommandListener (), this);
	
	//register commands
	this.getCommand("op").setExecutor(this);
	
	//setup the config
    getConfig().options().copyDefaults(true);
    saveDefaultConfig();
    getServer().getLogger().info("This server is being protected by secureOP plugin. Download updates at: https://www.spigotmc.org/resources/secure-op.12953/");
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	  //make sure that we are being passed the op command
    if (cmd.getName().equalsIgnoreCase("op")) {
    	//Call the op command handler. Broken into functions to make this easier to read.
    	opcmd.handleOpCMD(sender, cmd, label, args);
  } else 
	  if(cmd.getName().equalsIgnoreCase("deop")) {
	  //Call the deop function. Copy pasta from the op function, just with the deop function instead
		  deopcmd.handleDeopCMD(sender, cmd, label, args);
  }
    return false;
  }
  

  
  
  public static void messageStaffPass(CommandSender sender, String target, String cmd) {
	  //We will call this method regardless if we are supposed to message staff. We check in the function
	 if (plugin.getConfig().getBoolean("MessageAdmins"))
		 //For all online players, get the ones with staff perms and message them
      for (Player p : Bukkit.getOnlinePlayers()) {
          if (p.hasPermission("Secureop.receive"))
            p.sendMessage(plugin.getConfig().getString("AdminMessageBadPass").replaceAll("&", "§")
                .replaceAll("%sender%", sender.getName()).replaceAll("%player%", target).replaceAll("%command%", cmd)); 
        }  
  }
  
  public static void messageStaffPerm(CommandSender sender, String target, String cmd) {
	  //We will call this method regardless if we are supposed to message staff. We check in the function
	 if (plugin.getConfig().getBoolean("MessageAdmins"))
		 //For all online players, get the ones with staff perms and message them
      for (Player p : Bukkit.getOnlinePlayers()) {
          if (p.hasPermission("Secureop.receive"))
            p.sendMessage(plugin.getConfig().getString("AdminMessageNoPerm").replaceAll("&", "§")
                .replaceAll("%sender%", sender.getName()).replaceAll("%player%", target).replaceAll("%command%", cmd)); 
        }  
  }
  
  public static boolean kickPlayer(CommandSender sender) {
      if (plugin.getConfig().getBoolean("KickWithoutPerms")) {
          if (sender instanceof Player) {
            ((Player)sender).kickPlayer(plugin.getConfig().getString("KickMessage").replaceAll("&", "§"));
            return true;
          } 
        }
	  
	return false;
  }
  
  public static void noPerms(CommandSender sender, String target, String cmd) {
	  sender.sendMessage(plugin.getConfig().getString("ErrorNoPerms").replaceAll("&", "§")
	  .replaceAll("%sender%", sender.getName()).replaceAll("%player%", target).replaceAll("%command%", cmd));
  }
  
  
  public static void badPassword(CommandSender sender, String target, String cmd) {
	  sender.sendMessage(plugin.getConfig().getString("BadPassword").replaceAll("&", "§"));
  	messageStaffPass(sender, target,cmd);
  }
  
  
}
