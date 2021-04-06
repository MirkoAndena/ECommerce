package ecommerce.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ecommerce.database.dto.User;
import ecommerce.hashing.HashFunction;

// Il digest della password dovrebbe essere creato lato client ma non si può senza javascript,
// quindi viaggiano attraverso la rete in chiaro. Questo è un livello di protezione minimo (e non sufficente)
// in questo modo almeno sul database non vengono salvate direttamente le password
public class UserDao {

	private Connection connection;
	private HashFunction hashFunction;

	public UserDao(Connection connection, HashFunction hashFunction) {
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
	
	public boolean isPresent(String email, String password) {
		String query = "SELECT id FROM User WHERE email LIKE $1 AND password LIKE $2";
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(0, email);
			statement.setString(1, hashFunction.CreateDigest(password));
			return statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
