package xyz.srnyx.notyourhorse;

import org.bukkit.GameMode;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.AnnoyingListener;
import xyz.srnyx.annoyingapi.AnnoyingMessage;
import xyz.srnyx.annoyingapi.AnnoyingPlugin;

import java.util.HashMap;
import java.util.Random;


public class HorseListener implements AnnoyingListener {
    @NotNull private final NotYourHorse plugin;
    @NotNull private final Random random = new Random();
    @NotNull private final HashMap<String, String> players = new HashMap<>();

    @Contract(pure = true)
    public HorseListener(@NotNull NotYourHorse plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull AnnoyingPlugin getPlugin() {
        return plugin;
    }

    /**
     * Raised when an entity enters a vehicle.
     */
    @EventHandler
    public void onVehicleEnter(@NotNull VehicleEnterEvent event) {
        final Entity vehicle = event.getVehicle();
        if (!plugin.enabled || !(vehicle instanceof AbstractHorse)) return;
        final Entity rider = event.getEntered();
        final AnimalTamer owner = ((AbstractHorse) vehicle).getOwner();
        if (owner == null || owner.equals(event.getEntered()) // owner checks
                || !(rider instanceof Player) || rider.hasPermission("notyourhorse.bypass") // player & permission check
                || plugin.chance < random.nextInt(100)) return; // chance check
        final Player riderPlayer = (Player) rider;
        if (riderPlayer.getGameMode() == GameMode.CREATIVE) return; // gamemode check

        event.setCancelled(true);
        if (plugin.damage == null || riderPlayer.getHealth() - plugin.damage <= 0) {
            players.put(riderPlayer.getName(), owner.getName());
            riderPlayer.setHealth(0);
            return;
        }
        riderPlayer.damage(plugin.damage);
    }

    /**
     * Thrown whenever a {@link Player} dies
     */
    @EventHandler
    public void onPlayerDeath(@NotNull PlayerDeathEvent event) {
        final String name = event.getEntity().getName();
        final String owner = players.get(name);
        if (owner == null) return;
        players.remove(name);
        event.setDeathMessage(new AnnoyingMessage(plugin, "death")
                .replace("%player%", name)
                .replace("%owner%", owner)
                .toString());
    }
}
