package ecommerce.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ecommerce.Config;
import ecommerce.SessionContext;
import ecommerce.database.dto.Article;
import ecommerce.database.dto.Seller;
import ecommerce.frontendDto.ArticleFound;
import ecommerce.frontendDto.ExposedArticle;
import ecommerce.frontendDto.ExposedSeller;
import ecommerce.utils.Pair;

public class ArticleDao {
	
	private Connection connection;

	public ArticleDao(Connection connection) {
		this.connection = connection;
	}
	
	public Article build(ResultSet set) throws SQLException {
		return new Article(
		set.getInt("id"),
		set.getString("name"),
		set.getString("description"),
		set.getString("image"),
		set.getString("category"));
	}
	
	private void buildArticle(Seller seller, List<ExposedArticle> articles, int user, ResultSet set) throws SQLException {
		Article article = build(set);		
		ExposedSeller exposedSeller = new ExposedSeller(seller, set.getFloat("price"));
		exposedSeller.setTotalOfCart(SessionContext.getInstance(user).getCart());
		ExposedArticle.addSellerToArticleList(articles, article, exposedSeller);
	}
	
	public Pair<Article, Seller> getArticleAndSellerById(SellerDao sellerDao, int articleId, int sellerId) {
		String query = """
		    SELECT DISTINCT a.`id`, a.`name`, a.`description`, a.`image`, s.`name` as 'seller', s.`id` as seller_id, s.`rating`, s.`free_shipping_threshold`, sa.`price`, c.`name` as 'category'
			FROM `article` a
			INNER JOIN `seller_articles` sa ON a.id = sa.article
			INNER JOIN `seller` s ON s.id = sa.seller
			INNER JOIN `category` c ON a.category = c.id
			WHERE a.`id` = ? AND s.`id` = ?
			""";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, articleId);
			statement.setInt(2, sellerId);
			try (ResultSet set = statement.executeQuery()) {
				if (!set.isBeforeFirst()) return null;
				if (set.next()) {
					return new Pair<Article, Seller>(build(set), sellerDao.build(set, "seller_id"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ExposedArticle getArticleById(SellerDao sellerDao, int articleId, int user) {
		List<ExposedArticle> articles = new ArrayList<ExposedArticle>();
		String query = """
		    SELECT DISTINCT a.`id`, a.`name`, a.`description`, a.`image`, s.`name` as 'seller', s.`id` as seller_id, s.`rating`, s.`free_shipping_threshold`, sa.`price`, c.`name` as 'category'
			FROM `article` a
			INNER JOIN `seller_articles` sa ON a.id = sa.article
			INNER JOIN `seller` s ON s.id = sa.seller
			INNER JOIN `category` c ON a.category = c.id
			WHERE a.`id` = ?
			""";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, articleId);
			try (ResultSet set = statement.executeQuery()) {
				if (!set.isBeforeFirst()) return null;
				while (set.next()) {
					buildArticle(sellerDao.build(set, "seller_id"), articles, user, set);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return articles.size() > 0 ? articles.get(0) : null;
	}
	
	public void setArticleSeen(int articleId, int user) {
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
	
	// Ultimi articoli visti
	public List<ExposedArticle> getLastSeen(SellerDao sellerDao, int user) {
		List<ExposedArticle> articles = new ArrayList<ExposedArticle>();
		String query = """
		    SELECT DISTINCT a.`id`, a.`name`, a.`description`, a.`image`, s.`name` as 'seller', s.`id` as seller_id, s.`rating`, s.`free_shipping_threshold`, sa.`price`, c.`name` as 'category'
			FROM `article` a
			INNER JOIN `seller_articles` sa ON a.id = sa.article
			INNER JOIN `seller` s ON s.id = sa.seller
			INNER JOIN `category` c ON a.category = c.id
			INNER JOIN `articles_seen` ase ON ase.article = a.id
			WHERE ase.`user` = ?
			ORDER BY `datetime` DESC, sa.`price` LIMIT 5
			""";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, user);
			try (ResultSet set = statement.executeQuery()) {
				if (!set.isBeforeFirst()) return articles;
				while (set.next()) {
					buildArticle(sellerDao.build(set, "seller_id"), articles, user, set);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return articles;
	}
	
	// Articoli nella categoria default
	public List<ExposedArticle> getSalesArticles(SellerDao sellerDao, int user) {
		List<ExposedArticle> articles = new ArrayList<ExposedArticle>();
		String query = """
			SELECT DISTINCT a.`id`, a.`name`, a.`description`, a.`image`, s.`name` as 'seller', s.`id` as seller_id, s.`rating`, s.`free_shipping_threshold`, sa.`price`, c.`name` as 'category'
			FROM `article` a
			INNER JOIN `seller_articles` sa ON a.id = sa.article
			INNER JOIN `seller` s ON s.id = sa.seller
			INNER JOIN `category` c ON a.category = c.id
			WHERE c.id = ?
			ORDER BY sa.`price`
				""";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, Config.DefaultCategory);
			try (ResultSet set = statement.executeQuery()) {
				if (!set.isBeforeFirst()) return articles;
				while (set.next()) {
					buildArticle(sellerDao.build(set, "seller_id"), articles, user, set);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return articles;
	}
	
	// Articoli cercati
	public List<ArticleFound> searchInNameAndDescription(String text) {
		List<ArticleFound> articles = new ArrayList<ArticleFound>();
		if (text == null) return articles;	
		String query = """
			SELECT DISTINCT a.`id`, a.`name`, a.`description`, a.`image`, s.`name` as 'seller', s.`id` as seller_id, s.`rating`, s.`free_shipping_threshold`, sa.`price`, c.`name` as 'category'
			FROM `article` a
			INNER JOIN `seller_articles` sa ON a.id = sa.article
			INNER JOIN `seller` s ON s.id = sa.seller
			INNER JOIN `category` c ON a.category = c.id
			WHERE a.`name` LIKE ? OR a.`description` LIKE ?
			ORDER BY sa.`price`
				""";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			text = "%" + text + "%";
			statement.setString(1, text);
			statement.setString(2, text);
			try (ResultSet set = statement.executeQuery()) {
				if (!set.isBeforeFirst()) return articles;
				while (set.next()) {
					Article article = build(set);
					ArticleFound.updateList(articles, article, set.getFloat("price"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return articles;
	}
}
