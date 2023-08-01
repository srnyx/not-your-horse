package xyz.srnyx.notyourhorse.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.AnnoyingListener;
import xyz.srnyx.annoyingapi.message.AnnoyingMessage;
import xyz.srnyx.notyourhorse.NotYourHorse;

import java.util.HashMap;
import java.util.Random;


public class HorseListener implements AnnoyingListener {
    @NotNull private static final Random RANDOM = new Random();

    @NotNull private final NotYourHorse plugin;
    @NotNull private final HashMap<String, String> players = new HashMap<>();

    public HorseListener(@NotNull NotYourHorse plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public NotYourHorse getAnnoyingPlugin() {
        return plugin;
    }

    /**
     * Raised when an entity enters a vehicle.
     */
    @EventHandler
    public void onVehicleEnter(@NotNull VehicleEnterEvent event) {
        // Plugin and vehicle check
        final Vehicle vehicle = event.getVehicle();
        if (!plugin.enabled || !(vehicle instanceof Tameable)) return;
        // Owner, rider, permission, and chance check
        final Entity rider = event.getEntered();
        final AnimalTamer owner = ((Tameable) vehicle).getOwner();
        if (owner == null || owner.equals(rider) || !(rider instanceof Player) || rider.hasPermission("notyourhorse.bypass") || plugin.chance < RANDOM.nextInt(100)) return;
        // GameMode check
        final Player riderPlayer = (Player) rider;
        final GameMode gameMode = riderPlayer.getGameMode();
        if (gameMode == GameMode.CREATIVE || gameMode == GameMode.SPECTATOR) return;

        // Damage/kill rider
        event.setCancelled(true);
        if (plugin.damage != null && riderPlayer.getHealth() - plugin.damage > 0) {
            riderPlayer.damage(plugin.damage);
            return;
        }
        players.put(riderPlayer.getName(), owner.getName());
        riderPlayer.setHealth(0);
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
