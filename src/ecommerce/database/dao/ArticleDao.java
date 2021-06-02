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
	private User user;

	public ArticleDao(Connection connection, User user) {
		this.connection = connection;
		this.user = user;
	}
	
	public void setArticleSeen(int articleId) {
		String query = "INSERT INTO `ecommerce`.`articles_seen` (`article`, `user`, `datetime`) VALUES (?,?,?)";
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, articleId);
			statement.setInt(2, user.id);
			statement.setLong(3, new Date().toInstant().getEpochSecond());
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Article> getLastSeen() {
		List<Article> articles = new ArrayList<Article>();
		String query = "SELECT TOP 5 * FROM `ecommerce`.`articles_seen` WHERE `user` = ? ORDER BY `datetime` DESC";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, user.id);
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
}
