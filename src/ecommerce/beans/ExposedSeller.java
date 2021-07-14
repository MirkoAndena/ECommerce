package ecommerce.beans;

import ecommerce.database.dto.Cart;
import ecommerce.database.dto.Seller;
import ecommerce.utils.Pair;

public class ExposedSeller {
	public Seller seller;
	public float price;
	public int articlesAddedToCart;
	public float totalValue;
	
	public ExposedSeller(Seller seller, float price) {
		this.seller = seller;
		this.price = price;
	}
	
	public void setTotalOfCart(Cart cart) {
		Pair<Integer, Float> result = cart.getTotalOfSeller(seller);
		this.articlesAddedToCart = result.first;
		this.totalValue = result.second;
	}
}
