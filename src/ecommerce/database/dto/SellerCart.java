package ecommerce.database.dto;

import java.util.ArrayList;
import java.util.List;

public class SellerCart {
	public Seller seller;
	public List<Purchase> purchases;
	
	public SellerCart(Seller seller) {
		this.seller = seller;
		this.purchases = new ArrayList<Purchase>();
	}
	
	public void addPurchase(Article article, int quantity, float price) {
		Purchase purchase = new Purchase(article, price, quantity);
		purchases.add(purchase);
	}
	
	public float calculateTotal() {
		float purchaseTotal = calculatePurchaseTotal();
		float shippingPrice = calculateShippingPrice(purchaseTotal);
		return purchaseTotal + shippingPrice;
	}
	
	private float calculatePurchaseTotal() {
		float sum = 0;
		for (Purchase purchase : purchases)
			sum += purchase.calculateTotal();
		return sum;
	}
	
	private float calculateShippingPrice(float total) {
		if (seller.freeShippingThreshold == 0 || total >= seller.freeShippingThreshold)
			return 0;
		float endPrice = -1;
		for (Range range : seller.shipmentRanges) {
			if (range.end == null) endPrice = range.price;				
			else if (purchases.size() > range.start && purchases.size() <= range.end)
				return range.price;
		}
		return endPrice;
	}
}
