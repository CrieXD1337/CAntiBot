package me.criex.antibot.service;

import cn.nukkit.Player;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.BossBarColor;
import cn.nukkit.utils.DummyBossBar;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.criex.antibot.AntiBot;
import me.criex.antibot.utils.CaptchaGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author CrieXD1337
 */

@RequiredArgsConstructor
public class VerificationService {

    private final AntiBot main;
    @Getter
    private final Map<UUID, VerificationData> verificationData = new HashMap<>();

    public void startVerification(Player player) {
        String[] captchaData = CaptchaGenerator.generateCaptcha(main.getConfig().getInt("captcha-length")).split(":");
        String originalCaptcha = captchaData[0];
        String displayCaptcha = captchaData[1];
        int timeout = main.getConfig().getInt("timeout-seconds");

        DummyBossBar bossBar = new DummyBossBar.Builder(player)
                .text(main.getConfig().getString("bossbar-title").replace("{seconds}", String.valueOf(timeout)))
                .length(100)
                .color(BossBarColor.RED)
                .build();
        player.createBossBar(bossBar);

        VerificationData data = new VerificationData(originalCaptcha, bossBar.getBossBarId(), timeout);
        verificationData.put(player.getUniqueId(), data);

        main.getFormService().showCaptchaForm(player, displayCaptcha);
        player.setImmobile(true);

        data.setTaskId(main.getServer().getScheduler().scheduleRepeatingTask(main, new Task() {
            @Override
            public void onRun(int currentTick) {
                VerificationData data = verificationData.get(player.getUniqueId());
                if (data == null) return;

                int remaining = data.decrementTime();
                bossBar.setText(main.getConfig().getString("bossbar-title").replace("{seconds}", String.valueOf(remaining)));
                bossBar.setLength((float) remaining / timeout * 100);

                if (remaining <= 0) {
                    player.removeBossBar(bossBar.getBossBarId());
                    verificationData.remove(player.getUniqueId());
                    player.setImmobile(false);
                    player.kick(main.getConfig().getString("kick-message-timeout"));
                    cancel();
                }
            }
        }, 20).getTaskId());
    }

    public void verifyCaptcha(Player player, String input) {
        VerificationData data = verificationData.get(player.getUniqueId());
        if (data == null) return;

        main.getServer().getScheduler().cancelTask(data.getTaskId());
        String processedInput = CaptchaGenerator.convertInputToOriginal(input.trim());
        if (data.getCaptcha().equalsIgnoreCase(processedInput)) {
            player.removeBossBar(data.getBossBarId());
            verificationData.remove(player.getUniqueId());
            player.setImmobile(false);
            player.sendMessage(main.getConfig().getString("success-message"));
        } else {
            player.removeBossBar(data.getBossBarId());
            verificationData.remove(player.getUniqueId());
            player.setImmobile(false);
            player.kick(main.getConfig().getString("kick-message-fail"));
        }
    }

    public void cancelVerification(Player player) {
        VerificationData data = verificationData.get(player.getUniqueId());
        if (data == null) return;

        main.getServer().getScheduler().cancelTask(data.getTaskId());
        player.removeBossBar(data.getBossBarId());
        verificationData.remove(player.getUniqueId());
        player.setImmobile(false);
    }

    @Getter
    @RequiredArgsConstructor
    public static class VerificationData {
        private final String captcha;
        private final long bossBarId;
        private int time;
        private int taskId;

        public VerificationData(String captcha, long bossBarId, int time) {
            this.captcha = captcha;
            this.bossBarId = bossBarId;
            this.time = time;
        }

        public void setTaskId(int taskId) {
            this.taskId = taskId;
        }

        public int decrementTime() {
            return --time;
        }
    }
}