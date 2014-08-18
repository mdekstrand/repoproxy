package net.elehack.repoproxy.cache;

import java.nio.file.Path;
import java.util.Optional;

public interface RepositoryCache {
    boolean hasEntry(Path path);

    Optional<CacheEntry> getEntry(String path);

    CacheEntryWriter openWriter(String path);

    public static RepositoryCache blackHole() {
        return new BlackholeCache();
    }

    public static RepositoryCache directory(Path path) {
        return new FSCache(path);
    }
}
