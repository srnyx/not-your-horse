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
        // enabled
        if (params.equals("enabled")) return String.valueOf(plugin.enabled);

        // chance
        if (params.equals("chance")) return String.valueOf(plugin.chance);

        // damage
        if (params.equals("damage")) return plugin.damage == null ? "kill" : plugin.damage.toString();

        return null;
    }
}
