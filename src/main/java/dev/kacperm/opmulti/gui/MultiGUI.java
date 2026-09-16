package dev.kacperm.opmulti.gui;

import dev.kacperm.opmulti.OPMulti;
import dev.kacperm.opmulti.multi.MultiData;
import dev.kacperm.opmulti.multi.MultiManager;
import dev.kacperm.opmulti.multi.MultiSource;
import dev.kacperm.opmulti.multi.MultiType;
import dev.kacperm.opmulti.utils.color.Color;
import dev.kacperm.opmulti.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class MultiGUI implements InventoryHolder {

    private final MultiManager multiManager = new MultiManager();
    private final Player owner;
    private final Inventory inventory;
    private final Set<MultiType> expandedTypes = new HashSet<>();
    private final Map<Integer, MultiType> typeSlots = new HashMap<>();

    public MultiGUI(Player owner) {
        this.owner = owner;

        YamlConfiguration config = OPMulti.getInstance().getConfiguration().getConfiguration();

        int rows = config.getInt("settings.gui.rows", 6);
        String title = config.getString("settings.gui.title", "&6&lMultipliers");

        this.inventory = Bukkit.createInventory(this, rows * 9, Color.translate(title));

        this.fill(config);
        this.build(config);
    }

    private void fill(YamlConfiguration config) {
        Material material = Material.matchMaterial(config.getString("settings.gui.filler.material", "GRAY_STAINED_GLASS_PANE"));
        if (material == null) {
            material = Material.GRAY_STAINED_GLASS_PANE;
        }

        ItemStack filler = new ItemBuilder(material)
                .name(config.getString("settings.gui.filler.name", " "))
                .customModelData(config.getInt("settings.gui.filler.custom-model-data", 0))
                .build();

        for (int slot = 0; slot < this.inventory.getSize(); slot++) {
            this.inventory.setItem(slot, filler);
        }
    }

    private void build(YamlConfiguration config) {
        ConfigurationSection typesSection = config.getConfigurationSection("settings.gui.types");
        if (typesSection == null) {
            return;
        }

        for (MultiType type : MultiType.values()) {
            ConfigurationSection typeSection = typesSection.getConfigurationSection(type.getKey());
            if (typeSection == null) {
                continue;
            }

            int slot = typeSection.getInt("slot", -1);
            if (slot < 0 || slot >= this.inventory.getSize()) {
                continue;
            }

            this.typeSlots.put(slot, type);
            this.inventory.setItem(slot, this.buildItem(config, typeSection, type));
        }
    }

    private ItemStack buildItem(YamlConfiguration config, ConfigurationSection typeSection, MultiType type) {
        Material material = Material.matchMaterial(typeSection.getString("material", "STONE"));
        if (material == null) {
            material = Material.STONE;
        }

        MultiData data = this.multiManager.getData(type);
        double total = this.multiManager.getTotalMultiplier(this.owner, type);
        boolean expanded = this.expandedTypes.contains(type);

        List<String> loreTemplate = config.getStringList(expanded ? "settings.gui.lore.expanded" : "settings.gui.lore.collapsed");

        return new ItemBuilder(material)
                .name(typeSection.getString("display-name", type.name()))
                .lore(this.applyPlaceholders(config, loreTemplate, total, data))
                .customModelData(typeSection.getInt("custom-model-data", 0))
                .build();
    }

    private List<String> applyPlaceholders(YamlConfiguration config, List<String> loreTemplate, double total, MultiData data) {
        String sourceFormat = config.getString("settings.gui.lore.source-format", "&7 - &f{name}: &a+{value}x");
        String permanent = this.formatSources(sourceFormat, data.permanent());
        String temporary = this.formatSources(sourceFormat, data.temporary());

        List<String> lore = new ArrayList<>();
        for (String line : loreTemplate) {
            String replaced = line
                    .replace("{total}", this.format(total))
                    .replace("{permanent}", permanent)
                    .replace("{temporary}", temporary);

            lore.addAll(List.of(replaced.split("\n", -1)));
        }

        return lore;
    }

    private String formatSources(String format, List<MultiSource> sources) {
        if (sources.isEmpty()) {
            return "&7 - &fNone";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < sources.size(); i++) {
            MultiSource source = sources.get(i);
            builder.append(format.replace("{name}", source.name()).replace("{value}", this.format(source.value())));

            if (i < sources.size() - 1) {
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    private String format(double value) {
        return String.format(Locale.US, "%.2f", value);
    }

    public void toggle(MultiType type) {
        if (!this.expandedTypes.remove(type)) {
            this.expandedTypes.add(type);
        }

        YamlConfiguration config = OPMulti.getInstance().getConfiguration().getConfiguration();
        ConfigurationSection typeSection = config.getConfigurationSection("settings.gui.types." + type.getKey());
        if (typeSection == null) {
            return;
        }

        for (Map.Entry<Integer, MultiType> entry : this.typeSlots.entrySet()) {
            if (entry.getValue() == type) {
                this.inventory.setItem(entry.getKey(), this.buildItem(config, typeSection, type));
                break;
            }
        }
    }

    public MultiType getTypeAt(int slot) {
        return this.typeSlots.get(slot);
    }

    public void open() {
        this.owner.openInventory(this.inventory);
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}
