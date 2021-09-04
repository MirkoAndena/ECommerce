package ecommerce_ria.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ecommerce_ria.controllers.support.AuthenticatedServlet;
import ecommerce_ria.controllers.support.FatalException;
import ecommerce_ria.utils.FileReader;
import ecommerce_ria.utils.Json;

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
		
		Json json = Json.build()
				.add("cartTemplate", FileReader.read(this, "sellercart_template.html"))
				.add("purchaseTemplate", FileReader.read(this, "cart_purchase_template.html"));
			
		super.sendResult(response, json);
	}

	@Override
	public void Post(HttpServletRequest request, HttpServletResponse response, int user) throws ServletException, IOException, FatalException {
		// TODO Auto-generated method stub
	}
}
