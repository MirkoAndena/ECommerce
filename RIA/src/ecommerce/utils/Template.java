package ecommerce.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;

public class Template {
	public String template;
	public List<Map<String, Object>> contents;
	
	private Template() {
		contents = new ArrayList<Map<String,Object>>();
	}
	
	public static Template load(HttpServlet servlet, String filename) {
		String path = servlet.getServletContext().getRealPath("/WEB-INF/" + filename);   
		Template template = new Template();
		template.template = FileReader.read(path);
		return template;
	}
	
	public Template addContent(Map<String, Object> map) {
		contents.add(map);
		return this;
	}
	
	public Json getJson() {
		return null;
	}
}
