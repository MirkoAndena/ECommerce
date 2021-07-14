package ecommerce.database.dto;

public class Purchase {
	public Article article;
	public float price;
	public int quantity;
	
	public Purchase(Article article, float price, int quantity) {
		this.article = article;
		this.price = price;
		this.quantity = quantity;
	}

	public float calculateTotal() {
		return price * quantity;
	}
}
