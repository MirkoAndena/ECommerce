package ecommerce.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ecommerce.Hashing.HashFunction;

// Il digest della password dovrebbe essere creato lato client ma non si può senza javascript,
// quindi viaggiano attraverso la rete in chiaro. Questo è un livello di protezione minimo (e non sufficente)
// in questo modo almeno sul database non vengono salvate direttamente le password
public class User {

	private int id;
	private String name;
	private String surname;
	private String address;
	private String email;
	private String password;
	
	public void Store(Connection connection, HashFunction hash) {
		String query = "INSERT INTO User (name, surname, address, email, password) VALUES (?,?,?,?,?)";
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(0, name);
			statement.setString(1, surname);
			statement.setString(2, address);
			statement.setString(3, email);
			statement.setString(4, hash.CreateDigest(password));
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
