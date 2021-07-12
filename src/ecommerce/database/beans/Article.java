package ecommerce.database.beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

// Questo beans non continene solo le info di un articolo ma tutte quelle che devono essere
// visualizzate
public class Article {
	
    public int id;
	public String name;
	public String description;
	public String image;
	public String category;
	public List<Seller> sellers;

	public Article(int id, String name, String description, String image, String category) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.image = resolveImagePath(image);
		this.category = category;
		this.sellers = new ArrayList<Seller>();
	}
	
	public static Article addOrCreate(List<Article> articles, Article article, Seller seller) {
		
		// Cerco l'articolo nella lista
		Article found = null;
		for (Article a : articles) {
			if (a.id == article.id)
				found = a;
		}
		
		if (found != null) {
			// Se trovato aggiungo il venditore
			found.sellers.add(seller);
			return found;
		}
		else {
			// Altrimenti creo un nuovo articolo e lo inserisco in lista
			article.sellers.add(seller);
			articles.add(article);
			return article;
		}		
	}
	
	private String resolveImagePath(String imageFileName) {
		return "images/" + imageFileName;
	}
}
