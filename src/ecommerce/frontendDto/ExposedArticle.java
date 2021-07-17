package ecommerce.frontendDto;

import java.util.ArrayList;
import java.util.List;

import ecommerce.database.dto.Article;

public class ExposedArticle {
	public Article article;
	public List<ExposedSeller> sellers;
	
	public ExposedArticle(Article article) {
		this.article = article;
		this.sellers = new ArrayList<ExposedSeller>();
	}
	
	public static void addSellerToArticleList(List<ExposedArticle> exposedArticles, Article article, ExposedSeller exposedSeller) {
			
		// Cerco l'articolo nella lista
		ExposedArticle found = null;
		for (ExposedArticle a : exposedArticles)
			if (a.article.id == article.id) {
				found = a;
				break;
			}
		
		
		if (found != null) {
			// Se trovato aggiungo il venditore
			found.sellers.add(exposedSeller);
		}
		else {
			// Altrimenti creo un nuovo articolo e lo inserisco in lista
			ExposedArticle exposedArticle = new ExposedArticle(article);
			exposedArticle.sellers.add(exposedSeller);
			exposedArticles.add(exposedArticle);
		}		
	}
}
