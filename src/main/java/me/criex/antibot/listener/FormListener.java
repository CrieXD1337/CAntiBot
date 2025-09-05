package me.criex.antibot.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.window.FormWindowCustom;
import lombok.RequiredArgsConstructor;
import me.criex.antibot.AntiBot;

/**
 * @author CrieXD1337
 */

@RequiredArgsConstructor
public class FormListener implements Listener {

    private final AntiBot main;

    @EventHandler
    public void onFormResponse(PlayerFormRespondedEvent event) {
        if (!(event.getWindow() instanceof FormWindowCustom)) return;

        if (event.getFormID() == main.getFormService().getCaptchaFormId()) {
            if (event.wasClosed()) {
                main.getVerificationService().cancelVerification(event.getPlayer());
                main.getVerificationService().startVerification(event.getPlayer());
                return;
            }

            FormResponseCustom response = (FormResponseCustom) event.getResponse();
            if (response == null) return;

            String input = response.getInputResponse(0);
            if (input != null) {
                main.getVerificationService().verifyCaptcha(event.getPlayer(), input);
            }
        }
    }
}