package ecommerce.frontendDto;

import java.util.ArrayList;
import java.util.List;
import ecommerce.database.dto.Article;
import ecommerce.database.dto.Purchase;
import ecommerce.database.dto.Range;
import ecommerce.database.dto.Seller;

public class SellerCart {
	public Seller seller;
	public List<Purchase> purchases;
	
	public SellerCart(Seller seller) {
		this.seller = seller;
		this.purchases = new ArrayList<Purchase>();
	}
	
	public void addPurchase(Article article, int quantity, float price) {
		
		// Find same article
		Purchase found = null;
		for (Purchase purchase : purchases)
			if (purchase.article.id == article.id) {
				found = purchase;
				break;
			}
		
		if (found != null)
			found.quantity += quantity;
		else {
			Purchase purchase = new Purchase(article, price, quantity);
			purchases.add(purchase);
		}
	}
	
	public float calculatePurchaseTotal() {
		float sum = 0;
		for (Purchase purchase : purchases)
			sum += purchase.calculateTotal();
		return sum;
	}
	
	public float calculateShippingPrice(float total) {
		int articleCount = calculateTotalArticles();
		if (seller.freeShippingThreshold == 0 || total >= seller.freeShippingThreshold)
			return 0;
		float endPrice = -1;
		for (Range range : seller.shipmentRanges) {
			if (range.end == null) endPrice = range.price;				
			else if (articleCount > range.start && articleCount <= range.end)
				return range.price;
		}
		return endPrice;
	}
	
	public float calculateTotal() {
		float total = calculatePurchaseTotal();
		return total + calculateShippingPrice(total);
	}
	
	public int calculateTotalArticles() {
		int sum = 0;
		for (Purchase purchase : purchases)
			sum += purchase.quantity;
		return sum;
	}
	
	public String getStringPrice() {
		float total = calculatePurchaseTotal();
		float shipment = calculateShippingPrice(total);
		String shipmentString = shipment == 0 ? "gratuita" : String.format("%.2f €", shipment);
		return String.format("Totale: %.2f € (%.2f € + spedizione %s)", (total + shipment), total, shipmentString);
	}
}
