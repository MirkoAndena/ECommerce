package ecommerce.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ecommerce.database.dao.ArticleDao;
import ecommerce.frontendDto.ExposedArticle;

@WebServlet("/Home")
public class Home extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private ArticleDao articleDao;
       
    public Home() {
        super();
    }
    
	@Override
	protected void OnInit() {

	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (super.redirectIfNotLogged(request, response)) return;
		
		if (this.articleDao == null)
			this.articleDao = new ArticleDao(connection, super.getUserId(request));
		
		// Get last seen articles or from a default list
		List<ExposedArticle> exposedArticles = articleDao.getLastSeen();
		List<ExposedArticle> defaultArticles = articleDao.getSalesArticles();
		
		if (exposedArticles.size() < 5) {
			// Remove articles duplicated from default list
			for (ExposedArticle a : exposedArticles)
				defaultArticles.removeIf(b -> a.article.id == a.article.id);
			
			// Adding casual elements from default
			Random random = new Random();
			while (exposedArticles.size() < 5) {
				int index = random.nextInt(defaultArticles.size());
				exposedArticles.add(defaultArticles.get(index));
				defaultArticles.remove(index);
			}
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
