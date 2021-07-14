package ecommerce.database.beans;

import java.util.List;

public class Seller {
	public int id;
	public String name;
	public int rating;
	public float freeShippingThreshold;
	public float price;
	public String shipmentRange;
	
	public int articlesAddedToCart;
	public float totalValue;
	
	public Seller(int id, String name, int rating, float freeShippingThreshold, float price) {
		this.id = id;
		this.name = name;
		this.rating = rating;
		this.freeShippingThreshold = freeShippingThreshold;
		this.price = price;
		this.shipmentRange = null;
	}
	
	public void setShipmentRange(List<Range> ranges) {
		this.shipmentRange = Range.compileRangeList(ranges);
	}
	
	public void setTotalOfCart(Cart cart) {
		float[] result = cart.getTotalOfSeller(id);
		this.articlesAddedToCart = (int)result[0];
		this.totalValue = result[1];
	}
	
	public float calculateShippingPrice(float articlesPrice, int count) {
		return 0; // TODO
	}
}
