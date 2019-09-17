package me.macro.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;

import java.util.List;

import me.main.utils.HTTP;

public class MacroCache {

    private static final String URL = "https://beta.mkb.gorlem.ml/";
    private static final String API = "api/docs";
    private static final ObjectMapper mapper = new ObjectMapper();

    private List<ResponseStruct> cache;
    
    public MacroCache() {

        try {
            String everything = HTTP.getAsString(URL + API);
            try {
                
                cache = mapper.readValue(everything, mapper.getTypeFactory().constructCollectionType(List.class, ResponseStruct.class));
            } catch(Exception e) {
                System.out.println("[EVERYTHING]: " + everything);
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public Mono<ResponseStruct> getEntry(String name) {
        return getEntry(null, name);
    }

    public Mono<ResponseStruct> getEntry(String type, String name) {

        ResponseStruct.Types types = null;
        if(type != null) {
            types = ResponseStruct.Types.getTypeOfString(type);
        }
        for (ResponseStruct struct : cache) {
            if(struct.name.equalsIgnoreCase(name) && typeAccepts(types, struct)) {
                if(struct.fullyCached) {
                    return Mono.just(struct);
                } else {
                    return Mono.fromCallable(() -> {
                        try {
                            String response = HTTP.getAsString(URL + struct.resource);
                            return struct.overwriteNull(mapper.readValue(response, ResponseStruct.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                            return struct;
                        }
                    });
                }
            }
        }
        return Mono.just(ResponseStruct.NULL);
    }

    private static boolean typeAccepts(ResponseStruct.Types type, ResponseStruct struct) {
        if(type == null) {
            return true;
        }
        return type.equals(struct.type);
    }

}
