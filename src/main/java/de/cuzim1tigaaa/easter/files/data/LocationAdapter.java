package de.cuzim1tigaaa.easter.files.data;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.lang.reflect.Type;
import java.util.UUID;

public class LocationAdapter implements JsonSerializer<Location>, JsonDeserializer<Location> {

	@Override
	public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		UUID world = UUID.fromString(jsonObject.get("world").getAsString());
		double x = jsonObject.get("x").getAsDouble();
		double y = jsonObject.get("y").getAsDouble();
		double z = jsonObject.get("z").getAsDouble();

		return new Location(Bukkit.getWorld(world), x, y, z);
	}

	@Override
	public JsonElement serialize(Location src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("world", src.getWorld().getUID().toString());
		jsonObject.addProperty("x", src.getX());
		jsonObject.addProperty("y", src.getY());
		jsonObject.addProperty("z", src.getZ());
		return jsonObject;
	}
}