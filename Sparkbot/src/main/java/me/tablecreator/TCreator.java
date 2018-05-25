package me.tablecreator;

import java.util.List;

public class TCreator {

	public static Grid parseString(List<String> s,int startfrom) {

		if(s.size() > startfrom+1) {
			try {
				String[] line1 = s.get(startfrom).split(";");
				int width = Integer.parseInt(line1[0]);
				int height = Integer.parseInt(line1[1]);
				int beginindex = 0;
				if(line1.length > 2) {
					try {
						beginindex = Integer.parseInt(line1[2]);
					} catch(NumberFormatException ignored) {

					}
				}


				Grid out = new Grid(width,height,beginindex);

				for (int i = startfrom+1; i < s.size(); i++) {
					String line = s.get(i);
					out.putCell(line);
				}
				return out;
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error");
				return null;
			}
		}
		System.out.println("null");
		return null;
	}

}
