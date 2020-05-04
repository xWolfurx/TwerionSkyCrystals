package net.twerion.skycrystals.database;

import net.twerion.skycrystals.SkyCrystals;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncHandler {

	private ExecutorService executor;
	
	public AsyncHandler() {
		this.executor = Executors.newCachedThreadPool();
	}
	
	public ExecutorService getExecutor() {
		return this.executor;
	}
	
	public void update(final String statement) {
		this.executor.execute(new Runnable() {
			public void run() {
                SkyCrystals.getInstance().getSQLManager().executeUpdate(statement);
			}
		});
	}
	
	public void update(final PreparedStatement statement) {
		this.executor.execute(new Runnable() {
			public void run() {
				SkyCrystals.getInstance().getSQLManager().executeUpdate(statement);
			}
		});
	}
	
	public void query(final PreparedStatement statement, final Callback<ResultSet> callback) {
		this.executor.execute(new Runnable() {
			public void run() {
				callback.accept(SkyCrystals.getInstance().getSQLManager().executeQuery(statement));
			}
		});
	}
	
}
