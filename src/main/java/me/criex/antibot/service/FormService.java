package me.criex.antibot.service;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.window.FormWindowCustom;
import lombok.RequiredArgsConstructor;
import me.criex.antibot.AntiBot;

/**
 * @author CrieXD1337
 */

@RequiredArgsConstructor
public class FormService {

    private final AntiBot main;
    private static final int CAPTCHA_FORM_ID = 3592;

    public void showCaptchaForm(Player player, String captcha) {
        FormWindowCustom form = new FormWindowCustom(main.getConfig().getString("form-title"));
        String content = main.getConfig().getString("form-content").replace("{captcha}", captcha);
        form.addElement(new ElementInput(content, main.getConfig().getString("form-placeholder")));
        player.showFormWindow(form, CAPTCHA_FORM_ID);
    }

    public int getCaptchaFormId() {
        return CAPTCHA_FORM_ID;
    }
}