package me.macro.formatter;

@SuppressWarnings("unused")
public enum ControlElement {

	IF(new String[]{"if"},new String[]{"elseif","else"},new String[]{"endif"}),
	ADVANCED_IF(new String[]{"ifbeginswith","ifcontains","ifendswith","ifmatches"},new String[]{"elseif","else"},new String[]{"endif"}),
	FOR(new String[]{"for","foreach"},new String[]{"next"}),
	UNSAFE(new String[]{"unsafe"},new String[]{"endunsafe"}),
	LOOP(new String[]{"do"},new String[]{"loop","while","until"})
	
	
	;
	private final String[]
		open,
		middle,
		close;
	
	ControlElement(String[] open,String[] close) {
		this.open = open;
		this.middle = null;
		this.close = close;
	}

	ControlElement(String[] open, String[] middle, String[] close) {
		this.open = open;
		this.middle = middle;
		this.close = close;
	}	
	
	public static ControlElement isStart(String line) {
		
		System.out.println("DEBUG2: " + line);
		for(ControlElement element : ControlElement.values())
			for(String str : element.open)
				if(line.toLowerCase().startsWith(str.toLowerCase()))
					return element;
		return null;
	}

	public static boolean canBeMiddle(String cmd) {
		for(ControlElement element : ControlElement.values())
			if(element.middle != null)
				for(String str : element.middle)
					if(str.toLowerCase().equals(cmd.toLowerCase()))
						return true;
		return false;
	}

	public static boolean canBeEnd(String cmd) {
		for(ControlElement element : ControlElement.values())
			for(String str : element.close)
				if(str.toLowerCase().equals(cmd.toLowerCase()))
					return true;
		return false;
	}

	public boolean isMiddle(String line) {
		if(middle == null)
			return false;
		for(String str : middle)
			if(line.toLowerCase().startsWith(str.toLowerCase()))
				return true;
		return false;
	}
	
	public boolean isEnd(String line) {
		for(String str : close)
			if(line.toLowerCase().startsWith(str.toLowerCase()))
				return true;
		return false;
	}
	
}
