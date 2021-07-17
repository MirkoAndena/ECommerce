package ecommerce.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ecommerce.database.dao.ArticleDao;
import ecommerce.frontendDto.ArticleFound;
import ecommerce.frontendDto.ExposedArticle;

@WebServlet("/Search")
public class Search extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private ArticleDao articleDao;
       
    public Search() {
        super();
    }
    
	@Override
	protected void OnInit() {

	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (super.redirectIfNotLogged(request, response)) return;
		
		if (this.articleDao == null)
			this.articleDao = new ArticleDao(connection, super.getUserId(request));
		
		// Search articles
		String searched = request.getParameter("search_string");
		List<ArticleFound> articlesFound = articleDao.searchInNameAndDescription(searched);
		
		// Selected article
		ExposedArticle selectedArticle = null;
		String selected = request.getParameter("selected");
		if (selected != null) {
			int id = -1;
			try { id = Integer.parseInt(selected); }
			catch (NumberFormatException e) { System.err.println("Integer value not parsable"); }
			selectedArticle = articleDao.getArticleById(id);
		}
		
		// Update last seen articles
		if (selectedArticle != null)
			articleDao.setArticleSeen(selectedArticle.article.id);
		
		// Visualizzazione del carattere euro (€)
		response.setCharacterEncoding("UTF-8");
		
		super.getThymeleaf().init(request, response)
		.setVariable("articlesFound", articlesFound)
		.setVariable("selected", selectedArticle)
		.setVariable("searched", searched)
		.process("/results.html");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Unused
	}
}
