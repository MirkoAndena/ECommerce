package ecommerce_ria.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ecommerce_ria.controllers.support.AuthenticatedServlet;
import ecommerce_ria.controllers.support.FatalException;
import ecommerce_ria.database.dao.ArticleDao;
import ecommerce_ria.database.dao.SellerDao;
import ecommerce_ria.frontendDto.ExposedArticle;
import ecommerce_ria.utils.FileReader;
import ecommerce_ria.utils.Json;

@WebServlet("/Home")
@MultipartConfig
public class Home extends AuthenticatedServlet {
	private static final long serialVersionUID = 1L;
	private ArticleDao articleDao;
	private SellerDao sellerDao;
       
    public Home() {
        super();
    }
    
	@Override
	protected void OnInit() {
		this.articleDao = new ArticleDao(connection);
		this.sellerDao = new SellerDao(connection);
	}

	@Override
	public void Get(HttpServletRequest request, HttpServletResponse response, int user) throws ServletException, IOException, FatalException {		
		// Get last seen articles or from a default list
		List<ExposedArticle> exposedArticles = articleDao.getLastSeen(sellerDao, user);
		List<ExposedArticle> defaultArticles = articleDao.getSalesArticles(sellerDao, user);
		
		if (exposedArticles.size() + defaultArticles.size() < 5)
			throw new FatalException("Da specifica devono esserci almeno 5 elementi");
		
		if (exposedArticles.size() < 5) {
			// Remove articles duplicated from default list
			for (ExposedArticle a : exposedArticles)
				defaultArticles.removeIf(b -> b.id == a.id);
			
			// Adding casual elements from default
			Random random = new Random();
			while (exposedArticles.size() < 5 && defaultArticles.size() > 0) {
				int index = random.nextInt(defaultArticles.size());
				exposedArticles.add(defaultArticles.get(index));
				defaultArticles.remove(index);
			}
		}
		
		// Visualizzazione del carattere euro (�)
		response.setCharacterEncoding("UTF-8");
		
		Json json = Json.build()
			.add("articleTemplate", FileReader.read(this, "article_template.html"))
			.add("sellerTemplate", FileReader.read(this, "seller_template.html"))
			.add("articlePopperTemplate", FileReader.read(this, "article_popper.html"))
			.add("content", exposedArticles);
		
		super.sendResult(response, json);
	}

	@Override
	public void Post(HttpServletRequest request, HttpServletResponse response, int user) throws ServletException, IOException, FatalException {
		// TODO Auto-generated method stub
	}
}
