package de.cuzim1tigaaa.easter.utils.reward;

import com.google.gson.*;
import de.cuzim1tigaaa.easter.EasterEggs;
import de.cuzim1tigaaa.easter.utils.RewardType;

import java.lang.reflect.Type;

public class RewardDeserializer implements JsonDeserializer<Reward> {

	@Override
	public Reward deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject object = json.getAsJsonObject();
		RewardType rewardType = RewardType.valueOf(object.get("rewardType").getAsString());
		Class<? extends Reward> rewardClass = rewardType == RewardType.ITEMS ? ItemsReward.class : Reward.class;
		return EasterEggs.getGSON().fromJson(json, rewardClass);
	}
}