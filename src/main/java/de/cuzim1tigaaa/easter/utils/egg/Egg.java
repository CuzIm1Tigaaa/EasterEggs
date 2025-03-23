package de.cuzim1tigaaa.easter.utils.egg;

import lombok.Getter;
import org.bukkit.Location;

import java.util.UUID;

@Getter
public class Egg {

	private final UUID uniqueId;
	private final Location location;
	private final String categoryId;

	public Egg(UUID uniqueId, Location location, String categoryId) {
		this.uniqueId = uniqueId;
		this.location = location;
		this.categoryId = categoryId;
	}

	public Egg(Location location, String categoryId) {
		this(UUID.randomUUID(), location, categoryId);
	}

	public Category getCategory() {
		return EggUtils.getCategoryById(categoryId);
	}
}