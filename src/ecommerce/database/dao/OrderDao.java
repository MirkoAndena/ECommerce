package ecommerce.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ecommerce.database.dto.Article;
import ecommerce.database.dto.Order;
import ecommerce.database.dto.Purchase;
import ecommerce.database.dto.User;

public class OrderDao {

	private Connection connection;

	public OrderDao(Connection connection) {
		this.connection = connection;
	}
	
	// TODO andrebbe fatto a batch con rollback se fallisce qualcosa
	public boolean storeOrder(Order order) {
		String query = "INSERT INTO `ecommerce`.`order` (`user`, `seller`, `user_address`, `shipment_date`, `total`) VALUES (?,?,?,?,?)";
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, order.user.id);
			statement.setInt(2, order.seller.id);
			statement.setString(3, order.user.address);
			statement.setLong(4, order.shipmentDate.toInstant().getEpochSecond());
			statement.setFloat(5, order.total);
			int affectedRows = statement.executeUpdate();
			
			if (affectedRows == 0) return false;
			
			// Find id of inserted order
			long orderId = -1;
	        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                orderId = generatedKeys.getLong(1);
	            }
	            else return false;
	        }
	        
	        // Insert all articles
	        return insertArticles(order, orderId);
	        
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean insertArticles(Order order, long id) {
		for (Purchase purchase : order.purchases) {
			String query = "INSERT INTO `ecommerce`.`order_articles` (`order`, `article`, `quantity`) VALUES (?,?,?)";
			try {
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setLong(1, id);
				statement.setInt(2, purchase.article.id);
				statement.setInt(3, purchase.quantity);
				int affectedRows = statement.executeUpdate();
				if (affectedRows == 0) return false;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	public List<Order> getOrders(User user, ArticleDao articleDao, SellerDao sellerDao) {
		List<Order> orders = new ArrayList<Order>();
		String query = """
		    SELECT *
			FROM `order` a
			WHERE oa.`user` = ?
			""";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, user.id);
			try (ResultSet set = statement.executeQuery()) {
				if (!set.isBeforeFirst()) return orders;
				while (set.next()) {
					orders.add(build(user, articleDao, sellerDao, set));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}
	
	private Order build(User user, ArticleDao articleDao, SellerDao sellerDao, ResultSet set) throws SQLException {
		return new Order(
				user,
				sellerDao.getSellerById(set.getInt("seller")),
				user.address,
				new Date(set.getLong("shipment_date")),
				set.getFloat("total"),
				getPurchasesOfOrder(set.getInt("id"), articleDao)
			);
	}
	
	private List<Purchase> getPurchasesOfOrder(int order, ArticleDao articleDao) {
		List<Purchase> purchases = new ArrayList<Purchase>();
		String query = """
		    SELECT a.`id`, a.`name`, a.`description`, a.`image`, c.`name` as 'category', oa.`quantity`, oa.`price`
			FROM `article` a
			INNER JOIN `order_articles` oa ON a.id = oa.article
			INNER JOIN `category` c ON a.category = c.id
			WHERE oa.`order` = ?
			""";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, order);
			try (ResultSet set = statement.executeQuery()) {
				if (!set.isBeforeFirst()) return purchases;
				while (set.next()) {
					Article article = articleDao.build(set);
					Purchase purchase = new Purchase(article, set.getFloat("price"), set.getInt("quantity"));
					purchases.add(purchase);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return purchases;
	}
}
