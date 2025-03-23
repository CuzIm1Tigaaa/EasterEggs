package de.cuzim1tigaaa.easter.utils.egg;

import lombok.Getter;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class EggUtils {

	@Getter
	private static final Set<Egg> eggs = new HashSet<>();
	@Getter
	private static final Set<Category> categories = new HashSet<>();

	public static Egg getEggById(UUID id) {
		return eggs.stream().filter(e -> e.getUniqueId().equals(id))
				.findFirst().orElse(null);
	}

	public static Egg getEggByLocation(Location location) {
		return eggs.stream().filter(e -> e.getLocation().equals(location))
				.findFirst().orElse(null);
	}

	public static Set<Egg> getEggsByCategory(Category category) {
		return eggs.stream().filter(e -> e.getCategory().equals(category))
				.collect(Collectors.toSet());
	}

	public static Category getCategoryById(String id) {
		return categories.stream().filter(cat -> cat.getId().equals(id))
				.findFirst().orElse(null);
	}
}