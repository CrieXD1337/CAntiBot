package me.criex.antibot;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import lombok.Getter;
import me.criex.antibot.listener.FormListener;
import me.criex.antibot.listener.PlayerListener;
import me.criex.antibot.service.FormService;
import me.criex.antibot.service.VerificationService;

import java.io.File;

/**
 * @author CrieXD1337
 */

@Getter
public class AntiBot extends PluginBase {

    private Config config;
    private FormService formService;
    private VerificationService verificationService;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.config = new Config(new File(getDataFolder(), "config.yml"), Config.YAML);
        this.formService = new FormService(this);
        this.verificationService = new VerificationService(this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new FormListener(this), this);
        getLogger().info("AntiBotPlugin enabled!");
    }
}