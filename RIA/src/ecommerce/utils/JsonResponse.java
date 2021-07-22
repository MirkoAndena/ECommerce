package ecommerce.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;

public class JsonResponse {
	
	public enum Pages { Home, Risultati, Carrello, Ordini };
	
	public String page;
	public Object content;
	
	private JsonResponse(Pages page, Object content) {
		this.page = page.name();
		this.content = content;
	}
	
	public static JsonResponse build(Pages page, Object content) {
		JsonResponse response = new JsonResponse(page, content);
		return response;
	}
	
	public String getJson() {
		return (new Gson()).toJson(this);
	}
	
	@Override
	public String toString() {
		return getJson();
	}
}
