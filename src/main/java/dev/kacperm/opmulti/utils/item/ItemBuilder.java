package dev.kacperm.opmulti.utils.item;

import dev.kacperm.opmulti.utils.color.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = this.item.getItemMeta();
    }

    public ItemBuilder name(String name) {
        if (this.meta != null && name != null && !name.isEmpty()) {
            this.meta.displayName(Color.translate(name));
        }
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        if (this.meta != null && lore != null && !lore.isEmpty()) {
            this.meta.lore(Color.translateList(lore));
        }
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder customModelData(int customModelData) {
        if (this.meta != null && customModelData > 0) {
            this.meta.setCustomModelData(customModelData);
        }
        return this;
    }

    public ItemStack build() {
        this.item.setItemMeta(this.meta);
        return this.item;
    }
}
