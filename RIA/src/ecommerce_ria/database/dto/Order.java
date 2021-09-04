package ecommerce_ria.database.dto;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang.time.DateFormatUtils;

import ecommerce_ria.frontendDto.SellerCart;

public class Order {
	public int id;
	public User user;
	public Seller seller;
	public String userAddress;
	public Date shipmentDate;
	public float total;
	
	public Order(User user, SellerCart sellerCart) {
		this.id = -1;
		this.user = user;
		this.seller = sellerCart.seller;
		this.userAddress = user.address;
		this.shipmentDate = createRandomShipmentDate();
		this.total = sellerCart.calculateTotal();
	}
	
	public Order(int id, User user, Seller seller, String address, Date shipmentDate, float total) {
		this.id = id;
		this.user = user;
		this.seller = seller;
		this.userAddress = user.address;
		this.shipmentDate = shipmentDate;
		this.total = total;
	}

	private Date createRandomShipmentDate() {
		Random random = new Random();
		Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, random.nextInt(7));
        return cal.getTime();
	}
	
	public String getShipmentDate() {
		return DateFormatUtils.format(shipmentDate, "dd/MM/yyyy");
	}
}
