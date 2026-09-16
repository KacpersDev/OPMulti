package dev.kacperm.opmulti.multi;

import dev.kacperm.opmulti.OPMulti;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.gangsta.opboosters.api.BoosterAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MultiManager {

    public double getTotalMultiplier(Player player, MultiType type) {
        String boosterTypeName = OPMulti.getInstance().getConfiguration().getConfiguration()
                .getString("settings.gui.types." + type.getKey() + ".booster-type", "CUSTOM");

        BoosterAPI.BoosterType boosterType;
        try {
            boosterType = BoosterAPI.BoosterType.valueOf(boosterTypeName.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            boosterType = BoosterAPI.BoosterType.CUSTOM;
        }

        return BoosterAPI.getTotalMultiplier(player, boosterType);
    }

    public MultiData getData(MultiType type) {
        ConfigurationSection section = OPMulti.getInstance().getConfiguration().getConfiguration()
                .getConfigurationSection("settings.gui.types." + type.getKey() + ".sources");

        if (section == null) {
            return new MultiData(List.of(), List.of());
        }

        return new MultiData(
                this.readSources(section.getConfigurationSection("permanent")),
                this.readSources(section.getConfigurationSection("temporary"))
        );
    }

    private List<MultiSource> readSources(ConfigurationSection section) {
        List<MultiSource> sources = new ArrayList<>();
        if (section == null) {
            return sources;
        }

        for (String key : section.getKeys(false)) {
            sources.add(new MultiSource(key, section.getDouble(key)));
        }

        return sources;
    }
}
