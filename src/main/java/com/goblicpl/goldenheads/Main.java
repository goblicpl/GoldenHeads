package com.goblicpl.goldenheads;

import com.goblicpl.goldenheads.data.ConfigHandler;
import com.goblicpl.goldenheads.effect.EffectManager;
import com.goblicpl.goldenheads.listener.CraftItemListener;
import com.goblicpl.goldenheads.listener.HeadListener;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.UUID;

public class Main extends JavaPlugin {

    private static Main instance;

    private ConfigHandler configHandler;
    private EffectManager effectManager;

    public void onEnable() {
        instance = this;

        getLogger().info("Wczytuje plik konfiguracyjny.");
        this.configHandler = new ConfigHandler();

        getLogger().info("Wczytuje efekty podczas jedzenia glowy.");
        this.effectManager = new EffectManager();

        if(ConfigHandler.getInstance().getConfiguration().getBoolean("crafting.head-crafting-enabled")) {
            getLogger().info("Wczytuje crafting glowy.");
            loadRecipe();
        } else {
            getLogger().info("Crafting glowy jest wylaczony.");
        }
        if(ConfigHandler.getInstance().getConfiguration().getBoolean("crafting.disable-god-apple")) {
            getLogger().info("Crafting koxa zostal wylaczony.");
            getServer().getPluginManager().registerEvents(new CraftItemListener(), this);
        }

        getServer().getPluginManager().registerEvents(new HeadListener(), this);
    }

    public void loadRecipe() {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);

        String url = "http://textures.minecraft.net/texture/452dca68c8f8af533fb737faeeacbe717b968767fc18824dc2d37ac789fc77";

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        headMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', ConfigHandler.getInstance().getConfiguration().getString("head-name"))));
        head.setItemMeta(headMeta);

        ShapedRecipe shapedRecipe = new ShapedRecipe(head);

        shapedRecipe.shape("ggg","ghg","ggg");
        shapedRecipe.setIngredient('g', Material.GOLD_BLOCK);

        ItemStack skullItem = new ItemStack(Material.SKULL_ITEM, 1, (short)3);

        shapedRecipe.setIngredient('h', skullItem.getData());

        Bukkit.getServer().addRecipe(shapedRecipe);
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public EffectManager getEffectManager() {
        return effectManager;
    }

    public static Main getInstance() {
        return instance;
    }
}
