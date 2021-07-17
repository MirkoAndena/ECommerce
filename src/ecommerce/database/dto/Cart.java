package ecommerce.database.dto;

import java.util.ArrayList;
import java.util.List;

import ecommerce.frontendDto.SellerCart;
import ecommerce.utils.Pair;

public class Cart {
	
	public List<SellerCart> sellerCarts;
	
	public Cart() {
		this.sellerCarts = new ArrayList<SellerCart>();
	}
	
	public void add(Seller seller, Article article, int quantity, float price) {
		SellerCart found = null;
		for (SellerCart sellerCart : sellerCarts)
			if (sellerCart.seller.id == seller.id) {
				found = sellerCart;
				break;
			}
		
		if (found == null) {
			found = new SellerCart(seller);
			sellerCarts.add(found);
		}
			
		found.addPurchase(article, quantity, price);
	}
	
	public Pair<Integer, Float> getTotalOfSeller(Seller seller) {
		for (SellerCart sellerCart : sellerCarts)
			if (sellerCart.seller.id == seller.id)
				return new Pair<Integer, Float>(sellerCart.purchases.size(), sellerCart.calculatePurchaseTotal());
		return new Pair<Integer, Float>(0, 0f);
	}
}
