package com.github.jaeukkang12.duty.listener;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Collections;

import static com.github.jaeukkang12.duty.Duty.messageData;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (messageData.getMessages(player.getUniqueId() + "").isEmpty()) {
            messageData.getMessages(player.getUniqueId() + "").forEach(player::sendMessage);
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.MASTER, 1f, 1f);
            messageData.setStringList(player.getUniqueId() + "", Collections.emptyList());
        }
    }
}
