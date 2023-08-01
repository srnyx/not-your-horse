package xyz.srnyx.notyourhorse;

import org.bukkit.configuration.ConfigurationSection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.PluginPlatform;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;
import xyz.srnyx.annoyingapi.file.AnnoyingData;
import xyz.srnyx.annoyingapi.file.AnnoyingFile;
import xyz.srnyx.annoyingapi.file.AnnoyingResource;
import xyz.srnyx.annoyingapi.message.AnnoyingMessage;
import xyz.srnyx.annoyingapi.message.DefaultReplaceType;


public class NotYourHorse extends AnnoyingPlugin {
    public AnnoyingData data;
    public boolean enabled = true;
    @Range(from = 0, to = 100) public int chance;
    @Nullable public Double damage;

    public NotYourHorse() {
        options
                .pluginOptions(pluginOptions -> pluginOptions.updatePlatforms(
                        PluginPlatform.modrinth("not-your-horse"),
                        PluginPlatform.hangar(this, "srnyx"),
                        PluginPlatform.spigot("107339")))
                .bStatsOptions(bStatsOptions -> bStatsOptions.id(18878))
                .registrationOptions
                .automaticRegistration(automaticRegistration -> automaticRegistration.packages(
                        "xyz.srnyx.notyourhorse.commands",
                        "xyz.srnyx.notyourhorse.listeners"))
                .papiExpansionToRegister(() -> new NYHPlaceholders(this));

        reload();
    }

    @Override
    public void reload() {
        // CONFIG
        final ConfigurationSection section = new AnnoyingResource(this, "config.yml").getConfigurationSection("punishment");
        // chance
        int chanceConfig = section.getInt("chance", 50);
        if (chanceConfig < 0) chanceConfig = 0;
        if (chanceConfig > 100) chanceConfig = 100;
        chance = chanceConfig;
        // damage
        final double damageConfig = section.getDouble("damage");
        damage = damageConfig == 0 ? null : damageConfig;

        // DATA
        data = new AnnoyingData(this, "data.yml", new AnnoyingFile.Options<>().canBeEmpty(false));
        // enabled
        enabled = data.getBoolean("enabled", true);
    }

    public void toggle(boolean status, @NotNull AnnoyingSender sender) {
        enabled = status;
        data.setSave("enabled", status);
        new AnnoyingMessage(this, "command.toggle")
                .replace("%status%", status, DefaultReplaceType.BOOLEAN)
                .send(sender);
    }
}
