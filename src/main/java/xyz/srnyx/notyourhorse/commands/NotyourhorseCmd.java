package xyz.srnyx.notyourhorse.commands;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.command.AnnoyingCommand;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;
import xyz.srnyx.annoyingapi.message.AnnoyingMessage;

import xyz.srnyx.notyourhorse.NotYourHorse;

import java.util.Arrays;
import java.util.List;


public class NotyourhorseCmd implements AnnoyingCommand {
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
        final String[] args = sender.args;

        if (args.length == 0) {
            plugin.toggle(!plugin.enabled, sender);
            return;
        }

        if (args.length == 1) {
            // <on|off>
            if (sender.argEquals(0, "on", "off")) {
                plugin.toggle(sender.argEquals(0, "on"), sender);
                return;
            }

            // reload
            if (sender.argEquals(0, "reload")) {
                plugin.reloadPlugin();
                new AnnoyingMessage(plugin, "command.reload").send(sender);
                return;
            }
        }

        sender.invalidArguments();
    }

    @Override @NotNull
    public List<String> onTabComplete(@NotNull AnnoyingSender sender) {
        return Arrays.asList("reload", plugin.enabled ? "off" : "on");
    }
}
