package me.macro.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import me.main.utils.HTTP;

public class MacroCache {
    
    private static final String       URL    = "https://beta.mkb.gorlem.ml/api/docs";
    private static final ObjectMapper mapper = new ObjectMapper();
    
    private List<ResponseStruct> cache;
    
    public MacroCache() {
    
        try {
            String everything = HTTP.getAsString(URL);
            cache = mapper.readValue(everything, mapper.getTypeFactory().constructCollectionType(List.class, ResponseStruct.class));
            System.out.println(cache);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
