package de.cuzim1tigaaa.easter.db.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.cuzim1tigaaa.easter.files.Config;
import de.cuzim1tigaaa.easter.files.Paths;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL {

	public String host, database, username, password;
	public int port;
	private HikariDataSource dataSource;

	public MySQL() {
		this.init();
		this.connect();
	}

	private void init() {
		Config config = Config.getConfig();
		this.host = config.getString(Paths.CONFIG_DB_HOST);
		this.port = config.getInt(Paths.CONFIG_DB_PORT);
		this.database = config.getString(Paths.CONFIG_DB_DATABASE);
		this.username = config.getString(Paths.CONFIG_DB_USERNAME);
		this.password = config.getString(Paths.CONFIG_DB_PASSWORD);
	}

	public void connect() {
		HikariConfig config = new HikariConfig();

		config.setJdbcUrl("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database);
		config.setDriverClassName("com.mysql.cj.jdbc.Driver");
		config.setUsername(this.username);
		config.setPassword(this.password);

		config.setMinimumIdle(2);
		config.setMaximumPoolSize(5);
		config.setConnectionTimeout(30000);

		this.dataSource = new HikariDataSource(config);
	}

	public void close(Connection connection, PreparedStatement statement, ResultSet set) {
		if(connection != null) try {
			connection.close();
		}catch(SQLException ignored) {
		}
		if(statement != null) try {
			statement.close();
		}catch(SQLException ignored) {
		}
		if(set != null) try {
			set.close();
		}catch(SQLException ignored) {
		}
	}

	public void close() {
		if(dataSource != null && !dataSource.isClosed()) dataSource.close();
	}

	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
}
