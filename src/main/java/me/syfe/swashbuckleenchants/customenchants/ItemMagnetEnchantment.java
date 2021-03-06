package me.syfe.swashbuckleenchants.customenchants;

import me.syfe.swashbuckleenchants.SwashbuckleEnchants;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class ItemMagnetEnchantment extends Enchantment implements Listener {

    public ItemMagnetEnchantment(String namespace) {
        super(new NamespacedKey(SwashbuckleEnchants.getPlugin(), namespace));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if (!e.isDropItems()) return;

        Player player = (Player) e.getPlayer();

        ItemStack[] stack = e.getBlock().getDrops(player.getInventory().getItemInMainHand()).toArray(new ItemStack[e.getBlock().getDrops(player.getInventory().getItemInMainHand()).size()]);

        if(player.getEquipment().getItemInMainHand().getEnchantments().containsKey(Enchantment.getByKey(SwashbuckleEnchants.itemMagnetEnchantment.getKey()))){
            if(player.getInventory().contains(stack[0]) || isInventoryFull(player) == false){
                player.getInventory().addItem(stack);
            } else {
                player.getWorld().dropItem(player.getLocation().add(0, .5, 0), stack[0]);
            }
            e.setDropItems(false);

            player.giveExp(e.getExpToDrop());
            e.setExpToDrop(0);
        }
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent e){
        if(e.getEntity().getKiller() instanceof Player){
            Player player = (Player) e.getEntity().getKiller();

            Collection<ItemStack> drops = e.getDrops();
            ItemStack[] stack = drops.toArray(new ItemStack[drops.size()]);

            if(player.getEquipment().getItemInMainHand().getEnchantments().containsKey(Enchantment.getByKey(SwashbuckleEnchants.itemMagnetEnchantment.getKey()))){
                if(player.getInventory().contains(stack[0]) || isInventoryFull(player) == false){
                    player.getInventory().addItem(drops.toArray(new ItemStack[0]));
                } else {
                    player.getWorld().dropItem(player.getLocation().add(0, .5, 0), stack[0]);
                }
                e.getDrops().clear();

                player.giveExp(e.getDroppedExp());
                e.setDroppedExp(0);
            }
        }
    }

    public boolean isInventoryFull(Player player)
    {
        return player.getInventory().firstEmpty() == -1;
    }

    @Override
    public String getName() {
        return "Item Magnet";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return true;
    }
}
