package xyz.srnyx.notyourhorse;

import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.srnyx.annoyingapi.AnnoyingPAPIExpansion;
import xyz.srnyx.annoyingapi.AnnoyingPlugin;


public class NYHPlaceholders extends AnnoyingPAPIExpansion {
    @NotNull private final NotYourHorse plugin;

    public NYHPlaceholders(@NotNull NotYourHorse plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public AnnoyingPlugin getAnnoyingPlugin() {
        return plugin;
    }

    @Override @NotNull
    public String getIdentifier() {
        return "notyourhorse";
    }

    @Override @Nullable
    public String onPlaceholderRequest(@Nullable Player player, @NotNull String params) {
        switch (params) {
            // enabled
            case "enabled": return String.valueOf(!plugin.data.has(NotYourHorse.COL_DISABLED));
            // chance
            case "chance": return String.valueOf(plugin.config.punishment.chance);
            // damage
            case "damage": return plugin.config.punishment.damage == null ? "kill" : plugin.config.punishment.damage.toString();

            default: return null;
        }
    }
}
