package ecommerce.frontendDto;

import java.util.List;

import ecommerce.database.dto.Article;
import ecommerce.utils.ListUtils;

public class ArticleFound {
	public int id;
	public String name;
	public float minPrice;
	public int sellerCount;
	
	public ArticleFound(Article article, float price) {
		this.id = article.id;
		this.name = article.name;
		this.minPrice = price;
		this.sellerCount = 1;
	}
	
	// Nessuna gestione dei duplicati, chiamare su prezzi di seller diversi
	public static void updateList(List<ArticleFound> articles, Article article, float price) {
		ArticleFound found = ListUtils.find(articles, item -> item.id == article.id);
		if (found != null) {
			found.sellerCount++;
			if (found.minPrice > price)
				found.minPrice = price;
		}
		else
		{
			articles.add(new ArticleFound(article, price));
		}
	}
}
