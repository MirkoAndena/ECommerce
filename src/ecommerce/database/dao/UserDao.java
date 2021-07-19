package ecommerce.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.mail.internet.InternetAddress;

import ecommerce.database.dto.User;
import ecommerce.hashing.HashFunction;

// Il digest della password dovrebbe essere creato lato client ma non si puÃ² senza javascript,
// quindi viaggiano attraverso la rete in chiaro. Questo Ã¨ un livello di protezione minimo (e non sufficente)
// in questo modo almeno sul database non vengono salvate direttamente le password
public class UserDao {

	private Connection connection;

	public UserDao(Connection connection) {
		this.connection = connection;
	}
	
	public void store(User user, HashFunction hashFunction) {
		String query = "INSERT INTO User (name, surname, address, email, password) VALUES (?,?,?,?,?)";
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(0, user.name);
			statement.setString(1, user.surname);
			statement.setString(2, user.address);
			statement.setString(3, user.email);
			statement.setString(4, hashFunction.CreateDigest(user.password));
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// SQL injection non funziona con email e password perchè sono sottoposti a verifica di integrità
	public Integer getUserIdFromLogin(String email, String password, HashFunction hashFunction) {
		if (!isValidEmailAddress(email)) return null;
		String query = "SELECT `id` FROM `ecommerce`.`user` WHERE `email` LIKE ? AND `password` LIKE ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, email.trim());
			statement.setString(2, hashFunction.CreateDigest(password.trim()));
			try (ResultSet set = statement.executeQuery()) {
				if (!set.isBeforeFirst()) return null;
				if (set.next()) return set.getInt("id");
				else return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static boolean isValidEmailAddress(String email) {
		   boolean result = true;
		   try {
		      InternetAddress emailAddr = new InternetAddress(email);
		      emailAddr.validate();
		   } catch (Exception ex) {
		      result = false;
		   }
		   return result;
	}
	
	public User getUserById(int id) {
		String query = "SELECT * FROM `ecommerce`.`user` WHERE `id` LIKE ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, id);
			try (ResultSet set = statement.executeQuery()) {
				if (!set.isBeforeFirst()) return null;
				if (set.next()) {
					return new User(
							set.getInt("id"),
							set.getString("name"),
							set.getString("surname"),
							set.getString("address"),
							set.getString("email")
					);
				}
				else return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
