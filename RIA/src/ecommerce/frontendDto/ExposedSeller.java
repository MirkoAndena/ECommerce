package ecommerce.frontendDto;

import java.util.List;

import ecommerce.database.dto.Cart;
import ecommerce.database.dto.Range;
import ecommerce.database.dto.Seller;
import ecommerce.utils.Pair;

public class ExposedSeller {
	public int id;
	public String name;
	public int rating;
	public float freeShippingThreshold;
	public List<Range> shipmentRanges;
	public String shipmentRangesStringValue;
	public float price;
	public int articlesAddedToCart;
	public float totalValue;
	
	public ExposedSeller(Seller seller, float price) {
		this.id = seller.id;
		this.name = seller.name;
		this.rating = seller.rating;
		this.freeShippingThreshold = seller.freeShippingThreshold;
		this.shipmentRangesStringValue = seller.getVerboseShippingRanges();
		this.shipmentRanges = seller.shipmentRanges;
		this.price = price;
	}
	
	public void setTotalOfCart(Cart cart) {
		Pair<Integer, Float> result = cart.getTotalOfSeller(id);
		this.articlesAddedToCart = result.first;
		this.totalValue = result.second;
	}
}
