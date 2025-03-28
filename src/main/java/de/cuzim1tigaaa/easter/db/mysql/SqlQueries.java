package de.cuzim1tigaaa.easter.db.mysql;

import de.cuzim1tigaaa.easter.EasterEggs;
import de.cuzim1tigaaa.easter.db.Queries;
import de.cuzim1tigaaa.easter.utils.progress.Progress;
import de.cuzim1tigaaa.easter.utils.reward.Reward;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

public class SqlQueries implements Queries {

	private final EasterEggs plugin;
	private final MySQL mysql;

	private final String database;

	public SqlQueries(EasterEggs plugin) throws ExecutionException, InterruptedException {
		this.plugin = plugin;
		this.mysql = new MySQL();

		this.database = "";
		this.createTable().get();
	}

	public CompletableFuture<Void> createTable() {
		return CompletableFuture.runAsync(() -> {
			Connection conn = null;
			PreparedStatement psCreate = null;

			try {
				conn = this.mysql.getConnection();
				psCreate = conn.prepareStatement(String.format(Query.CREATE_TABLE, this.database));
				psCreate.executeUpdate();
			} catch(SQLException exception) {
				this.plugin.getLogger().log(Level.SEVERE,
						"An error occurred while creating the table in the database!", exception);
			}finally {
				mysql.close(conn, psCreate, null);
			}
		});
	}

	@Override
	public CompletableFuture<Set<Progress>> getPlayerProgress(UUID uuid) {
		return CompletableFuture.supplyAsync(() -> {
			Connection conn = null;
			PreparedStatement psGetProgress = null;
			ResultSet rsGetProgress = null;
			Set<Progress> progressSet = new HashSet<>();

			try {
				conn = this.mysql.getConnection();
				psGetProgress = conn.prepareStatement(String.format(Query.GET_PLAYER_PROGRESS, this.database));
				psGetProgress.setString(1, uuid.toString());
				rsGetProgress = psGetProgress.executeQuery();

				while(rsGetProgress.next()) {
					Reward reward = Reward.fromBase64(rsGetProgress.getString("REWARD"));
					Progress progress = new Progress(UUID.fromString(rsGetProgress.getString("EGG_ID")),
							rsGetProgress.getTimestamp("FOUND_AT"), reward);
					progressSet.add(progress);
				}
			}catch(SQLException exception) {
				this.plugin.getLogger().log(Level.SEVERE,
						"An error occurred while getting the player progress from the database!", exception);
			}finally {
				this.mysql.close(conn, psGetProgress, rsGetProgress);
			}

			return progressSet;
		});
	}

	@Override
	public CompletableFuture<Void> addProgress(UUID uuid, Progress progress) {
		return null;
	}

	@Override
	public CompletableFuture<Void> removeProgress(UUID uuid, Progress progress) {
		return null;
	}

	@Override
	public CompletableFuture<Void> removePlayer(UUID uuid) {
		return null;
	}
}
