package ecommerce.frontendDto;

import java.util.ArrayList;
import java.util.List;

import ecommerce.database.dto.Article;

public class ExposedArticle {
	public int id;
	public String name;
	public String description;
	public String image;
	public String category;
	public List<ExposedSeller> sellers;
	
	public ExposedArticle(Article article) {
		this.id = article.id;
		this.name = article.name;
		this.description = article.description;
		this.image = article.image;
		this.category = article.category;
		this.sellers = new ArrayList<ExposedSeller>();
	}
	
	public static void addSellerToArticleList(List<ExposedArticle> exposedArticles, Article article, ExposedSeller exposedSeller) {
			
		// Cerco l'articolo nella lista
		ExposedArticle found = null;
		for (ExposedArticle a : exposedArticles)
			if (a.id == article.id) {
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
