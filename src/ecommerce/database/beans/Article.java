package ecommerce.database.beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

// Questo beans non continene solo le info di un articolo ma tutte quelle che devono essere
// visualizzate
public class Article {
	
	public static class Range {
		public Integer max;
		public Float price;
		
		public Range(Integer max, Float price) {
			this.max = max;
			this.price = price;
		}
	}
	
    public int id;
	public String name;
	public String description;
	public String image;
	public String category;
	public int sellerId;
	public String seller;
	public int rating;
	public float freeShippingThreshold;
	public float price;
	public String shipmentRange;

	public Article(int id, String name, String description, String image, String category, int sellerId,
					String seller, int rating, float freeShippingThreshold, float price) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.image = resolveImagePath(image);
		this.category = category;
		this.sellerId = sellerId;
		this.seller = seller;
		this.rating = rating;
		this.freeShippingThreshold = freeShippingThreshold;
		this.price = price;
	}
	
	private String resolveImagePath(String imageFileName) {
		return "images/" + imageFileName;
	}
	
	public void setShipmentRange(List<Range> ranges) {
		if (ranges.size() == 0) return;
		int last = 1;
		float maxPrice = -1;
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(compile(last, ranges.get(0)));
		
		for (int i = 0; i < ranges.size(); i++) {
			if (ranges.get(i).max == null)
				maxPrice = ranges.get(i).price;
			else {
				stringBuilder.append(", " + compile(last, ranges.get(i)));
				last = ranges.get(i).max;
			}
		}
		
		if (maxPrice > -1)
			if (ranges.size() == 1)
				this.shipmentRange = "Spedizione sempre a €" + maxPrice;
			else
				this.shipmentRange = "Spedizione " + stringBuilder.toString() + ", da " + last + " in su €" + maxPrice;
	}
	
	private String compile(int last, Range range) { 
		return "da " + last + " a " + range.max + " €" + range.price;
	}
}
