package me.criex.antibot.utils;

import cn.nukkit.plugin.PluginBase;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class UpdateChecker {

    private final PluginBase plugin;
    private final String currentCommit;
    private static final String GITHUB_API_URL = "https://api.github.com/repos/CrieXD1337/CAntiBot/commits/master";

    public UpdateChecker(PluginBase plugin) {
        this.plugin = plugin;
        this.currentCommit = getCurrentCommit();
    }

    private String getCurrentCommit() {
        Properties properties = new Properties();
        try (InputStream gitFileStream = plugin.getClass().getClassLoader().getResourceAsStream("git.properties")) {
            if (gitFileStream != null) {
                properties.load(gitFileStream);
                return properties.getProperty("git.commit.id.abbrev", "dev");
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to load git.properties: " + e.getMessage());
        }
        return "dev";
    }

    public void checkForUpdates() {
        CompletableFuture.runAsync(() -> {
            try {
                URLConnection request = new URL(GITHUB_API_URL).openConnection();
                request.setRequestProperty("Accept", "application/vnd.github.v3+json");
                request.connect();
                InputStreamReader content = new InputStreamReader(request.getInputStream());
                String latestCommit = JsonParser.parseReader(content).getAsJsonObject().get("sha").getAsString().substring(0, 7);
                content.close();

                if (currentCommit.equals(latestCommit)) {
                    plugin.getLogger().info("§aYou are running the latest version of §2CAntiBot §7(commit: " + currentCommit + ")");
                } else {
                    plugin.getLogger().info("§eA new version is available! §6Current commit: §e" + currentCommit + "§6, Latest commit: §e" + latestCommit);
                    plugin.getLogger().info("§6Download the latest version at: §ehttps://github.com/CrieXD1337/CAntiBot/actions");
                }
            } catch (Exception e) {
                plugin.getLogger().error("§cFailed to check for updates: " + e.getMessage());
            }
        });
    }
}