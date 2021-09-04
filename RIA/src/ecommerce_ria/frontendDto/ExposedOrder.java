package ecommerce_ria.frontendDto;

import java.util.List;

import ecommerce_ria.database.dto.Order;
import ecommerce_ria.database.dto.Purchase;

public class ExposedOrder {
	public int id;
	public String seller;
	public String userAddress;
	public String shipmentDate;
	public String total;
	public List<Purchase> purchases;
	
	public ExposedOrder(Order order, List<Purchase> purchases) {
		this.id = order.id;
		this.seller = order.seller.name;
		this.userAddress = order.userAddress;
		this.shipmentDate = order.getShipmentDate();
		this.total = String.format("%.2f �", order.total);
		this.purchases = purchases;
	}
}
