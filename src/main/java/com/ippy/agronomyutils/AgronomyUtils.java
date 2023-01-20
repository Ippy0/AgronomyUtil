package com.ippy.agronomyutils;

import com.ippy.agronomyutils.commands.NightVision;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AgronomyUtils extends JavaPlugin {
    AutoPickup autopickup;

    @Override
    public void onEnable() {
        autopickup = new AutoPickup(this);
        getCommand("setbucket").setExecutor(new InfiBucket(this));
        Bukkit.getPluginManager().registerEvents(new InfiBucket(this),this);
        getCommand("autopickup").setExecutor(autopickup);
        Bukkit.getPluginManager().registerEvents(autopickup,this);
        getCommand("nightvision").setExecutor(new NightVision());

    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
