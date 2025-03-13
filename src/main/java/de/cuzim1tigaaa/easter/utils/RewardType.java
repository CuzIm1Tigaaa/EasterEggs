package de.cuzim1tigaaa.easter.utils;

import lombok.Getter;

@Getter
public enum RewardType {

	COINS("Coins", "/coins give %player% %amount%"),
	ITEMS("", "/give %player% minecraft:%item% %amount%"),
	TALER("Taler", "/money give %player% %amount%");

	private final String name, command;

	RewardType(String name, String command) {
		this.name = name;
		this.command = command;
	}
}
