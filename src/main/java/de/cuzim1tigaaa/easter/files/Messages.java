package de.cuzim1tigaaa.easter.files;

import de.cuzim1tigaaa.colorlib.ColorLib;
import de.cuzim1tigaaa.colorlib.gradients.Gradient;
import de.cuzim1tigaaa.colorlib.gradients.LinearGradient;
import de.cuzim1tigaaa.easter.EasterEggs;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class Messages {

	private static final EasterEggs PLUGIN = EasterEggs.getPlugin(EasterEggs.class);
	private static Messages messages;

	private final Set<Gradient> gradients = Set.of(
			new LinearGradient()
	);

	public static Messages getMessages() {
		if(messages == null)
			messages = new Messages();
		return messages;
	}

	private FileConfiguration messagesFile;

	public void send(CommandSender sender, String path, Object... replace) {
		String message = get(path, replace);
		if(message == null) {
			PLUGIN.getLogger().warning("Error: Path '" + path + "' does not exist!");
			return;
		}
		sender.sendMessage(message);
	}

	public String get(String path, Object... replace) {
		if(messagesFile == null || messagesFile.get(path) == null) {
			return null;
		}
		String string = messagesFile.getString(path);
		for(int i = 0; i < replace.length; i++) {
			final String target = (String) replace[i];
			final String replacement = replace[i += 1].toString();

			if(target == null || replacement == null) string = null;
			if(string != null) string = string.replace("%" + target + "%", replacement);
		}
		if(string == null) {
			PLUGIN.getLogger().warning("Error: Path '" + path + "' does not exist!");
			return null;
		}
		return ColorLib.format(string, replace);
	}

	public void loadMessages(EasterEggs plugin) throws IOException {
		File messageFile = new File(plugin.getDataFolder(), "messages.yml");
		if(!messageFile.exists()) {
			messagesFile = new YamlConfiguration();
			messagesFile.save(messageFile);
		}
		messagesFile = YamlConfiguration.loadConfiguration(messageFile);
		messagesFile.options().setHeader(List.of("Alle Chatnachrichten, die das Plugin nutzt"));

		set(Paths.MESSAGES_SENDER,              "&cDu musst ein Spieler sein, um diesen Befehl ausführen zu können!");
		set(Paths.MESSAGES_PERMISSION,          "&cDu darfst diesen Befehl nicht benutzen!");
		set(Paths.MESSAGES_SYNTAX,              "&cFalsche Benutzung! &8» &c%SYNTAX%");
		set(Paths.MESSAGES_RELOAD,              "&7Das Plugin wurde neu geladen! &8[&9%TIME%ms&8]");
		set(Paths.MESSAGES_GUI_NO_DATA,         "&cEs wurden keine Daten gefunden!");

		messagesFile.save(messageFile);
	}
	private void set(String path, String message) {
		messagesFile.set(path, messagesFile.getString(path, message));
	}

	public static String capitalizeFully(String s) {
		s = s.replace("_", " ").toLowerCase();
		String[] words = s.split(" ");
		StringBuilder sb = new StringBuilder();
		for(String word : words) {
			sb.append(word.substring(0, 1).toUpperCase()).append(word.substring(1)).append(" ");
		}
		return sb.toString().trim();
	}
}
