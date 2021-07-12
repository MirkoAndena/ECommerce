package ecommerce.database.beans;

import java.util.List;

public class Range {
	public Integer max;
	public Float price;
	
	public Range(Integer max, Float price) {
		this.max = max;
		this.price = price;
	}

	private String compile(int last) { 
		return "da " + last + " a " + this.max + " €" + this.price;
	}
	
	public static String compileRangeList(List<Range> ranges) {
		if (ranges.size() == 0) return "ERROR";
		int last = 1;
		float maxPrice = -1;
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(ranges.get(0).compile(last));
		
		for (int i = 0; i < ranges.size(); i++) {
			if (ranges.get(i).max == null)
				maxPrice = ranges.get(i).price;
			else {
				stringBuilder.append(", " + ranges.get(i).compile(last));
				last = ranges.get(i).max;
			}
		}
		
		if (maxPrice > -1)
			if (ranges.size() == 1)
				return "Spedizione sempre a €" + maxPrice;
			else
				return "Spedizione " + stringBuilder.toString() + ", da " + last + " in su €" + maxPrice;
		else
			return "ERROR";
	}
}