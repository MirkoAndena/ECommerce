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

import ecommerce.database.beans.Article;
import ecommerce.database.beans.User;
import ecommerce.hashing.HashFunction;

public class ArticleDao {

	private Connection connection;
	private int user;

	public ArticleDao(Connection connection, Integer user) {
		this.connection = connection;
		this.user = user;
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
		String query = "SELECT * FROM `ecommerce`.`articles_seen` WHERE `user` = ? ORDER BY `datetime` DESC LIMIT 5";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, user);
			try (ResultSet set = statement.executeQuery()) {
				if (!set.isBeforeFirst()) return null;
				if (set.next()) articles.add(new Article(set));
				else return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return articles;
	}
	
	public List<Article> getSalesArticles() {
		List<Article> articles = new ArrayList<Article>();
		String query = "SELECT * FROM `ecommerce`.`article` WHERE `category` = 4";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			try (ResultSet set = statement.executeQuery()) {
				if (!set.isBeforeFirst()) return null;
				while (set.next())
					articles.add(new Article(set));
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
		String query = "SELECT * FROM `ecommerce`.`article` WHERE `name` LIKE ? OR `description` LIKE ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			text = "%" + text + "%";
			statement.setString(1, text);
			statement.setString(2, text);
			try (ResultSet set = statement.executeQuery()) {
				if (!set.isBeforeFirst()) return null;
				while (set.next())
					articles.add(new Article(set));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return articles;
	}
}
