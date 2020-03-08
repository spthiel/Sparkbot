package me.macro.formatter;

import java.util.ArrayList;
import java.util.List;

public class Test {
    
    public static void main(String[] args) {
    
        List<String> code = new ArrayList<>();
        code.add("&bandprompt = \"for\"");
        code.add("PROMPT(&transmitterband,\"$$?\",%&bandprompt%,true)");
        
        FormatObject formatter = MacroFormatter.format(code, false, false, false);
        for (String s : formatter.getFormatted()) {
            System.out.println(s.replace("\t","\\t"));
        }
        
    }
    
}
