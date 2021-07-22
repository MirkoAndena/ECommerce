package ecommerce.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ecommerce.controllers.support.AuthenticatedServlet;
import ecommerce.controllers.support.FatalException;
import ecommerce.database.dao.ArticleDao;
import ecommerce.database.dao.SellerDao;
import ecommerce.frontendDto.ArticleFound;
import ecommerce.frontendDto.ExposedArticle;

@WebServlet("/Search")
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
		
		if (searched == null || searched.trim().isEmpty()) {
			throw new FatalException("Stringa di ricerca non valida");
		}
		
		List<ArticleFound> articlesFound = articleDao.searchInNameAndDescription(searched);
		
		// Selected article
		ExposedArticle selectedArticle = null;
		String selected = request.getParameter("selected_article");
		if (selected != null) {
			int id = -1;
			try { id = Integer.parseInt(selected); }
			catch (NumberFormatException e) { throw new FatalException("Non è stato passato un valore corrispondente ad un id"); }
			selectedArticle = articleDao.getArticleById(sellerDao, id, user); // Null gestito dalla pagina html
		}
		
		// Update last seen articles
		if (selectedArticle != null)
			articleDao.setArticleSeen(selectedArticle.id, user);
		
		// Visualizzazione del carattere euro (€)
		response.setCharacterEncoding("UTF-8");
		
		super.getThymeleaf().init(request, response)
		.setVariable("articlesFound", articlesFound)
		.setVariable("selected", selectedArticle)
		.setVariable("searched", searched)
		.process("/results.html");
	}

	@Override
	public void Post(HttpServletRequest request, HttpServletResponse response, int user) throws ServletException, IOException, FatalException {
		// TODO Auto-generated method stub
	}
}
