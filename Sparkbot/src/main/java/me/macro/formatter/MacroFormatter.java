package me.macro.formatter;

import java.util.Iterator;
import java.util.Stack;

import me.macro.formatter.chain.Line;
import me.macro.formatter.chain.VariableType;

public class MacroFormatter {
 
    private int index = 0;
    private String input;
    private boolean
        indepthcheck,
        caps;
    
//    private final Stack<Integer> ifStack  = new Stack<>();
//    private final Stack<Integer> forStack = new Stack<>();
    private final Stack<ControlElement> controlElements = new Stack<>();
    
    public MacroFormatter(String input, boolean indepthcheck, boolean caps) {
        this.indepthcheck = indepthcheck;
        this.input = input;
        this.caps = caps;
    }
    
    public FormatObject format() {
    
        ParseHelper helper = new ParseHelper(input.toCharArray());
        FormatObject   out   = new FormatObject(indepthcheck);
        Iterator<Line> lines = helper.getChain().iterator();
        
        StringBuilder indentation = new StringBuilder();
        
        StringBuilder b = null;
        int lineNumber = 0;
        boolean lastWasEmpty = true;
        while (lines.hasNext()) {
            Line line = lines.next();
            boolean        indentAfter = false;
            if(line.hasAction()) {
                String         action      = line.getAction().toString();
                ControlElement ce;
                if ((ce = ControlElement.isStart(action)) != null) {
                    controlElements.push(ce);
                    indentAfter = true;
                } else if (!controlElements.empty() && controlElements.peek().isMiddle(action)) {
                    indentation.deleteCharAt(0);
                    indentAfter = true;
                } else if (!controlElements.empty() && controlElements.peek().isEnd(action)) {
                    indentation.deleteCharAt(0);
                    controlElements.pop();
                }
                //            if(action.startsWith("if")) {
                //                ifStack.push(lineNumber);
                //            }
                //            if(action.startsWith("for")) {
                //                forStack.push(lineNumber);
                //            }
    
            }
            if(line.isEmpty()) {
                if(!lastWasEmpty) {
                    out.addLine("");
                }
                lastWasEmpty = true;
            } else {
                out.addLine(buildLine(indentation.toString(), line));
                lastWasEmpty = false;
            }
            if (indentAfter) {
                indentation.append('\t');
            }
            lineNumber++;
        }
        
        return out;
    }
    
    private String buildLine(String indentation, Line line) {
        StringBuilder out = new StringBuilder(indentation);
        if(line.hasVariable()) {
            out.append(line.getVariable()).append(" = ");
        }
        if(line.hasAction()) {
            String action = line.getAction().toString();
            if(caps) {
                action = action.toUpperCase();
            } else {
                action = action.toLowerCase();
            }
            out.append(action);
            if(line.getAction().hasBrackets()) {
                out.append('(').append(String.join(",",line.getParameters())).append(")");
            }
        }
        if(line.hasRightside()) {
            if(line.getVariable().getType().equals(VariableType.STRING)) {
                String rightside = line.getRightside();
                if(rightside.matches("\".*\"")) {
                    out.append(rightside);
                } else {
                    out.append('"').append(rightside).append('"');
                }
            } else {
                out.append(line.getRightside());
            }
        }
        if(line.isMeta()) {
            out.append(line.getMeta());
        }
        return out.toString();
    }
    
}
