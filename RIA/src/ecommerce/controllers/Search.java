package ecommerce.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ecommerce.controllers.support.AuthenticatedServlet;
import ecommerce.controllers.support.FatalException;
import ecommerce.database.dao.ArticleDao;
import ecommerce.database.dao.SellerDao;
import ecommerce.frontendDto.ArticleFound;
import ecommerce.frontendDto.ExposedArticle;
import ecommerce.utils.ClientPages;
import ecommerce.utils.FileReader;
import ecommerce.utils.Json;

@WebServlet("/Search")
@MultipartConfig
public class Search extends AuthenticatedServlet {
	private static final long serialVersionUID = 1L;
	private ArticleDao articleDao;
	private SellerDao sellerDao;
       
    public Search() {
        super();
    }
    
	@Override
	protected void OnInit() {
		this.articleDao = new ArticleDao(connection);
		this.sellerDao = new SellerDao(connection);
	}

	@Override
	public void Get(HttpServletRequest request, HttpServletResponse response, int user) throws ServletException, IOException, FatalException {
		
		// Search articles
		String searched = request.getParameter("search_string");
		
		List<ArticleFound> articlesFound = new ArrayList<ArticleFound>();
		if (searched != null && !searched.isEmpty()) {
			articlesFound = articleDao.searchInNameAndDescription(searched);
		}
		
		// Selected article
		ExposedArticle selectedArticle = null;
		String selected = request.getParameter("selected_article");
		if (selected != null) {
			int id = -1;
			try { id = Integer.parseInt(selected); }
			catch (NumberFormatException e) { throw new FatalException(ClientPages.Risultati, "Non è stato passato un valore corrispondente ad un id"); }
			selectedArticle = articleDao.getArticleById(sellerDao, id, user); // Null gestito dalla pagina html
		}
		
		// Update last seen articles
		if (selectedArticle != null)
			articleDao.setArticleSeen(selectedArticle.id, user);
		
		// Visualizzazione del carattere euro (€)
		response.setCharacterEncoding("UTF-8");
		
		Json json = Json.build()
				.add("articleItemListTemplate", FileReader.read(this, "article_itemlist_template.html"))
				.add("articleTemplate", FileReader.read(this, "article_template.html"))
				.add("sellerTemplate", FileReader.read(this, "seller_template.html"))
				.add("articles", articlesFound)
				.add("selected", selectedArticle);
			
		super.sendResult(response, json);
	}

	@Override
	public void Post(HttpServletRequest request, HttpServletResponse response, int user) throws ServletException, IOException, FatalException {
		// TODO Auto-generated method stub
	}
}
