package me.bot.base.configs;

import org.json.JSONObject;

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

		JSONObject object = new JSONObject(readFileAsString(fileToEdit));


		return object.get(key);

	}

	public boolean isSet(String dir, String filename, String key) {

		File fileToEdit = new File(BASE_FOLDER + dir + "/" + filename);

		if (!fileToEdit.exists())
			return false;

		return new JSONObject(readFileAsString(fileToEdit)).has(key);

	}

	public JSONObject getConfig(String dir, String filename) {
		File folder = new File(BASE_FOLDER + dir);
		File fileToEdit = new File(BASE_FOLDER + dir + "/" + filename);
		if (!folder.exists())
			return new JSONObject();

		if (!fileToEdit.exists())
			return new JSONObject();

		return new JSONObject(readFileAsString(fileToEdit));
	}

	public void writeConfig(String dir, String filename, JSONObject config) {

		File folder = new File(BASE_FOLDER + dir);
		File fileToEdit = new File(BASE_FOLDER + dir + "/" + filename);
		if (!folder.exists())
			folder.mkdirs();

		if (!fileToEdit.exists()) {
			try {
				fileToEdit.createNewFile();
			} catch (IOException e) {
			}
		}

		writeFile(config.toString(),fileToEdit);
	}

	private List<String> readFileAsList(File file) {
		List<String> out = new ArrayList<>();

		try (BufferedReader in = new BufferedReader(new FileReader(file))) {

			String line;

			while ((line = in.readLine()) != null)
				out.add(line);

		} catch (Exception ex) {

		}

		return out;

	}

	private String readFileAsString(File file) {
		StringBuilder out = new StringBuilder();

		try (BufferedReader in = new BufferedReader(new FileReader(file))) {

			String line;

			while ((line = in.readLine()) != null)
				out.append(line + " ");

		} catch (Exception ex) {

		}

		return out.toString();

	}

	private void writeFile(List<String> toWrite, File file) {

		try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {

			for (String line : toWrite) {
				out.write(line + "\n");
			}

		} catch (IOException e) {

		}
	}

	private void writeFile(String toWrite, File file) {

		try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {

			out.write(toWrite);

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

			JSONObject c = new JSONObject();
			for (String key : toEdit.keySet()) {
				c.put(key, toEdit.get(key));
			}
			writeFile(c.toString(),fileToEdit);

		} else {

			JSONObject c = new JSONObject(readFileAsString(fileToEdit));

			for (String key : toEdit.keySet()) {
				c.put(key, toEdit.get(key));
			}

			writeFile(c.toString(), fileToEdit);

		}

	}

}
