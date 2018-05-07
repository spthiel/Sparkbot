package me.macro;

public enum ControlElement {

	IF(new String[]{"if"},new String[]{"elseif","else"},new String[]{"endif"}),
	ADVANCEDIF(new String[]{"ifbeginswith","ifcontains","ifendswith","ifmatches"},new String[]{"elseif","else"},new String[]{"endif"}),
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
	
	public static ControlElement isStart(String cmd) {
		for(ControlElement element : ControlElement.values())
			for(String str : element.open)
				if(str.equals(cmd))
					return element;
		return null;
	}

	public static boolean canBeMiddle(String cmd) {
		for(ControlElement element : ControlElement.values())
			if(element.middle != null)
				for(String str : element.middle)
					if(str.equals(cmd))
						return true;
		return false;
	}

	public static boolean canBeEnd(String cmd) {
		for(ControlElement element : ControlElement.values())
			for(String str : element.close)
				if(str.equals(cmd))
					return true;
		return false;
	}
	
	public boolean isMiddle(String cmd) {
		if(middle == null)
			return false;
		for(String str : middle)
			if(cmd.equalsIgnoreCase(str))
				return true;
		return false;
	}
	
	public boolean isEnd(String cmd) {
		for(String str : close)
			if(cmd.equalsIgnoreCase(str))
				return true;
		return false;
	}
	
}
