package dev.kacperm.opmulti;

import dev.kacperm.opmulti.commands.MultiCommand;
import dev.kacperm.opmulti.gui.MultiGUIListener;
import dev.kacperm.opmulti.utils.config.Config;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.gangsta.opboosters.OPBoosters;
import org.gangsta.opboosters.api.BoosterAPI;

import java.io.File;

@Getter
public final class OPMulti extends JavaPlugin {

    @Getter
    private static OPMulti instance;

    private Config configuration, language;

    @Override
    public void onEnable() {
        instance = this;

        this.loadConfigurations();
        this.loadListeners();
        this.loadCommands();

        boolean opBoostersEnabled = getServer().getPluginManager().getPlugin("OPBoosters") instanceof OPBoosters
                && BoosterAPI.isAvailable();
        if (!opBoostersEnabled) {
            getLogger().warning("OPBoosters was not found or is not enabled - multiplier totals will show as 1.0x.");
        }
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    private void loadConfigurations() {
        this.configuration = new Config(this, new File(getDataFolder(), "configuration.yml"),
                new YamlConfiguration(), "configuration.yml");
        this.language = new Config(this, new File(getDataFolder(), "language.yml"),
                new YamlConfiguration(), "language.yml");

        this.configuration.create();
        this.language.create();
    }

    private void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new MultiGUIListener(), this);
    }

    private void loadCommands() {
        PluginCommand multiCommand = getCommand("multi");
        if (multiCommand != null) {
            multiCommand.setExecutor(new MultiCommand());
        }
    }
}
