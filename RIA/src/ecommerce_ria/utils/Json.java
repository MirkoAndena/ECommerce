package ecommerce_ria.utils;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ecommerce_ria.Config;

public class Json {
	
	public static Json build() {
		return new Json();
	}
	
	private GsonBuilder gsonBuilder;
	private Gson gson;
	private Map<String, Object> map;
	
	private Json() {
		gsonBuilder = new GsonBuilder();
		gson = Config.ReadableJson ? gsonBuilder.setPrettyPrinting().create() : gsonBuilder.create();
		map = new HashMap<String, Object>();
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
