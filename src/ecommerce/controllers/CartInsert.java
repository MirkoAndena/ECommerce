package ecommerce.controllers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ecommerce.database.beans.Article;
import ecommerce.database.dao.ArticleDao;

@WebServlet("/CartInsert")
public class CartInsert extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
    public CartInsert() {
        super();
    }
    
	@Override
	protected void OnInit() {

	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (super.redirectIfNotLogged(request, response)) return;
		
		String count = request.getParameter("count");
		String article = request.getParameter("article");
		String seller = request.getParameter("seller");
		
		// Conteggio
	}
}
