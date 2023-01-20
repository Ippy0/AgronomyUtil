package com.ippy.agronomyutils;


import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import java.util.Arrays;
import java.util.UUID;


public class InfiBucket implements Listener, CommandExecutor {

    AgronomyUtils main;
    NamespacedKey key,key2,inf,player;

    public InfiBucket(AgronomyUtils main) {

        this.main = main;
        key = new NamespacedKey(main,"uses");
        key2 = new NamespacedKey(main, "finaluses");
        inf = new NamespacedKey(main,"infinity");
        player = new NamespacedKey(main,"MCUUID");

    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {

            Player p = (Player) sender;
            ItemStack itemInHand = p.getItemInHand();
            ItemMeta itemMeta = itemInHand.getItemMeta();
            PersistentDataContainer container = itemMeta.getPersistentDataContainer();

            if (p.hasPermission("agronomyutils.use")) {

                if (itemInHand.getType() == Material.WATER_BUCKET) {
                    if(args[0].equals("infinity")){
                        container.set(inf,PersistentDataType.INTEGER,1);
                        itemMeta.setLore(Arrays.asList(CC.translate("&#38C50F&lInfinite &f&lUses")));
                        itemInHand.setItemMeta(itemMeta);
                    }
                    else {

                        int uses = Integer.parseInt(args[0]);

                        container.set(key, PersistentDataType.INTEGER, uses);
                        container.set(key2, PersistentDataType.INTEGER, uses);
                        container.set(inf,PersistentDataType.INTEGER,0);

                        String totalUses = Integer.toString(uses);

                        itemMeta.setLore(Arrays.asList(CC.translate("&f&l" + totalUses + "&#38C50F&l/&f&l" + totalUses)));

                        itemInHand.setItemMeta(itemMeta);
                    }

                }
            }
        }
        return false;
    }

    @EventHandler
    public void onWaterUse(PlayerBucketEmptyEvent e){

        Player p = e.getPlayer();

        ItemStack itemInHand = p.getInventory().getItem(e.getHand());
        ItemMeta itemMeta = itemInHand.getItemMeta();

        if (!itemInHand.hasItemMeta()) return;

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (!container.has(key, PersistentDataType.INTEGER)&&!container.has(inf,PersistentDataType.INTEGER)) return;

        if (!container.has(player,PersistentDataType.STRING)) {

            UUID mcuuid = p.getUniqueId();
            ItemStack bucket = p.getInventory().getItem(e.getHand());
            ItemMeta sellRodMeta = bucket.getItemMeta();
            PersistentDataContainer sellRodC = sellRodMeta.getPersistentDataContainer();

            sellRodC.set(player, PersistentDataType.STRING, mcuuid.toString());
            bucket.setItemMeta(sellRodMeta);
            p.setItemInHand(bucket);
            e.setCancelled(true);
        }
        if(container.get(player,PersistentDataType.STRING).equals(p.getUniqueId().toString())) {
            if (container.get(inf, PersistentDataType.INTEGER) == 1) {
                ItemStack bucket = p.getItemInHand();
                e.setItemStack(bucket);

            } else {

                int uses = container.get(key, PersistentDataType.INTEGER);
                int totalUses = container.get(key2, PersistentDataType.INTEGER);

                if (uses > 0) {

                    uses--;

                    Material bucket = e.getBucket();
                    ItemStack bucketIS = new ItemStack(bucket);
                    ItemMeta bucketMeta = bucketIS.getItemMeta();
                    PersistentDataContainer bucketContainer = bucketMeta.getPersistentDataContainer();

                    bucketContainer.set(key, PersistentDataType.INTEGER, uses);
                    bucketContainer.set(key2, PersistentDataType.INTEGER, totalUses);
                    bucketContainer.set(player, PersistentDataType.STRING,p.getUniqueId().toString());
                    bucketMeta.setLore(Arrays.asList(CC.translate("&f&l" + Integer.toString(uses) + "&#38C50F&l/&f&l" + Integer.toString(totalUses))));
                    bucketIS.setItemMeta(bucketMeta);
                    e.setItemStack(bucketIS);
                }
            }
        }
        else p.sendMessage(CC.translate("&c&lThis is not your bucket!"));


    }

}



