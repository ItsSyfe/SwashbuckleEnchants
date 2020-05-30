package me.syfe.swashbuckleenchants;

import me.syfe.swashbuckleenchants.customenchants.ItemMagnetEnchantment;
import org.bukkit.*;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public final class SwashbuckleEnchants extends JavaPlugin implements Listener {

    private static SwashbuckleEnchants plugin;
    public static ItemMagnetEnchantment itemMagnetEnchantment;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        itemMagnetEnchantment = new ItemMagnetEnchantment("item_magnet");

        registerEnchantment(itemMagnetEnchantment);

        this.getServer().getPluginManager().registerEvents(itemMagnetEnchantment, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            Field keyField = Enchantment.class.getDeclaredField("byKey");

            keyField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<NamespacedKey, Enchantment> byKey = (HashMap<NamespacedKey, Enchantment>) keyField.get(null);

            if(byKey.containsKey(itemMagnetEnchantment.getKey())) {
                byKey.remove(itemMagnetEnchantment.getKey());
            }
            Field nameField = Enchantment.class.getDeclaredField("byName");

            nameField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) nameField.get(null);

            if(byName.containsKey(itemMagnetEnchantment.getName())) {
                byName.remove(itemMagnetEnchantment.getName());
            }
        } catch (Exception ignored) { }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(command.getName().equals("itmgive")){
            if(sender.hasPermission("SwashbuckleEnchants.itemmagnet")){
                Player player = (Player) sender;
                if (player instanceof Player) {
                    player.getInventory().getItemInMainHand().addEnchantment(SwashbuckleEnchants.itemMagnetEnchantment, 1);
                    ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.LIGHT_PURPLE + "Item Magnet I");
                    meta.setLore(lore);
                    player.getInventory().getItemInMainHand().setItemMeta(meta);
                    sender.sendMessage(ChatColor.GREEN + "[SwashbuckleEnchants] Successfully applied Item Magnet to item in hand!");
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "[SwashbuckleEnchants] You don't have permission to do that!");
                return true;
            }
        }


        return false;
    }

    public static SwashbuckleEnchants getPlugin(){
        return plugin;
    }

    public static void registerEnchantment(Enchantment enchantment) {
        boolean registered = true;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (Exception e) {
            registered = false;
            e.printStackTrace();
        }
        if(registered){
            // It's been registered!
        }
    }

}
