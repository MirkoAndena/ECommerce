package ecommerce_ria.database.dto;

public class User {
    public int id;
	public String name;
	public String surname;
	public String address;
	public String email;
	public String password;

	public User() {
		this.id = -1;
	}
	
	public User(int id, String name, String surname, String address, String email) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.address = address;
		this.email = email;
	}
}
