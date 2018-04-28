package me.main;

@SuppressWarnings("unused")
public class Messages {

	public static String getMessage(String key) {
		String message = "";



		return format(message);
	}

	public static String format(String s) {
		for(Emoji e : Emoji.values()) {
			s = s.replace(e.getName(),e.toString());
		}
		return s;
	}

	public enum Emoji {

		RED_CROSS(":red_cross:",398120014974287873L);

		private static final String FORMAT = "<$name$id>";

		private String name;
		private Long id;

		Emoji(String name,Long id) {
			this.name = name;
			this.id = id;
		}

		public Long getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return FORMAT
					.replace("$name",name)
					.replace("$id","" + id);
		}
	}

}