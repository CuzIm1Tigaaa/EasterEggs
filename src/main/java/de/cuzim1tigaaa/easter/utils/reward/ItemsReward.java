package de.cuzim1tigaaa.easter.utils.reward;

import com.google.gson.JsonObject;
import de.cuzim1tigaaa.colorlib.ColorLib;
import de.cuzim1tigaaa.easter.files.Messages;
import de.cuzim1tigaaa.easter.utils.RewardType;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Getter
public class ItemsReward extends Reward {

	private final ItemStack itemStack;

	public ItemsReward(ItemStack itemStack, int amount) {
		super(RewardType.ITEMS, amount);
		this.itemStack = itemStack;
	}

	@Override
	public String toBase64() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("rewardType", rewardType.name());
		jsonObject.addProperty("amount", amount);
		jsonObject.addProperty("itemStack", itemStackToBase64(itemStack));
		return Base64.getEncoder().encodeToString(jsonObject.toString().getBytes());
	}

	@Override
	public String toString() {
		String name = itemStack.getItemMeta().getDisplayName();
		if(name.isEmpty())
			name = Messages.capitalizeFully(itemStack.getType().name());
		return ColorLib.format("&7Du hast &a%dx %s &7gefunden!", amount, name);
	}

	public String itemStackToBase64(ItemStack item) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
			dataOutput.writeObject(item);
			dataOutput.close();
			return Base64.getEncoder().encodeToString(outputStream.toByteArray());
		}catch(IOException e) {
			return null;
		}
	}
}