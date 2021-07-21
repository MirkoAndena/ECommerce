package ecommerce.database.dto;

import java.util.ArrayList;
import java.util.List;

public class Seller {
	public int id;
	public String name;
	public int rating;
	public float freeShippingThreshold;
	public List<Range> shipmentRanges;
	
	public Seller(int id, String name, int rating, float freeShippingThreshold) {
		this.id = id;
		this.name = name;
		this.rating = rating;
		this.freeShippingThreshold = freeShippingThreshold;
		this.shipmentRanges = new ArrayList<Range>();
	}
	
	public void setShipmentRange(List<Range> ranges) {
		this.shipmentRanges = ranges;
	}
	
	public String getVerboseShippingRanges() {
		
		// Lista vuota => spedizione gratuita
		if (shipmentRanges.size() == 0) 
			return "gratuita";
		
		// Un solo elemento senza il massimo => spedizione fissa
		if (shipmentRanges.size() == 1 && shipmentRanges.get(0).end == null)
			return "€" + shipmentRanges.get(0).price;
		
		// Ranges
		List<String> list = new ArrayList<String>();
		for (Range range : shipmentRanges) {
			list.add(range.toString());
		}
		
		return String.join(", ", list);
	}

	@Override
	public String toString() {
		return name;
	}
}
