package ecommerce.utils;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Json {
	
	public static Json build(ClientPages page) {
		return new Json(page);
	}
	
	private GsonBuilder gsonBuilder;
	private Gson gson;
	private Map<String, Object> map;
	
	private Json(ClientPages page) {
		gsonBuilder = new GsonBuilder();
		gson = gsonBuilder.create();
		map = new HashMap<String, Object>();
		this.add("page", page.servlet);
	}
	
	public Json add(String key, Object value) {
		map.put(key, value);
		return this;
	}
	
	public String toJson() {
		return gson.toJson(map);
	}
	
	@Override
	public String toString() {
		return toJson();
	}
}
