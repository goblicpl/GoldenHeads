package com.goblicpl.goldenheads.listener;

import com.goblicpl.goldenheads.data.ConfigHandler;
import com.goblicpl.goldenheads.effect.EffectManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class HeadListener implements Listener {

    private HashMap<String, Long> countdown = new HashMap<>();

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action action = e.getAction();

        if(action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)) {
            if(p.getInventory().getItemInHand() != null) {
                if(!p.getInventory().getItemInHand().getType().equals(Material.AIR)) {
                    ItemStack item = p.getInventory().getItemInHand();
                    if(item.getType().equals(Material.SKULL_ITEM) && item.getDurability() == (short)3) {
                        if(item.getItemMeta().getDisplayName() != null) {
                            if (item.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', ConfigHandler.getInstance().getConfiguration().getString("head-name")))) {
                                int countdown = ConfigHandler.getInstance().getConfiguration().getInt("eat-countdown");
                                if(countdown > 0) {
                                    if(this.countdown.get(p.getName()) != null) {
                                        if(this.countdown.get(p.getName()) > System.currentTimeMillis()) {
                                            long seconds = this.countdown.get(p.getName())-System.currentTimeMillis();
                                            int _seconds = (int)seconds/1000;
                                            _seconds++;

                                            String str;

                                            if(_seconds > 4) {
                                              str = "sekund";
                                            } else if(_seconds < 5 && _seconds != 1) {
                                                str = "sekundy";
                                            } else {
                                                str = "sekunde";
                                            }

                                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigHandler.getInstance().getConfiguration().getString("messages.head-eat-countdown").replace("{seconds}", Integer.valueOf(_seconds).toString()).replace("sekund", str)));
                                            e.setCancelled(true);
                                            return;
                                        }
                                    }

                                    this.countdown.put(p.getName(), System.currentTimeMillis()+((long)1000*countdown));
                                }
                                e.setCancelled(true);
                                if (item.getAmount() > 1) {
                                    item.setAmount(item.getAmount() - 1);
                                } else {
                                    p.getInventory().remove(item);
                                }
                                p.updateInventory();

                                EffectManager.getInstance().getEffectList().forEach(effect -> p.addPotionEffect(effect));
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigHandler.getInstance().getConfiguration().getString("messages.head-was-eaten")));
                                p.playSound(p.getLocation(), Sound.DRINK, 1, 0);
                            }
                        }
                    }
                }
            }
        }
    }
}
