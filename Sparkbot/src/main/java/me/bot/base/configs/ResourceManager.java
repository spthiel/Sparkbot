package me.bot.base.configs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class ResourceManager {

	private String BASE_FOLDER;
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final HashMap<String, Language> langMapper = new HashMap<>();

	public ResourceManager(String folder, Language language) {
		this.BASE_FOLDER = folder;
	}

	public Map<String,Object> getConfig(String dir, String filename) {
		File folder = new File(BASE_FOLDER + dir);
		File fileToEdit = new File(BASE_FOLDER + dir + "/" + filename);
		if (!folder.exists())
			return new HashMap<>();

		if (!fileToEdit.exists())
			return new HashMap<>();

		try {
			return mapper.readValue(readFileAsString(fileToEdit), new TypeReference<Map<String, Object>>(){});
		} catch (IOException e) {
			e.printStackTrace();
			return new HashMap<>();
		}
	}

	public void writeConfig(String dir, String filename, Map<String,Object> config) {

		File folder = new File(BASE_FOLDER + dir);
		File fileToEdit = new File(BASE_FOLDER + dir + "/" + filename);
		if (!folder.exists())
			if(!folder.mkdirs())
				throw new RuntimeException("Couldn't create folder");

		if (!fileToEdit.exists()) {
			try {
				if(!fileToEdit.createNewFile())
					throw new RuntimeException("Couldn't create file");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			writeFile(mapper.writeValueAsString(config),fileToEdit);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
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
				out.append(line).append(" ");

		} catch (Exception ignored) {

		}

		return out.toString();

	}

	private static void writeFile(List<String> toWrite, File file) {

		try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {

			for (String line : toWrite) {
				out.write(line + "\n");
			}

		} catch (IOException ignored) {

		}
	}

	private static void writeFile(String toWrite, File file) {

		try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {

			out.write(toWrite);

		} catch (IOException ignored) {

		}
	}


//	public Language loadLanguage() {
//
//	}

}
