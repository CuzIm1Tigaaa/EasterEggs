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
		File configFile = new File(plugin.getDataFolder(), "config.yml");
		if(!configFile.exists()) {
			this.configFile = new YamlConfiguration();
			this.configFile.save(configFile);
		}
		this.configFile = YamlConfiguration.loadConfiguration(configFile);

		set(Paths.CONFIG_DB_HOST, "localhost");
		set(Paths.CONFIG_DB_PORT, 3306);
		set(Paths.CONFIG_DB_DATABASE, "database");
		set(Paths.CONFIG_DB_USERNAME, "username");
		set(Paths.CONFIG_DB_PASSWORD, "password");
		set(Paths.CONFIG_DB_NAME, "Easter");

		set(Paths.CONFIG_HIGHLIGHT_USE, true);
		set(Paths.CONFIG_HIGHLIGHT_PARTICLE, "HAPPY_VILLAGER");
		set(Paths.CONFIG_HIGHLIGHT_DISTANCE, 7.0);

		this.configFile.save(configFile);
	}

	private void set(String path, Object object) {
		configFile.set(path, configFile.get(path, object));
	}

	public String getString(String path) {
		return configFile.getString(path);
	}

	public int getInt(String path) {
		return configFile.getInt(path);
	}

	public double getDouble(String path) {
		return configFile.getDouble(path);
	}

	public boolean getBoolean(String path) {
		return configFile.getBoolean(path);
	}
}