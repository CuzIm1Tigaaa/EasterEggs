package de.cuzim1tigaaa.easter;

import de.cuzim1tigaaa.easter.commands.EasterCommand;
import de.cuzim1tigaaa.easter.files.DataFiles;
import de.cuzim1tigaaa.easter.files.Messages;
import de.cuzim1tigaaa.easter.listeners.EasterEvents;
import de.cuzim1tigaaa.easter.listeners.GuiEvents;
import de.cuzim1tigaaa.easter.utils.Highlight;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Level;

public class EasterEggs extends JavaPlugin {

	private Highlight highlight;
	private DataFiles dataFiles;

	@Override
	public void onEnable() {
		this.dataFiles = new DataFiles(this);
		reload();
		this.dataFiles.loadEasterEggs();

		new EasterCommand(this);
		new EasterEvents(this);
		new GuiEvents(this);

		this.highlight = new Highlight(this);
		this.highlight.startHighlight();
	}

	@Override
	public void onDisable() {
		this.highlight.endHighlight();
		this.dataFiles.saveEasterEggs();
	}

	public void reload() {
		this.dataFiles.loadCategories();
		try {
			Messages.getMessages().loadMessages(this);
		} catch(IOException e) {
			this.getLogger().log(Level.SEVERE, "Failed to load messages.yml", e);
		}
	}
}