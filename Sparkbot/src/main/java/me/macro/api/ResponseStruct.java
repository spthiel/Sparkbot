package me.macro.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.TextFormatter;

import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ResponseStruct {
    
    private transient static ObjectMapper mapper = new ObjectMapper();
    @SuppressWarnings("StaticInitializerReferencesSubClass")
    public transient static  NullStruct   NULL   = new NullStruct();

    ResponseStruct() {}

    public ResponseStruct(@JsonProperty("type") String _type, @JsonProperty("changelog") List<Changelog> _changelog) {
        
        type = Types.getTypeByName(_type);
        fullyCached = false;
        if(_changelog != null) {
            _changelog.sort((c1, c2) -> {
                int o1 = Integer.compare(c1.version.major, c2.version.major);
                if (o1 != 0) {
                    return o1;
                }
                int o2 = Integer.compare(c1.version.minor, c2.version.minor);
                if (o2 != 0) {
                    return o2;
                }
                return Integer.compare(c1.version.patch, c2.version.patch);
            });
        }
        changelog = _changelog;
    }
    
    public Types   type;
    public boolean fullyCached;
    
    public String       resource;
    public String       name         = "missing";
    public String       extendedName = "missing";
    public String       category;
    public String       permission;
    public String       description;
    public String       example;
    @JsonProperty("sinceVersion")
    public SinceVersion version;
    public List<Related> related;
    public List<Link> links;
    public List<Changelog> changelog;

    public ResponseStruct overwriteNull(ResponseStruct struct) {
        
        fullyCached = true;
        
        if (type == null) { type = struct.type; }
        if (resource == null) { resource = struct.resource; }
        if (name.equals("missing")) { name = struct.name; }
        if (extendedName.equals("missing")) { extendedName = struct.extendedName; }
        if (category == null) { category = struct.category; }
        if (permission == null) { permission = struct.permission; }
        if (description == null) { description = struct.description; }
        if (example == null) { example = struct.example; }
        if (version == null) { version = struct.version; }
        if (related == null) { related = struct.related; }
        if (links == null) { links = struct.links; }
        if (changelog == null) { changelog = struct.changelog; }

        return this;
    }
    
    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class Related {
        
        public Related(@JsonProperty("type") String _type) {
        
            type = Types.getTypeByName(_type);
        }
        
        public Types type;
        public String name;
        public String resource;
        
    }
    
    public static class SinceVersion {
        
        public SinceVersion(@JsonProperty("name") String _name, @JsonProperty("major") int _major, @JsonProperty("minor") int _minor, @JsonProperty("patch") int _patch) {
            name = "v" + _major + "." + _minor + "." + _patch;
            major = _major;
            minor = _minor;
            patch = _patch;
        }
        
        public String name;
        public String minecraft;
        public String url;
        public int api;
        public int    major, minor , patch;

        @Override
        public String toString() {
            return name;
        }
    }
    
    public static class Link {
        
        public String url;
        public String title;
        
    }

    public static class Changelog {

        public SinceVersion version;
        public String type;
        public String message;

    }
    
    public enum Types {
        
        ACTION("action", "actions"),
        VARIABLE("variable", "variables"),
        EVENT("event", "events"),
        ITERATOR("iterator", "iterators"),
        COMMAND("command","commands"),
        PARAMETER("parameter", "parameters");
        
        private final String name;
        private final String url;
        
        Types(String name, String url) {
            
            this.name = name;
            this.url = url;
        }
        
        public String getName() {
            
            return name;
        }
        
        public String getUrl() {
            
            return url;
        }
        
        private static Types getTypeByName(String name) {
            
            for (Types t : Types.values()) {
                if (t.getName().equalsIgnoreCase(name)) {
                    return t;
                }
            }
            return null;
        }

        public static Types getTypeOfString(String str) {
            for(Types type : Types.values()) {
                if(type.name().toLowerCase().startsWith(str.toLowerCase())) {
                    return type;
                }
            }
            throw new MacroTypeNotFoundException();
        }
    }
    
    @Override
    public String toString() {
        
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error";
        }
    }
}
