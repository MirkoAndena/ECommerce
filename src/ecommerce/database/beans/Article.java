package ecommerce.database.beans;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Article {
    public int id;
	public String name;
	public String description;
	public String image;
	public String category;

	public Article(ResultSet set) throws SQLException {
		this.id = set.getInt("id");
		this.name = set.getString("name");
		this.description = set.getString("description");
		this.image = set.getString("image");
		this.category = set.getString("category");
	}
	
	public Article(int id, String name, String description, String image, String category) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.image = image;
		this.category = category;
	}
}
