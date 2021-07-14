package ecommerce.database.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Range {
	public Integer max;
	public Float price;
	
	public Range(Integer max, Float price) {
		this.max = max;
		this.price = price;
	}

	private String compile(int first) { 
		return "da " + first + " a " + this.max + " articoli €" + this.price;
	}
	
	public static String compileRangeList(List<Range> ranges) {
		
		if (ranges.size() == 0) 
			return "gratuita";
		
		if (ranges.size() == 1 && ranges.get(0).max == null)
			return "€" + ranges.get(0).price;
		
		int lastOfLast = 1;
		List<String> list = new ArrayList<String>();
		for (Range range : ranges) {
			if (range.max != null) {
				list.add(range.compile(lastOfLast));
				lastOfLast = range.max + 1;
			}
			else {
				list.add("da " + lastOfLast + " in su €" + range.price);
			}
		}
		
		return String.join(", ", list);
	}
}