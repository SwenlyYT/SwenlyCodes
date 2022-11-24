package ru.swenly.swenlycodes.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.swenly.swenlycodes.SwenlyCodes;

public class TestCMD implements CommandExecutor {
    public static FileConfiguration config;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        config = SwenlyCodes.getPlugin(SwenlyCodes.class).getConfig();
        sender.sendMessage(config.getString("aboba") + "");
        return true;
    }
}
