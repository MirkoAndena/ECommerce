package ecommerce.database.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import ecommerce.database.IBeanBuilder;
import ecommerce.database.beans.Seller;

public class SellerDao implements IBeanBuilder<Seller> {

	@Override
	public Seller build(ResultSet set) throws SQLException {
		return new Seller(
			set.getInt("id"),
			set.getString("name"),
			set.getInt("rating"),
			set.getFloat("free_shipping_threshold"),
			set.getFloat("price"));
	}

}
