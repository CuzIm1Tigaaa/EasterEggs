package de.cuzim1tigaaa.easter.db;

import de.cuzim1tigaaa.easter.utils.progress.Progress;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface Queries {

	CompletableFuture<Set<Progress>> getPlayerProgress(UUID uuid);

	CompletableFuture<Void> addProgress(UUID uuid, Progress progress);

	CompletableFuture<Void> removeProgress(UUID uuid, Progress progress);

	CompletableFuture<Void> removePlayer(UUID uuid);

}
