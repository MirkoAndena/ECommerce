package ecommerce.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import ecommerce.database.IBeanBuilder;
import ecommerce.database.beans.Article;
import ecommerce.database.beans.Article.Range;
import ecommerce.database.beans.User;
import ecommerce.hashing.HashFunction;

public class ArticleDao implements IBeanBuilder<Article>{
	
	private Connection connection;
	private int user;

	public ArticleDao(Connection connection, Integer user) {
		this.connection = connection;
		this.user = user;
	}
	
	public Article build(ResultSet set) throws SQLException {
		return new Article(
		set.getInt("id"),
		set.getString("name"),
		set.getString("description"),
		set.getString("image"),
		set.getString("category"),
		set.getInt("seller_id"),
		set.getString("seller"),
		set.getInt("rating"),
		set.getFloat("free_shipping_threshold"),
		set.getFloat("price"));
	}
	
	public void setArticleSeen(int articleId) {
		String query = "INSERT INTO `ecommerce`.`articles_seen` (`article`, `user`, `datetime`) VALUES (?,?,?)";
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, articleId);
			statement.setInt(2, user);
			statement.setLong(3, new Date().toInstant().getEpochSecond());
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Article> getLastSeen() {
		List<Article> articles = new ArrayList<Article>();
		String query = """
			SELECT a.`name`, a.`description`, a.`image`, s.`name` as 'seller', s.`id` as seller_id, s.`rating`, s.`free_shipping_threshold`, sa.`price`, c.`name` as 'category'
			FROM `article` a, `seller` s, `category` c, `shipment_range` sr, `seller_articles` sa, `articles_seen` ase
			WHERE a.id = sa.article AND
				s.id = sa.seller AND 
				a.category = c.id AND
			    sr.seller = s.id AND
			    ase.`user` = ?
			    ORDER BY `datetime` DESC LIMIT 5
			""";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, user);
			try (ResultSet set = statement.executeQuery()) {
				if (!set.isBeforeFirst()) return null;
				while (set.next()) {
					Article article = build(set);
					List<Range> ranges = getShipmentRange(article.sellerId);
					if (ranges != null) article.setShipmentRange(ranges);
					articles.add(article);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return articles;
	}
	
	public List<Article> getSalesArticles() {
		List<Article> articles = new ArrayList<Article>();
		String query = """
			SELECT a.`name`, a.`description`, a.`image`, s.`name` as 'seller', s.`id` as seller_id, s.`rating`, s.`free_shipping_threshold`, sa.`price`, c.`name` as 'category'
				FROM `article` a, `seller` s, `category` c, `shipment_range` sr, `seller_articles` sa
				WHERE a.id = sa.article AND
					s.id = sa.seller AND 
					a.category = c.id AND
				    sr.seller = s.id AND
				    c.id = 4
				""";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			try (ResultSet set = statement.executeQuery()) {
				if (!set.isBeforeFirst()) return null;
				while (set.next()) {
					Article article = build(set);
					List<Range> ranges = getShipmentRange(article.sellerId);
					if (ranges != null) article.setShipmentRange(ranges);
					articles.add(article);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return articles;
	}
	
	public List<Article> searchInNameAndDescription(String text) {
		List<Article> articles = new ArrayList<Article>();
		if (text == null) return articles;	
		String query = """
			SELECT a.`name`, a.`description`, a.`image`, s.`name` as 'seller', s.`id` as seller_id, s.`rating`, s.`free_shipping_threshold`, sa.`price`, c.`name` as 'category'
				FROM `article` a, `seller` s, `category` c, `shipment_range` sr, `seller_articles` sa
				WHERE a.id = sa.article AND
					s.id = sa.seller AND 
					a.category = c.id AND
				    sr.seller = s.id AND
				    a.`name` LIKE ? AND a.`description` LIKE ?
				""";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			text = "%" + text + "%";
			statement.setString(1, text);
			statement.setString(2, text);
			try (ResultSet set = statement.executeQuery()) {
				if (!set.isBeforeFirst()) return null;
				while (set.next()) {
					Article article = build(set);
					List<Range> ranges = getShipmentRange(article.sellerId);
					if (ranges != null) article.setShipmentRange(ranges);
					articles.add(article);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return articles;
	}
	
	private List<Range> getShipmentRange(int sellerId) {
		String query = """
			SELECT `max_articles`, `price`
			FROM `shipment_range`
			WHERE `seller` = ?
			ORDER BY `max_articles`
			""";
		List<Range> ranges = new ArrayList<Article.Range>();
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, sellerId);
			try (ResultSet set = statement.executeQuery()) {
				if (!set.isBeforeFirst()) return null;
				while (set.next()) {
					Integer max = set.getInt("max_articles");
					float price = set.getFloat("price");
					ranges.add(new Range(max, price));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return ranges;
	}
}
