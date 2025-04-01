package de.cuzim1tigaaa.easter.files.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import de.cuzim1tigaaa.easter.EasterEggs;
import de.cuzim1tigaaa.easter.utils.RewardType;
import de.cuzim1tigaaa.easter.utils.egg.*;
import de.cuzim1tigaaa.guimanager.ItemUtils;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.*;
import java.util.*;
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
				plugin.getLogger().log(Level.INFO, "Loaded " + eggs.length + " easter eggs");
				reader.close();
			}
		}catch(IOException exception) {
			plugin.getLogger().log(Level.SEVERE, "An error occurred while loading the easter egg file!", exception);
		}
	}

	public void loadCategories() {
		EggUtils.getCategories().clear();
		File file = new File(plugin.getDataFolder(), "categories.yml");
		if(!file.exists()) {
			file.getParentFile().mkdir();
			plugin.saveResource("categories.yml", false);
		}

		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection categories = config.getConfigurationSection("categories");
		if(categories == null)
			return;

		for(String cat : categories.getKeys(false)) {
			String name = categories.getString(cat + ".name");
			String customHeadUrl = categories.getString(cat + ".headURL");
			String reward = categories.getString(cat + ".type");
			RewardType rewardType = (RewardType) EasterEggs.getEnumByName(reward, RewardType.class, RewardType.TALER);
			int min = categories.getInt(cat + ".min");
			int max = categories.getInt(cat + ".max");

			List<ItemStack> items = new ArrayList<>();
			if(rewardType == RewardType.ITEMS) {
				for(String item : categories.getConfigurationSection(cat + ".items").getKeys(false)) {
					Material material = Material.getMaterial(item);
					if(material != null) {
						ItemStack itemStack = new ItemStack(material);
						ItemMeta meta = itemStack.getItemMeta();

						String displayName = categories.getString(cat + ".items." + item + ".name");
						if(displayName != null)
							meta.setDisplayName(displayName);

						List<String> lore = categories.getStringList(cat + ".items." + item + ".lore");
						if(!lore.isEmpty())
							meta.setLore(lore);

						if(categories.getBoolean(cat + ".items." + item + ".enchanted")) {
							itemStack.setItemMeta(meta);
							itemStack = ItemUtils.addEnchantments(itemStack);
							items.add(itemStack);
							continue;
						}

						List<String> enchantments = categories.getStringList(cat + ".items." + item + ".enchantments");
						Map<Enchantment, Integer> enchantmentMap = new HashMap<>();
						for(String enchantment : enchantments) {
							String[] split = enchantment.split(":");
							if(split.length == 2) {
								Enchantment ench = Registry.ENCHANTMENT.get(NamespacedKey.minecraft(split[0].toLowerCase()));
								if(ench != null) {
									int level = Integer.parseInt(split[1]);
									if(level > 0)
										enchantmentMap.put(ench, level);
								}
							}
						}
						itemStack.setItemMeta(meta);
						itemStack = ItemUtils.addEnchantments(itemStack, enchantmentMap, false);
						items.add(itemStack);
					}
				}
			}
			Category category = new Category(cat, name, customHeadUrl, rewardType, min, max, items);
			EggUtils.getCategories().add(category);
		}
	}
}