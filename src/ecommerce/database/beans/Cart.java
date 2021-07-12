package ecommerce.database.beans;

import java.util.ArrayList;
import java.util.List;

public class Cart {
	
	public static class Group {
		public int seller;
		public List<Article> articles;
		
		public Group(int seller) {
			this.seller = seller;
			this.articles = new ArrayList<Article>();
		}
	}
	
	public List<Group> groups;
	
	public Cart() {
		this.groups = new ArrayList<Cart.Group>();
	}
	
	// MUST CONDITION: sellerId = any of article.sellers.id
	public void add(Article article, int sellerId) {
		
		// Cerco se esiste già un gruppo con quel venditore
		Group group = null;
		for (Group g : groups) {
			if (g.seller == sellerId)
				group = g;
		}
		
		if (group != null)
			group.articles.add(article);
		else {
			Group newGroup = new Group(sellerId);
			newGroup.articles.add(article);
			groups.add(newGroup);
		}
	}
	
	// Ritorno float array perchè java fa cagare!
	public float[] getTotalOfSeller(int sellerId) {
		int count = 0;
		float total = 0;
		
		// Cerco il venditore nel gruppo
		for (Group group : groups) {
			if (group.seller == sellerId) {
				count = group.articles.size();
				
				// Per ogni articolo devo ritrovare il venditore e sommare il suo prezzo
				for (Article article : group.articles) {
					for (Seller seller : article.sellers) {
						if (seller.id == sellerId)
							total += seller.price;
					}
				}
			}
		}
		
		return new float[] {count, total};
	}
}
