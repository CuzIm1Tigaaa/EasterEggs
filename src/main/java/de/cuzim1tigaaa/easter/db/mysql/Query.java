package de.cuzim1tigaaa.easter.db.mysql;

public class Query {

	public static final String CREATE_TABLE =
			"""
			CREATE TABLE IF NOT EXISTS `%s` (
				`UUID` VARCHAR(36) NOT NULL,
				`EGG_ID` VARCHAR(36) NOT NULL,
				`FOUND_AT` TIMESTAMP NOT NULL,
				`REWARD` VARCHAR(150) NOT NULL,
				PRIMARY KEY (`UUID`, `EGG_ID`)
			)
			""";

	public static final String GET_PLAYER_PROGRESS =
			"""
			SELECT *
			FROM `%s`
			WHERE `UUID` = ?
			""";

}