package ru.swenly.swenlycodes;

import org.bukkit.plugin.java.JavaPlugin;
import ru.swenly.swenlycodes.commands.*;

public final class SwenlyCodes extends JavaPlugin {
    @Override
    public void onEnable() {
        getCommand("code").setExecutor(new CodeCMD());
        getCommand("code").setTabCompleter(new CodeCompleter());

        getCommand("test").setExecutor(new TestCMD());
        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
