package me.criex.antibot.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.event.player.*;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.CommandRequestPacket;
import cn.nukkit.network.protocol.TextPacket;
import lombok.RequiredArgsConstructor;
import me.criex.antibot.AntiBot;

import java.util.UUID;

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

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) return;
        if (isPlayerInVerification(event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().sendTip(main.getConfig().getString("no-access-to-do"));
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        if (isPlayerInVerification(player)) {
            event.setCancelled(true);
            player.sendTip(main.getConfig().getString("no-access-to-do"));
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (event.isCancelled()) return;
        if (isPlayerInVerification(event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().sendTip(main.getConfig().getString("no-access-to-do"));
        }
    }

    @EventHandler
    public void onInventoryTransaction(InventoryTransactionEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getTransaction().getSource();
        if (isPlayerInVerification(player)) {
            event.setCancelled(true);
            player.sendTip(main.getConfig().getString("no-access-to-do"));
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        if (isPlayerInVerification(player)) {
            if (event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK ||
                    event.getAction() == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
                event.setCancelled(true);
                event.getPlayer().sendTip(main.getConfig().getString("no-access-to-do"));
            }
        }
    }

    @EventHandler
    public void onDataPacketReceive(DataPacketReceiveEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        if (isPlayerInVerification(player)) {
            if (event.getPacket() instanceof CommandRequestPacket) {
                event.setCancelled(true);
                player.sendTip(main.getConfig().getString("no-access-to-do"));
            }

            if (event.getPacket() instanceof TextPacket) {
                TextPacket packet = (TextPacket) event.getPacket();
                if (packet.type == TextPacket.TYPE_CHAT) {
                    event.setCancelled(true);
                    player.sendTip(main.getConfig().getString("no-access-to-do"));
                }
            }
        }
    }

    //@EventHandler
    //public void onEntityDamage(EntityDamageEvent event) {
    //    if (event.isCancelled()) return;
    //    if (event.getEntity() instanceof Player) {
    //        Player player = (Player) event.getEntity();
    //        if (isPlayerInVerification(player)) {
    //            UUID playerId = player.getUniqueId();
    //            event.setCancelled(true);
    //            player.sendTip(main.getConfig().getString("no-access-to-do"));
    //        }
    //    }
    //}

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        if (isPlayerInVerification(player)) {
            event.setCancelled(true);
            player.sendMessage(main.getConfig().getString("no-access-to-do"));
        }
    }

    private boolean isPlayerInVerification(Player player) {
        return main.getVerificationService().getVerificationData().containsKey(player.getUniqueId());
    }
}