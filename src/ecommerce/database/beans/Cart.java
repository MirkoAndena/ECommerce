package ecommerce.database.beans;

import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.x.protobuf.Mysqlx.ClientMessages.Builder;

import ecommerce.database.beans.Cart.Group.BuyingArticle;

public class Cart {
	
	public static class Group {
		
		public static class BuyingArticle {
			public Article article;
			public int quantity;
			
			public BuyingArticle(Article article, int quantity) {
				this.article = article;
				this.quantity = quantity;
			}
		}
		
		public int seller;
		public List<BuyingArticle> articles;
		
		public Group(int seller) {
			this.seller = seller;
			this.articles = new ArrayList<BuyingArticle>();
		}
	}
	
	public List<Group> groups;
	
	public Cart() {
		this.groups = new ArrayList<Cart.Group>();
	}
	
	// MUST CONDITION: sellerId = any of article.sellers.id
	public void add(Article article, int quantity, int sellerId) {
		BuyingArticle buyingArticle = new BuyingArticle(article, quantity);
		
		// Cerco se esiste già un gruppo con quel venditore
		Group group = null;
		for (Group g : groups) {
			if (g.seller == sellerId)
				group = g;
		}
		
		if (group != null) {
			BuyingArticle found = getArticle(group.articles, article.id);
			if (found == null) group.articles.add(buyingArticle);
			else found.quantity += quantity;
		}
		else {
			Group newGroup = new Group(sellerId);
			newGroup.articles.add(buyingArticle);
			groups.add(newGroup);
		}
	}
	
	private BuyingArticle getArticle(List<BuyingArticle> buyingArticles, int articleId) {
		for (BuyingArticle article : buyingArticles)
			if (article.article.id == articleId)
				return article;
		return null;
	}
	
	// Ritorno float array perchè java fa cagare!
	public float[] getTotalOfSeller(int sellerId) {
		int count = 0;
		float total = 0;
		
		Group group = getGroupOfSeller(sellerId);
		
		// Cerco il gruppo del venditore
		if (group != null) {
			count = group.articles.size();
			for (BuyingArticle buyingArticle : group.articles) {
				Seller seller = getSellerOfArticle(buyingArticle.article, sellerId);
				total += seller.price * buyingArticle.quantity;
			}
		}
		
		// Calcolo spese di spedizione
		total += seller.calculateShippingPrice(total, count);
		// TODO gestire diversamente i prezzi (non all'interno dei seller)
		
		return new float[] {count, total};
	}
	
	private Group getGroupOfSeller(int sellerId) {
		for (Group group : groups)
			if (group.seller == sellerId)
				return group;
		return null;
	}
	
	private Seller getSellerOfArticle(Article article, int sellerId) {
		for (Seller seller : article.sellers) {
			if (seller.id == sellerId)
				return seller;
		}
		return null;
	}
	
	private float getArticlePriceOfSeller(Article article, int sellerId) {
		for (Seller seller : article.sellers) {
			if (seller.id == sellerId)
				return seller.price;
		}
		return 0;
	}
}
