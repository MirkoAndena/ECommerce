package ecommerce.database.dto;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import ecommerce.frontendDto.SellerCart;

public class Order {
	public User user;
	public Seller seller;
	public String userAddress;
	public Date shipmentDate;
	public float total;
	public List<Purchase> purchases;
	
	public Order(User user, SellerCart sellerCart) {
		this.user = user;
		this.seller = sellerCart.seller;
		this.userAddress = user.address;
		this.shipmentDate = createRandomShipmentDate();
		this.total = sellerCart.calculateTotal();
		this.purchases = sellerCart.purchases;
	}
	
	public Order(User user, Seller seller, String address, Date shipmentDate, float total, List<Purchase> purchases) {
		this.user = user;
		this.seller = seller;
		this.userAddress = user.address;
		this.shipmentDate = shipmentDate;
		this.total = total;
		this.purchases = purchases;
	}

	private Date createRandomShipmentDate() {
		Random random = new Random();
		Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, random.nextInt(7));
        return cal.getTime();
	}
}
