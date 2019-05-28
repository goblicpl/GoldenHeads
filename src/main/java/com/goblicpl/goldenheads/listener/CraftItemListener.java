package com.goblicpl.goldenheads.listener;

import com.goblicpl.goldenheads.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class CraftItemListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void craftItem(PrepareItemCraftEvent e) {
        Material itemType = e.getRecipe().getResult().getType();
        Byte itemData = e.getRecipe().getResult().getData().getData();

        if(itemType.equals(Material.GOLDEN_APPLE) && itemData == 1) {
            e.getInventory().setResult(new ItemStack(Material.AIR));
            for(HumanEntity he:e.getViewers()) {
                if(he instanceof Player) {
                    ((Player)he).sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigHandler.getInstance().getConfiguration().getString("messages.god-apple-crafting-disabled")));
                }
            }
        }
    }
}
