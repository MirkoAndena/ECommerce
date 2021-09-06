package ecommerce.database.dto;

public class Range {
	public int start;
	public Integer end;
	public Float price;
	
	public Range(int start, Integer end, Float price) {
		this.start = start;
		this.end = end;
		this.price = price;
	}

	@Override
	public String toString() { 
		if (end != null)
			return String.format("da %d a %d articoli %.2f €", start, end, price);
		else
			return String.format("da %d in su %.2f €", start, price);
	}
}