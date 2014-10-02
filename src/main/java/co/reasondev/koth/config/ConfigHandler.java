/*
 * Copyright © ReasonDev 2014
 * All Rights Reserved
 * No part of this project or any of its contents may be reproduced, copied, modified or adapted, without the prior written consent of SirReason.
 */

package co.reasondev.koth.config;

import co.reasondev.koth.KoTH;
import co.reasondev.koth.util.Messaging;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class ConfigHandler {

    private static KoTH plugin;
    private static Map<String, YamlConfiguration> loadedConfigs = new TreeMap<String, YamlConfiguration>(String.CASE_INSENSITIVE_ORDER);

    public ConfigHandler(KoTH plugin) {
        this.plugin = plugin;
    }

    public void loadConfigs() {
        loadConfig("config");
        loadConfig("hills");
    }

    public void saveConfigs() {
        saveConfig("hills");
    }

    private boolean isConfigLoaded(String fileName) {
        return loadedConfigs.containsKey(fileName);
    }

    public static YamlConfiguration getConfig(String fileName) {
        return loadedConfigs.get(fileName);
    }

    public static void reloadConfig() {
        plugin.reloadConfig();
    }

    private void loadConfig(String fileName) {
        File cFile = new File(plugin.getDataFolder(), fileName + ".yml");
        if(!validateFile(cFile)) Messaging.printErr("Error! Could not load Configuration! [" + fileName + ".yml]");
        else if(!isConfigLoaded(fileName)) loadedConfigs.put(fileName, YamlConfiguration.loadConfiguration(cFile));
    }

    private void saveConfig(String fileName) {
        if(!isConfigLoaded(fileName)) Messaging.printErr("Error! Tried to save non-existent Configuration! [" + fileName + ".yml]");
        else if(!saveFile(fileName)) Messaging.printErr("Error! Could not save Configuration! [" + fileName + ".yml]");
    }

    private boolean validateFile(File file) {
        if (file.exists()) return true;
        else if (plugin.getResource(file.getName()) != null) plugin.saveResource(file.getName(), false);
        else
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return file.exists();
    }

    private boolean saveFile(String fileName) {
        File cFile = new File(plugin.getDataFolder(), fileName + ".yml");
        try {
            loadedConfigs.get(fileName).save(cFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
