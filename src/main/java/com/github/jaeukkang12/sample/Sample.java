package com.github.jaeukkang12.sample;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Sample extends JavaPlugin {

    private static JavaPlugin plugin;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("afashs-lib") == null) {
            Bukkit.getLogger().warning("[" + this.getName() + "]" + "afashs-lib 플러그인이 감지되지 않았습니다! 플러그인을 종료합니다.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        // INSTANCE
        plugin = this;
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }
}
