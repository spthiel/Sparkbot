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
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
    private       String                                 BASE_FOLDER;
    private final Class<? extends Language>              languageClass;
    private final HashMap<String, Language>              langMapper   = new HashMap<>();
    private final HashMap<File, HashMap<String, Object>> configMapper = new HashMap<>();
    
    public ResourceManager(String folder, Class<? extends Language> language) {
        
        this.BASE_FOLDER = folder;
        this.languageClass = language;
    }
    
    public String getBaseFolder() {
        
        return BASE_FOLDER;
    }
    
    public HashMap<String, Object> getConfig(String dir, String filename) {
        
        
        File folder     = new File(BASE_FOLDER + dir);
        File fileToEdit = new File(BASE_FOLDER + dir + "/" + filename);
        if (configMapper.containsKey(fileToEdit)) {
            return configMapper.get(fileToEdit);
        }
        if (!folder.exists()) {
            return new HashMap<>();
        }
    
        if (!fileToEdit.exists()) {
            return new HashMap<>();
        }
        
        try {
            HashMap<String, Object> map = mapper.readValue(
                    readFileAsString(fileToEdit),
                    new TypeReference<Map<String, Object>>() {
                    }
                                                          );
            configMapper.put(fileToEdit, map);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
    
    public void saveAll() {
        
        for (File location : configMapper.keySet()) {
            
            File folder = location.getParentFile();
            
            if (!folder.exists()) {
                if (!folder.mkdirs()) {
                    throw new RuntimeException("Couldn't create folder");
                }
            }
            
            if (!location.exists()) {
                try {
                    if (!location.createNewFile()) {
                        throw new RuntimeException("Couldn't create file");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            try {
                writeFile(mapper.writeValueAsString(configMapper.get(location)), location);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void saveConfig(String location, HashMap<String, Object> config) {
    
    }
    
    public void writeConfig(String dir, String filename, HashMap<String, Object> config) {
        
        File fileToEdit = new File(BASE_FOLDER + dir + "/" + filename);
        configMapper.put(fileToEdit, config);
        
    }
    
    public static List<String> readFileAsList(File file) {
        
        List<String> out = new ArrayList<>();
        
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            
            String line;
            
            while ((line = in.readLine()) != null) {
                out.add(line);
            }
            
        } catch (Exception ex) {
        
        }
        
        return out;
        
    }
    
    public static String readFileAsString(File file) {
        
        StringBuilder out = new StringBuilder();
        
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            
            String line;
            
            while ((line = in.readLine()) != null)
                out.append(line).append(" ");
            
        } catch (Exception ignored) {
        
        }
        
        return out.toString();
        
    }
    
    public static void writeFile(List<String> toWrite, File file) {
        
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
            
            for (String line : toWrite) {
                out.write(line + "\n");
            }
            
        } catch (IOException ignored) {
        
        }
    }
    
    public static void writeFile(String toWrite, File file) {
        
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
            
            out.write(toWrite);
            
        } catch (IOException ignored) {
        
        }
    }
    
    
    public Language loadLanguage(String identifier) {
        
        if (langMapper.containsKey(identifier)) {
            return langMapper.get(identifier);
        }
        
        String dir      = "/language";
        String filename = identifier + ".lang";
        
        File folder = new File(BASE_FOLDER + dir);
        File file   = new File(BASE_FOLDER + dir, filename);
        
        if (!folder.exists()) {
            folder.mkdirs();
        }
        
        if (!file.exists()) {
            try {
                Language lang = languageClass.newInstance();
                writeFile(mapper.writeValueAsString(lang), file);
            } catch (InstantiationException | IllegalAccessException | JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            
            try {
                Language lang = mapper.readValue(readFileAsString(file), languageClass);
                langMapper.put(identifier, lang);
                return lang;
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
        try {
            return languageClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    
    }
    
}
