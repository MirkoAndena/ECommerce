package ecommerce.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IDTOBuilder<T> {
	T build(ResultSet set) throws SQLException;
}
