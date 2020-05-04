package net.twerion.skycrystals.database;

import net.twerion.skycrystals.SkyCrystals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SQLManager {

	private String host;
	private String port;
	private String username;
	private String password;
	private String database;
	private Connection con;
	private final StayAliveTask aliveTask;
	private final AsyncHandler asyncHandler;
	private final Updater updater;
	
	public SQLManager(String host, String port, String username, String password, String database) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.database = database;
		this.aliveTask = new StayAliveTask(this);
		this.asyncHandler = new AsyncHandler();
		this.updater = new Updater();
	}
	
	public boolean openConnection() {
		try {
			if((this.con != null) && (!this.con.isClosed())) {
				return false;
			}
			Class.forName("com.mysql.jdbc.Driver");
			this.con = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
			this.aliveTask.setActive(true);
			this.aliveTask.start();
			this.updater.setActive(true);
			this.updater.start();
			return true;
		} catch (Exception e) {
			SkyCrystals.getInstance().getLogger().warning("Failed to connect: " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public Connection getConnection() {
		try {
			if((this.con == null) || (this.con.isClosed())) {
				openConnection();
			}
		} catch (Exception e) {}
		return this.con;
	}
	
	public void closeConnection() {
		try {
			if((this.con != null) && (!this.con.isClosed())) {
				this.con.close();
				this.con = null;
				this.aliveTask.setActive(false);
			}
		} catch (Exception e) {
			SkyCrystals.getInstance().getLogger().warning("Failed to connect: " + e.getMessage());
		}
	}
	
	public void close(PreparedStatement st, ResultSet rs) {
		try {
			if(st != null) {
				st.close();
			}
			if(rs != null) {
				rs.close();
			}
		} catch (Exception e) {}
	}
	
	public void executeUpdate(String statement) {
		try {
			PreparedStatement st = this.con.prepareStatement(statement);
			st.executeUpdate();
			close(st, null);
		}
		catch (Exception e) {
			SkyCrystals.getInstance().getLogger().warning("An error occurred while executing update: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void executeUpdate(PreparedStatement statement) {
		try {
			statement.executeUpdate();
			close(statement, null);
		} catch (Exception e) {
			SkyCrystals.getInstance().getLogger().warning("An error occurred while executing update: " + e.getMessage());
			e.printStackTrace();
		}
	}
	  
	public ResultSet executeQuery(String statement) {
		try {
			PreparedStatement st = this.con.prepareStatement(statement);
			return st.executeQuery();
		} catch (Exception e) {
			SkyCrystals.getInstance().getLogger().warning("An error occurred while executing query: " + e.getMessage());
		}
		return null;
	}
	  
	public ResultSet executeQuery(PreparedStatement statement) {
		try {
			return statement.executeQuery();
		} catch (Exception e) {
			SkyCrystals.getInstance().getLogger().warning("An error occurred while executing query: " + e.getMessage());
		}
		return null;
	}
	  
	public AsyncHandler getAsyncHandler() {
		return this.asyncHandler;
	}
	  
	public StayAliveTask getAliveTask() {
		return this.aliveTask;
	}
	  
	public Updater getUpdater() {
		return this.updater;
	}
}
