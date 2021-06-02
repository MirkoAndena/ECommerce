package ecommerce.controllers;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.context.WebContext;

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
		// TODO: prendere l'utente da variabili di contesto
		this.articleDao = new ArticleDao(connection, null);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.redirectIfNotLogged(request, response);
		super.getThymeleaf().init(request, response)
		.setVariable("articles", articleDao.getLastSeen())
		.process("/WEB-INF/Home.html");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
