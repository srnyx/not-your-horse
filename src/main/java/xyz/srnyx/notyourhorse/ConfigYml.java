package xyz.srnyx.notyourhorse;

import org.bukkit.configuration.ConfigurationSection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import xyz.srnyx.annoyingapi.file.AnnoyingResource;


public class ConfigYml {
    @NotNull private final AnnoyingResource config;
    @NotNull public final Punishment punishment;

    public ConfigYml(@NotNull NotYourHorse plugin) {
        config = new AnnoyingResource(plugin, "config.yml");
        punishment = new Punishment();
    }

    public class Punishment {
        @Range(from = 0, to = 100) public int chance = 50;
        @Nullable public Double damage = null;

        public Punishment() {
            final ConfigurationSection section = config.getConfigurationSection("punishment");
            if (section == null) return;

            // chance
            chance = section.getInt("chance", chance);
            if (chance < 0) chance = 0;
            if (chance > 100) chance = 100;

            // damage
            final String damageString = section.getString("damage");
            if (damageString == null || damageString.equalsIgnoreCase("kill")) {
                damage = null;
            } else {
                damage = section.getDouble("damage");
            }
        }
    }
}
