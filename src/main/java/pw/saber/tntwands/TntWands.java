package pw.saber.tntwands;

import org.bukkit.plugin.java.JavaPlugin;
import pw.saber.tntwands.command.CommandTntWands;
import pw.saber.tntwands.listener.InteractionListener;

public class TntWands extends JavaPlugin {

    public static TntWands instance;

    public static TntWands getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        getServer().getPluginManager().registerEvents(new InteractionListener(), this);
        getCommand("tntwand").setExecutor(new CommandTntWands());
    }

    public void onDisable() {
        instance = null;
    }
}
