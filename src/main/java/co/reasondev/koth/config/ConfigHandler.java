package co.reasondev.koth.config;

import co.reasondev.koth.KotH;
import co.reasondev.koth.Messaging;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class ConfigHandler {

    private KotH plugin;
    private static Map<String, YamlConfiguration> loadedConfigs = new TreeMap<String, YamlConfiguration>(String.CASE_INSENSITIVE_ORDER);

    public ConfigHandler(KotH plugin) {
        this.plugin = plugin;
    }

    public void loadConfigs() {
        loadConfig("config");
        loadConfig("lang");
        loadConfig("hills");
        Messaging.printInfo("Configurations successfully loaded!");
    }

    public void saveConfigs() {
        saveConfig("hills");
        Messaging.printInfo("Configurations successfully saved!");
    }

    /**
     * Retrieve a currently loaded YamlConfiguration
     *
     * @param fileName The String key corresponding to the YamlConfiguration
     * @return The corresponding YamlConfiguration value, if it exists
     */
    public static YamlConfiguration getConfig(String fileName) {
        return loadedConfigs.get(fileName);
    }

    /**
     * Checks to see if a YamlConfiguration is loaded
     *
     * @param fileName The String key corresponding to the YamlConfiguration
     * @return Whether or not the YamlConfiguration is loaded
     */
    private boolean isConfigLoaded(String fileName) {
        return loadedConfigs.containsKey(fileName);
    }

    /**
     * Loads a new YamlConfiguration from an existing or newly created file
     *
     * @param fileName The name of the file without a .yml ending
     */
    private void loadConfig(String fileName) {
        File cFile = new File(plugin.getDataFolder(), fileName + ".yml");
        if (!validateFile(cFile)) Messaging.printErr("Error! Could not load Configuration!", fileName);
        else if (!isConfigLoaded(fileName)) loadedConfigs.put(fileName, YamlConfiguration.loadConfiguration(cFile));
    }

    /**
     * Saves a loaded YamlConfiguration to a file
     *
     * @param fileName The name of the file without a .yml ending
     */
    private void saveConfig(String fileName) {
        if (!isConfigLoaded(fileName))
            Messaging.printErr("Error! Tried to save non-existent Configuration!", fileName);
        else if (!saveFile(fileName)) Messaging.printErr("Error! Could not save Configuration!", fileName);
    }

    /**
     * Checks to see if a given File exists or can be created
     *
     * @param file The File in question
     * @return Whether or not the File exists/was created successfully
     */
    private boolean validateFile(File file) {
        if (file.exists()) return true;
        if (plugin.getResource(file.getName()) != null) plugin.saveResource(file.getName(), false);
        else
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return file.exists();
    }

    /**
     * Attempts to save a given File
     *
     * @param fileName The name of the file without a .yml ending
     * @return Whether or not the File was successfully saved
     */
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
