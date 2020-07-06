package me.syfe.swashbuckleenchants.customenchants;

import me.syfe.swashbuckleenchants.api.AdvancedItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SoulListener implements Listener
{

    public static boolean isSoulbound(ItemStack item) {
        AdvancedItemStack stack = new AdvancedItemStack(item);
        return stack.getNBTTagInt("Soulbound", 0) == 1 ? true : false;
    }

    List<ItemStack> giveOnRespawn = new ArrayList<ItemStack>();

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        for(int i = 0; i < e.getDrops().size(); i++){
            if(isSoulbound(e.getDrops().get(i))) {
                giveOnRespawn.add(e.getDrops().get(i));
                e.getDrops().remove(i);
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e){
        ItemStack[] stack = giveOnRespawn.toArray(new ItemStack[giveOnRespawn.size()]);
        e.getPlayer().getInventory().addItem(stack);
        e.getPlayer().updateInventory();
        e.getPlayer().closeInventory();
        giveOnRespawn.clear();
    }
}
