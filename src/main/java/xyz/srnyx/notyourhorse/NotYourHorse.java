package xyz.srnyx.notyourhorse;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import xyz.srnyx.annoyingapi.AnnoyingMessage;
import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;
import xyz.srnyx.annoyingapi.file.AnnoyingData;
import xyz.srnyx.annoyingapi.file.AnnoyingResource;


public class NotYourHorse extends AnnoyingPlugin {
    @NotNull public final AnnoyingData data = new AnnoyingData(this, "data.yml");
    public boolean enabled;
    @Range(from = 0, to = 100) public int chance;
    @Nullable public Double damage;

    public NotYourHorse() {
        super();
        reload();

        // Options
        options.colorLight = ChatColor.GREEN;
        options.colorDark = ChatColor.DARK_GREEN;
        options.commands.add(new NotyourhorseCommand(this));
        options.listeners.add(new HorseListener(this));
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
        // enabled
        enabled = data.getBoolean("enabled", true);
    }

    public void toggle(boolean status, @NotNull AnnoyingSender sender) {
        enabled = status;
        data.set("enabled", status, true);
        new AnnoyingMessage(this, "command.toggle")
                .replace("%status%", status, AnnoyingMessage.DefaultReplaceType.BOOLEAN)
                .send(sender);
    }
}
