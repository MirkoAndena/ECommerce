package ecommerce.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ecommerce.database.dto.Range;
import ecommerce.database.dto.Seller;

public class SellerDao {
	
	private Connection connection;

	public SellerDao(Connection connection) {
		this.connection = connection;
	}
	
	private Seller build(ResultSet set) throws SQLException {
		return build(set, "id", "name");
	}
	
	public Seller build(ResultSet set, String idColumn, String nameColumn) throws SQLException {
		Seller seller = new Seller(
			set.getInt(idColumn),
			set.getString(nameColumn),
			set.getInt("rating"),
			set.getFloat("free_shipping_threshold"));
		
		// Setting shipment range
		List<Range> ranges = getShipmentRange(seller.id);
		if (ranges != null && seller.freeShippingThreshold > 0) seller.setShipmentRange(ranges);
		
		return seller;
	}
	
	public Seller getSellerById(int id) {
		String query = """
				SELECT *
				FROM `seller`
				WHERE `id` = ?
			""";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, id);
			try (ResultSet set = statement.executeQuery()) {
				if (!set.isBeforeFirst()) return null;
				if (set.next()) {
					return build(set);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<Range> getShipmentRange(int sellerId) {
		String query = """
			SELECT `max_articles`, `price`
			FROM `shipment_range`
			WHERE `seller` = ?
			ORDER BY -`max_articles` DESC
			""";
		List<Range> ranges = new ArrayList<Range>();
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, sellerId);
			try (ResultSet set = statement.executeQuery()) {
				if (!set.isBeforeFirst()) return null;
				Integer last = 1;
				while (set.next()) {
					Integer max = set.getInt("max_articles");
					if (set.wasNull()) max = null;
					float price = set.getFloat("price");
					ranges.add(new Range(last, max, price));
					last = max;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return ranges;
	}
}
