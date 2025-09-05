package me.criex.antibot.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import lombok.RequiredArgsConstructor;
import me.criex.antibot.AntiBot;

/**
 * @author CrieXD1337
 */

@RequiredArgsConstructor
public class PlayerListener implements Listener {

    private final AntiBot main;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        main.getVerificationService().startVerification(event.getPlayer());
    }
}