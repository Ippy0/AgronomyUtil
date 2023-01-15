package com.ippy.agronomyutils;

import com.ippy.agronomyutils.commands.NightVision;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AgronomyUtils extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("setbucket").setExecutor(new InfiBucket(this));
        Bukkit.getPluginManager().registerEvents(new InfiBucket(this),this);
        getCommand("nightvision").setExecutor(new NightVision());

    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
