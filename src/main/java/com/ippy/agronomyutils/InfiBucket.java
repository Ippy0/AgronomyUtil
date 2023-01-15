package com.ippy.agronomyutils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;


public class InfiBucket implements Listener, CommandExecutor {

    AgronomyUtils main;
    NamespacedKey key;
    public InfiBucket(AgronomyUtils main) {

        this.main = main;
        key = new NamespacedKey(main,"uses");
        loresList.add("");
    }


    List<String> loresList = new ArrayList<String>();




    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;
            ItemStack itemInHand = p.getItemInHand();

            if(itemInHand.getType() == Material.WATER_BUCKET){

                ItemMeta itemMeta = itemInHand.getItemMeta();
                itemMeta.getPersistentDataContainer().set(key,PersistentDataType.INTEGER,Integer.parseInt(args[0]));
                loresList.set(0,itemMeta.getPersistentDataContainer().get(key,PersistentDataType.INTEGER).toString());
                itemMeta.setLore(loresList);


                itemInHand.setItemMeta(itemMeta);



            }
        }
        return false;
    }

    @EventHandler
    public void onWaterUse(PlayerBucketEmptyEvent e){

        Player p = e.getPlayer();

        ItemStack itemInHand = p.getItemInHand();
        p.sendMessage(itemInHand.getType().toString());
        ItemMeta itemMeta = itemInHand.getItemMeta();
        if (!itemInHand.hasItemMeta()) return;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if(!container.has(key,PersistentDataType.INTEGER))return;

        int uses = container.get(key,PersistentDataType.INTEGER);
        p.sendMessage(Integer.toString(uses));
        if(uses>0) {
            uses--;
            if (itemInHand.getType() == Material.WATER_BUCKET) {

                Bukkit.getScheduler().runTaskLater(main, () -> {

                    p.getItemInHand().setType(Material.WATER_BUCKET);

                }, 1);
                itemInHand = p.getItemInHand();
                p.sendMessage(itemInHand.getType().toString());
                p.sendMessage(Integer.toString(uses));
                itemMeta=itemInHand.getItemMeta();
                itemMeta.getPersistentDataContainer().set(key,PersistentDataType.INTEGER,uses);
                loresList.set(0,Integer.toString(uses));
                itemMeta.setLore(loresList);
                itemInHand.setItemMeta(itemMeta);
            }
            if (itemInHand.getType() == Material.LAVA_BUCKET) {

                Bukkit.getScheduler().runTaskLater(main, () -> {

                    p.getItemInHand().setType(Material.LAVA_BUCKET);

                }, 1);
            }
        }


    }


}
