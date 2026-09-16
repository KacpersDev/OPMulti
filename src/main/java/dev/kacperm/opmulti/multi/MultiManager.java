package dev.kacperm.opmulti.multi;

import dev.kacperm.opmulti.OPMulti;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class MultiManager {

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
