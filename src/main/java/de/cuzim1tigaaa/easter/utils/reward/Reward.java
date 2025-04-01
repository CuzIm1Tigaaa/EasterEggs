package de.cuzim1tigaaa.easter.utils.reward;

import com.google.gson.*;
import de.cuzim1tigaaa.colorlib.ColorLib;
import de.cuzim1tigaaa.easter.utils.RewardType;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Getter
public class Reward {

	private static final Gson gson = new GsonBuilder()
			.registerTypeAdapter(Reward.class, new RewardDeserializer())
			.create();

	protected final RewardType rewardType;
	protected final int amount;

	public Reward(RewardType rewardType, int amount) {
		this.rewardType = rewardType;
		this.amount = amount;
	}

	public static Reward fromBase64(String base64) {
		String json = new String(Base64.getDecoder().decode(base64));
		JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
		RewardType rewardType = RewardType.valueOf(jsonObject.get("rewardType").getAsString());
		int amount = jsonObject.get("amount").getAsInt();

		if(rewardType == RewardType.ITEMS) {
			ItemStack itemStack = itemStackFromBase64(jsonObject.get("itemStack").getAsString());
			return new ItemsReward(itemStack, amount);
		}
		return new Reward(rewardType, amount);
	}

	public String toBase64() {
		String json = gson.toJson(this);
		return Base64.getEncoder().encodeToString(json.getBytes());
	}

	@Override
	public String toString() {
		return ColorLib.format("&7Du hast &a%d %s &7gefunden!", amount, rewardType.getName());
	}

	public static ItemStack itemStackFromBase64(String base64) {
		try {
			byte[] data = Base64.getDecoder().decode(base64);
			ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			ItemStack item = (ItemStack) dataInput.readObject();
			dataInput.close();
			return item;
		} catch (IOException | ClassNotFoundException e) {
			return null;
		}
	}
}