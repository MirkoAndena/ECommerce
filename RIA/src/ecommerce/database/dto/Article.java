package ecommerce.database.dto;

public class Article {
	
    public int id;
	public String name;
	public String description;
	public String image;
	public String category;

	public Article(int id, String name, String description, String image, String category) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.image = resolveImagePath(image);
		this.category = category;
	}
	
	private String resolveImagePath(String imageFileName) {
		return "images/" + imageFileName;
	}

	@Override
	public String toString() {
		return name;
	}
}
