package ecommerce.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ecommerce.beans.ExposedArticle;
import ecommerce.database.dao.ArticleDao;

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
		if (exposedArticles == null || exposedArticles.isEmpty()) 
			exposedArticles = articleDao.getSalesArticles();
		
		// Visualizzazione del carattere euro (�)
		response.setCharacterEncoding("UTF-8");
		
		super.getThymeleaf().init(request, response)
		.setVariable("exposedArticles", exposedArticles)
		.process("/home.html");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Unused
	}
}
