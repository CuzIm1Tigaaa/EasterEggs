package de.cuzim1tigaaa.easter.files;

import de.cuzim1tigaaa.easter.EasterEggs;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

	private static Config config;

	public static Config getConfig() {
		if(config == null)
			config = new Config();
		return config;
	}

	private YamlConfiguration configFile;

	public void loadConfig(EasterEggs plugin) throws IOException {
		File messageFile = new File(plugin.getDataFolder(), "config.yml");
		if(!messageFile.exists()) {
			configFile = new YamlConfiguration();
			configFile.save(messageFile);
		}


		configFile = YamlConfiguration.loadConfiguration(messageFile);
	}

	public String getString(String path) {
		return configFile.getString(path);
	}

	public int getInt(String path) {
		return configFile.getInt(path);
	}

	public boolean getBoolean(String path) {
		return configFile.getBoolean(path);
	}
}
