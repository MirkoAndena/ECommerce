package ecommerce.frontendDto;

import java.util.List;
import ecommerce.utils.ListUtils;

public class ArticleFound {
	public int id;
	public String name;
	public float minPrice;
	public int sellerCount;
	
	public ArticleFound(int id, String name, float price) {
		this.id = id;
		this.name = name;
		this.minPrice = price;
		this.sellerCount = 1;
	}
	
	// Nessuna gestione dei duplicati, chiamare su prezzi di seller diversi
	public static void updateList(List<ArticleFound> articles, ArticleFound article, float price) {
		ArticleFound found = ListUtils.find(articles, item -> item.id == article.id);
		if (found != null) {
			found.sellerCount++;
			if (found.minPrice > price)
				found.minPrice = price;
		}
		else
		{
			articles.add(article);
		}
	}
}
