package de.cuzim1tigaaa.easter;

import de.cuzim1tigaaa.easter.commands.EasterCommand;
import de.cuzim1tigaaa.easter.files.Messages;
import de.cuzim1tigaaa.easter.listeners.EasterEvents;
import de.cuzim1tigaaa.easter.listeners.GuiEvents;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Level;

public class EasterEggs extends JavaPlugin {

	@Override
	public void onEnable() {
		reload();

		new EasterCommand(this);
		new EasterEvents(this);
		new GuiEvents(this);
	}

	public void reload() {
		try {
			Messages.getInstance().loadMessages(this);
		} catch(IOException e) {
			this.getLogger().log(Level.SEVERE, "Failed to load messages.yml", e);
		}
	}
}
