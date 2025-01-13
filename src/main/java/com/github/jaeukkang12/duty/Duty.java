package com.github.jaeukkang12.duty;

import com.github.jaeukkang12.duty.listener.PlayerJoinListener;
import com.github.jaeukkang12.lib.config.Config;
import com.github.jaeukkang12.lib.util.NumberUtil;
import com.github.jaeukkang12.money.api.EconomyAPI;
import com.github.jaeukkang12.money.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public final class Duty extends JavaPlugin {

    private static JavaPlugin plugin;

    // MESSAGE DATA
    public static Config messageData;

    // CONFIG
    public static Config config;

    // ECONOMY
    private static Economy economy;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("afashs-lib") == null) {
            Bukkit.getLogger().warning("[" + this.getName() + "]" + "afashs-lib 플러그인이 감지되지 않았습니다! 플러그인을 종료합니다.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        if (Bukkit.getPluginManager().getPlugin("afashs-money") == null) {
            Bukkit.getLogger().warning("[" + this.getName() + "]" + "afashs-money 플러그인이 감지되지 않았습니다! 플러그인을 종료합니다.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        // INSTANCE
        plugin = this;

        // CONFIG
        config = new Config("config", plugin);
        config.setPrefix("prefix");
        config.loadDefaultConfig();

        // MESSAGE DATA
        messageData = new Config("messageData", plugin);
        messageData.loadDefaultConfig();

        // ECONOMY
        economy = new EconomyAPI().getEconomyManager();

        // DUTY
        run();

        // EVENT
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), plugin);
    }

    private void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // TIME
                Date date = new Date();
                String time = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss").format(date);
                if (!time.contains("00:00")) {
                    return;
                }

                for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
                    Player player = p.getPlayer();
                    // DUTY
                    double duty = Math.round(economy.getMoney((player.getPlayer())) * 0.04);
                    economy.removeMoney(player.getPlayer(), duty);

                    // MESSAGE
                    List<String> msg = config.getMessages("duty.duty");
                    msg = msg.stream()
                            .map(line -> line.replace("{duty}", NumberUtil.format(duty))
                                                    .replace("{date}", time)
                                                    .replace("{money}", economy.getStringMoney(player)))
                            .collect(Collectors.toList());



                    // SEND MESSAGE
                    if (player.isOnline()) {
                        msg.forEach(player::sendMessage);
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.MASTER, 1f, 1f);
                    } else {
                        List<String> list = messageData.isExist(player.getUniqueId() + "") ? messageData.getMessages(player.getUniqueId() + "") : new ArrayList<>();
                        list.addAll(msg);
                        messageData.setStringList(player.getUniqueId() + "", list);
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 60 * 20L);
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(plugin);
    }
}
