package ecommerce_ria.database.dto;

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
	
	@Override
	public String toString() {
		return String.format("%s %.2f € per %d = %.2f €", article, price, quantity, price*quantity);
	}
}
