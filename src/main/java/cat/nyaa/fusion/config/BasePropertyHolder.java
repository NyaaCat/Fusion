package cat.nyaa.fusion.config;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import think.rpgitems.item.RPGItem;
import think.rpgitems.power.PropertyHolder;

public class BasePropertyHolder implements PropertyHolder {

    @Override
    public void init(ConfigurationSection s) {

    }

    @Override
    public void save(ConfigurationSection s) {

    }

    @Override
    public NamespacedKey getNamespacedKey() {
        return null;
    }

    @Override
    public RPGItem getItem() {
        return null;
    }

    @Override
    public void setItem(RPGItem item) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getPropertyHolderType() {
        return null;
    }
}
