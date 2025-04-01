package de.cuzim1tigaaa.easter.files.data;

import de.cuzim1tigaaa.easter.EasterEggs;
import lombok.Getter;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class CommandFile {

	private final EasterEggs plugin;
	@Getter private final Set<String> commands;

	public CommandFile(EasterEggs plugin) {
		this.plugin = plugin;
		this.commands = new HashSet<>();
	}

	public void saveEasterEggs(String fileName) {
		try {
			java.io.File file = new java.io.File(plugin.getDataFolder(), fileName + ".txt");
			if(!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			PrintWriter out = new PrintWriter(new FileWriter(file));
			for(String command : commands)
				out.println(command);

			out.flush();
			out.close();
		}catch(IOException exception) {
			plugin.getLogger().log(Level.SEVERE, "An error occurred while saving the easter egg file!", exception);
		}
	}

}