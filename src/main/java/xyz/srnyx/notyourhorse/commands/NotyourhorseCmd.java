package xyz.srnyx.notyourhorse.commands;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.command.AnnoyingCommand;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;
import xyz.srnyx.annoyingapi.message.AnnoyingMessage;

import xyz.srnyx.notyourhorse.NotYourHorse;

import java.util.Arrays;
import java.util.List;


public class NotyourhorseCmd extends AnnoyingCommand {
    @NotNull private final NotYourHorse plugin;

    public NotyourhorseCmd(@NotNull NotYourHorse plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public NotYourHorse getAnnoyingPlugin() {
        return plugin;
    }

    @Override @NotNull
    public String getPermission() {
        return "notyourhorse.command";
    }

    @Override
    public void onCommand(@NotNull AnnoyingSender sender) {
        if (sender.args.length == 1) {
            // reload
            if (sender.argEquals(0, "reload")) {
                plugin.reloadPlugin();
                new AnnoyingMessage(plugin, "command.reload").send(sender);
                return;
            }

            // <on|off>
            plugin.toggle(sender.argEquals(0, "on"), sender);
            return;
        }

        // No args: toggle
        plugin.toggle(!plugin.data.has(NotYourHorse.COL_DISABLED), sender);
    }

    @Override @NotNull
    public List<String> onTabComplete(@NotNull AnnoyingSender sender) {
        return Arrays.asList("reload", plugin.data.has(NotYourHorse.COL_DISABLED) ? "on" : "off");
    }
}
