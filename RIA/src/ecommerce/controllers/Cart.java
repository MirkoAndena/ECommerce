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

@WebServlet("/Cart")
@MultipartConfig
public class Cart extends AuthenticatedServlet {
	private static final long serialVersionUID = 1L;
       
    public Cart() {
        super();
    }
    
	@Override
	protected void OnInit() {
		
	}

	@Override
	public void Get(HttpServletRequest request, HttpServletResponse response, int user) throws ServletException, IOException, FatalException {
		
		// Visualizzazione del carattere euro (�)
		response.setCharacterEncoding("UTF-8");
		
		Json json = Json.build(ClientPages.Risultati)
				.add("cartTemplate", FileReader.read(this, "sellercart_template.html"))
				.add("purchaseTemplate", FileReader.read(this, "cart_purchase_template.html"));
			
		super.sendResult(response, json);
	}

	@Override
	public void Post(HttpServletRequest request, HttpServletResponse response, int user) throws ServletException, IOException, FatalException {
		// TODO Auto-generated method stub
	}
}
