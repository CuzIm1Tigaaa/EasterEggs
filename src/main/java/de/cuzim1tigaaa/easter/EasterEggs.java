package de.cuzim1tigaaa.easter;

import com.google.gson.Gson;
import de.cuzim1tigaaa.easter.commands.EasterCommand;
import de.cuzim1tigaaa.easter.db.mysql.SqlQueries;
import de.cuzim1tigaaa.easter.files.Config;
import de.cuzim1tigaaa.easter.files.Messages;
import de.cuzim1tigaaa.easter.files.data.DataFiles;
import de.cuzim1tigaaa.easter.listeners.EasterEvents;
import de.cuzim1tigaaa.easter.listeners.GuiEvents;
import de.cuzim1tigaaa.easter.utils.Highlight;
import de.cuzim1tigaaa.easter.utils.progress.ProgressUtils;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;

public class EasterEggs extends JavaPlugin {

	@Getter private static final Gson GSON = new Gson();

	private Highlight highlight;
	private DataFiles dataFiles;

	@Getter private SqlQueries sqlQueries;
	@Getter private ProgressUtils progressUtils;


	@Override
	public void onEnable() {
		this.dataFiles = new DataFiles(this);
		reload();
		this.dataFiles.loadEasterEggs();

		this.sqlQueries = new SqlQueries(this);
		this.progressUtils = new ProgressUtils(this);

		new EasterCommand(this);
		new EasterEvents(this);
		new GuiEvents(this);

		new ScoreboardReplacer(this);

		this.highlight = new Highlight(this);
		this.highlight.startHighlight();
	}

	@Override
	public void onDisable() {
		this.highlight.endHighlight();
		this.dataFiles.saveEasterEggs();
	}

	public void reload() {
		if(this.highlight != null)
			this.highlight.endHighlight();

		this.dataFiles.loadCategories();
		try {
			Config.getConfig().loadConfig(this);
			Messages.getMessages().loadMessages(this);
		} catch(IOException e) {
			this.getLogger().log(Level.SEVERE, "Failed to load config files", e);
		}

		if(this.highlight != null)
			this.highlight.startHighlight();
	}

	public static <T extends Enum<T>> Enum<T> getEnumByName(String type, Class<T> clazz, T def) {
		return Arrays.stream(clazz.getEnumConstants()).filter(trackType ->
				trackType.name().equalsIgnoreCase(type)).findFirst().orElse(def);
	}
}