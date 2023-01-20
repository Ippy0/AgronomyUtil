package com.ippy.agronomyutils;


import jdk.javadoc.internal.tool.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AutoPickup implements CommandExecutor, Listener {

    public List<UUID> autoPickup = new ArrayList<>();

    AgronomyUtils main;
    public AutoPickup(AgronomyUtils main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player)sender;

            if (command.getName().equalsIgnoreCase("autopickup")) {

                if (autoPickup.contains(p.getUniqueId())) {
                    autoPickup.remove(p.getUniqueId());
                    p.sendMessage(CC.translate("&7&l[&c&lOFF&7&l] &f&lAuto Pickup"));

                } else {
                    autoPickup.add(p.getUniqueId());
                    p.sendMessage(CC.translate("&7&l[&a&lON&7&l] &f&lAuto Pickup"));
                }


            }
        }
        return false;
    }

    @EventHandler
    public void OnBlockBreak(BlockDropItemEvent e) {
        if (autoPickup.contains(e.getPlayer().getUniqueId())) {
            List<Item> dropped = e.getItems();
            Player p = e.getPlayer();
            for (int i = 0; i < dropped.size(); i++) {
                dropped.get(i).setPickupDelay(0);
                dropped.get(i).teleport(p.getLocation());

            }

        }
    }
}
