package ecommerce.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import ecommerce.database.dto.User;
import ecommerce.hashing.HashFunction;

// Il digest della password dovrebbe essere creato lato client ma non si può senza javascript,
// quindi viaggiano attraverso la rete in chiaro. Questo è un livello di protezione minimo (e non sufficente)
// in questo modo almeno sul database non vengono salvate direttamente le password
public class UserDao extends Dao<User> {

	private Connection connection;
	private HashFunction hashFunction;

	public UserDao(Connection connection, HashFunction hashFunction) {
		super(User.class);
		this.connection = connection;
		this.hashFunction = hashFunction;
	}
	
	public void store(User user) {
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
	
	// SQL injection non funziona con email e password perch� sono sottoposti a verifica di integrit�
	public boolean checkEmailAndPassword(String email, String password) {
		if (!isValidEmailAddress(email)) return false;
		String query = "SELECT id FROM User WHERE email LIKE ? AND password LIKE ?";
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(0, email.trim());
			statement.setString(1, hashFunction.CreateDigest(password.trim()));
			return statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean isValidEmailAddress(String email) {
		   boolean result = true;
		   try {
		      InternetAddress emailAddr = new InternetAddress(email);
		      emailAddr.validate();
		   } catch (AddressException ex) {
		      result = false;
		   }
		   return result;
		}
}
