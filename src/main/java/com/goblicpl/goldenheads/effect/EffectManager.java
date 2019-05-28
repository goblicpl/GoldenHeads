package com.goblicpl.goldenheads.effect;

import com.goblicpl.goldenheads.Main;
import com.goblicpl.goldenheads.data.ConfigHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class EffectManager {

    private List<PotionEffect> effectList = new ArrayList<>();

    public EffectManager() {
        for(String effects : ConfigHandler.getInstance().getConfiguration().getStringList("effects")) {
            PotionEffectType effectType = PotionEffectType.getByName(effects.split(",")[0]);
            int duration = 20*(Integer.valueOf(effects.split(",")[1]));
            int level = Integer.valueOf(effects.split(",")[2]);

            getEffectList().add(new PotionEffect(effectType, duration, level));
        }
    }

    public List<PotionEffect> getEffectList() {
        return effectList;
    }

    public static EffectManager getInstance() {
        return Main.getInstance().getEffectManager();
    }
}
