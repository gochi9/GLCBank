package com.deadshotmdf.GLCBank.File;

import com.deadshotmdf.GLCBank.GLCB;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public abstract class InformationHolder {

    private final File file;
    private FileConfiguration config;

    public InformationHolder(GLCB main, String yml) {
        this.file = new File(main.getDataFolder(), "/"+yml);

        if(file.exists())
            return;

        file.mkdirs();
        main.saveResource(yml, false);
    }

    public FileConfiguration getConfig() {
        if(config == null)
            this.config = YamlConfiguration.loadConfiguration(file);

        return config;
    }

    public abstract void saveInformation();
    public abstract void loadInformation();

    public Set<String> getKeys(String section){
        ConfigurationSection sec = getConfig().getConfigurationSection(section);

        if(sec == null)
            return new HashSet<>();

        return sec.getKeys(false);
    }

    public void saveC(){
        try {
            getConfig().save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
