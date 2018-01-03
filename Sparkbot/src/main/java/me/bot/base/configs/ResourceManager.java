package me.bot.base.configs;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResourceManager {

	private String BASE_FOLDER;

	public ResourceManager(String folder) {
		this.BASE_FOLDER = folder;
	}

	public void setValue(String dir, String filename, String key, Object value) {

		HashMap<String, Object> toEdit = new HashMap<>();
		toEdit.put(key, value);

		editKey(dir, filename, toEdit);

	}

	public void setValues(String dir, String filename, HashMap<String, Object> toEdit) {
		editKey(dir, filename, toEdit);
	}

	public Object getValue(String dir, String filename, String key) {
		File folder = new File(BASE_FOLDER + dir);
		File fileToEdit = new File(BASE_FOLDER + dir + "/" + filename);
		if (!folder.exists())
			return null;

		if (!fileToEdit.exists())
			return null;

		return new Config(readFile(fileToEdit)).getValue(key);

	}

	private List<String> readFile(File file) {
		List<String> out = new ArrayList<>();

		try (BufferedReader in = new BufferedReader(new FileReader(file))) {

			String line;

			while ((line = in.readLine()) != null)
				out.add(line);

		} catch (Exception ex) {

		}

		return out;

	}

	private void writeFile(List<String> toWrite, File file) {

		try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {

			for (String line : toWrite)
				out.write(line + "\n");

		} catch (IOException e) {

		}
	}

	private void editKey(String dir, String filename, HashMap<String, Object> toEdit) {
		File folder = new File(BASE_FOLDER + dir);
		File fileToEdit = new File(BASE_FOLDER + dir + "/" + filename);
		if (!folder.exists())
			folder.mkdirs();

		if (!fileToEdit.exists()) {
			try {
				fileToEdit.createNewFile();
			} catch (IOException e) {
			}

			try (BufferedWriter out = new BufferedWriter(new FileWriter(fileToEdit))) {

				Config c = new Config();
				for (String key : toEdit.keySet())
					c.addOrResetKey(key, toEdit.get(key));
				for (String line : c.toFileFormat())
					out.write(line + "\n");
			} catch (IOException e) {

			}

		} else {

			Config c = new Config(readFile(fileToEdit));

			for (String key : toEdit.keySet())
				c.addOrResetKey(key, toEdit.get(key));

			writeFile(c.toFileFormat(), fileToEdit);

		}

	}

}
