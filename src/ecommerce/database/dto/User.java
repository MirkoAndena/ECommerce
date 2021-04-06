package ecommerce.database.dto;

public class User implements Dto {
    public int id;
	public String name;
	public String surname;
	public String address;
	public String email;
	public String password;

	public User() {
		this.id = -1;
	}
}
