package de.cuzim1tigaaa.easter.files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import de.cuzim1tigaaa.easter.EasterEggs;
import de.cuzim1tigaaa.easter.utils.RewardType;
import de.cuzim1tigaaa.easter.utils.egg.Category;
import de.cuzim1tigaaa.easter.utils.egg.Egg;
import de.cuzim1tigaaa.easter.utils.egg.EggUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class DataFiles {

	private final EasterEggs plugin;

	public DataFiles(EasterEggs plugin) {
		this.plugin = plugin;
	}

	public void saveEasterEggs() {
		try {
			java.io.File file = new java.io.File(plugin.getDataFolder(), "eastereggs.json");
			if(!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(file, false);
			Gson gson = new GsonBuilder()
					.registerTypeAdapter(Location.class, new LocationAdapter())
					.create();

			gson.toJson(EggUtils.getEggs(), fileWriter);
			fileWriter.flush();
			fileWriter.close();
		}catch(IOException exception) {
			plugin.getLogger().log(Level.SEVERE, "An error occurred while saving the easter egg file!", exception);
		}
	}

	public void loadEasterEggs() {
		EggUtils.getEggs().clear();

		try {
			java.io.File file = new java.io.File(plugin.getDataFolder(), "eastereggs.json");
			if(file.exists()) {
				Gson gson = new GsonBuilder()
						.registerTypeAdapter(Location.class, new LocationAdapter())
						.create();

				JsonReader reader = new JsonReader(new FileReader(file));
				Egg[] eggs = gson.fromJson(reader, Egg[].class);
				if(eggs == null || eggs.length == 0)
					return;
				EggUtils.getEggs().addAll(List.of(eggs));
				reader.close();
			}
		}catch(IOException exception) {
			plugin.getLogger().log(Level.SEVERE, "An error occurred while loading the easter egg file!", exception);
		}
	}

	public void loadCategories() {
		EggUtils.getEggs().clear();
		File file = new File(plugin.getDataFolder(), "categories.yml");
		if(!file.exists()) {
			file.getParentFile().mkdir();
			plugin.saveResource("categories.yml", false);
		}

		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection categories = config.getConfigurationSection("categories");
		for(String cat : categories.getKeys(false)) {
			String id = categories.getString(cat + ".id");
			String name = categories.getString(cat + ".name");
			String customHeadUrl = categories.getString(cat + ".customHeadUrl");
			RewardType rewardType = (RewardType) getEnumByName(cat + ".type", RewardType.class, RewardType.TALER);
			int min = categories.getInt(cat + ".min");
			int max = categories.getInt(cat + ".max");

			List<Material> materials = new ArrayList<>();
			if(rewardType == RewardType.ITEMS) {
				for(String item : categories.getStringList(cat + ".items")) {
					Material material = Material.getMaterial(item);
					if(material != null)
						materials.add(material);
				}
			}
			Category category = new Category(id, name, customHeadUrl, rewardType, min, max, materials);
			EggUtils.getCategories().add(category);
		}
	}

	public <T extends Enum<T>> Enum<T> getEnumByName(String type, Class<T> clazz, T def) {
		return Arrays.stream(clazz.getEnumConstants()).filter(trackType ->
				trackType.name().equalsIgnoreCase(type)).findFirst().orElse(def);
	}
}
