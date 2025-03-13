package de.cuzim1tigaaa.easter.utils.egg;

import lombok.Getter;
import org.bukkit.Location;

import java.util.UUID;

@Getter
public class Egg {

	private final UUID uniqueId;
	private final Location location;
	private final Category category;

	public Egg(UUID uniqueId, Location location, Category category) {
		this.uniqueId = uniqueId;
		this.location = location;
		this.category = category;
	}
}
