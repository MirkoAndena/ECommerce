package ecommerce.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ecommerce.database.dao.ArticleDao;
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
		List<ExposedArticle> exposedArticles = articleDao.searchInNameAndDescription(searched);
		
		// Update last seen articles
		for (ExposedArticle exposedArticle : exposedArticles) {
			articleDao.setArticleSeen(exposedArticle.article.id);
			System.out.println(exposedArticle.article.name);
		}
		
		// Visualizzazione del carattere euro (€)
		response.setCharacterEncoding("UTF-8");
		
		super.getThymeleaf().init(request, response)
		.setVariable("exposedArticles", exposedArticles)
		.process("/home.html");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Unused
	}
}
