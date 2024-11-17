package xyz.srnyx.notyourhorse;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.PluginPlatform;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;
import xyz.srnyx.annoyingapi.data.StringData;
import xyz.srnyx.annoyingapi.file.AnnoyingData;
import xyz.srnyx.annoyingapi.file.AnnoyingFile;
import xyz.srnyx.annoyingapi.message.AnnoyingMessage;
import xyz.srnyx.annoyingapi.message.DefaultReplaceType;

import java.io.File;
import java.util.logging.Level;


public class NotYourHorse extends AnnoyingPlugin {
    @NotNull public static final String TABLE = "data";
    @NotNull public static final String COL_DISABLED = "disabled";

    public ConfigYml config;
    public StringData data;

    public NotYourHorse() {
        options
                .pluginOptions(pluginOptions -> pluginOptions.updatePlatforms(
                        PluginPlatform.modrinth("GVSyHi8e"),
                        PluginPlatform.hangar(this),
                        PluginPlatform.spigot("107339")))
                .bStatsOptions(bStatsOptions -> bStatsOptions.id(18878))
                .dataOptions(dataOptions -> dataOptions
                        .enabled(true)
                        .table(TABLE, COL_DISABLED))
                .registrationOptions
                .automaticRegistration(automaticRegistration -> automaticRegistration.packages(
                        "xyz.srnyx.notyourhorse.commands",
                        "xyz.srnyx.notyourhorse.listeners"))
                .papiExpansionToRegister(() -> new NYHPlaceholders(this));
    }

    @Override
    public void enable() {
        reload();

        //TODO Convert old data
        final AnnoyingData oldData = new AnnoyingData(this, "data.yml", new AnnoyingFile.Options<>().canBeEmpty(false));
        if (!oldData.file.exists()) return;
        if (!oldData.contains("converted_now-stored-elsewhere") && !oldData.getBoolean("enabled", true)) data.set(COL_DISABLED, true);
        oldData.setSave("converted_now-stored-elsewhere", true);
        // Rename data file to old-data.yml
        if (!oldData.file.renameTo(new File(oldData.file.getParent(), "data-old.yml"))) log(Level.WARNING, "&cFailed to rename old data file: &4" + oldData.file.getPath());
    }

    @Override
    public void reload() {
        config = new ConfigYml(this);
        data = new StringData(this, TABLE, "server");
    }

    public void toggle(boolean status, @NotNull AnnoyingSender sender) {
        data.set(COL_DISABLED, status ? null : true);
        new AnnoyingMessage(this, "command.toggle")
                .replace("%status%", status, DefaultReplaceType.BOOLEAN)
                .send(sender);
    }
}
