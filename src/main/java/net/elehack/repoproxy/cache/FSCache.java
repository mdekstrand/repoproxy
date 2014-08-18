package net.elehack.repoproxy.cache;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Cache that doesn't cache anything.
 */
public class FSCache implements RepositoryCache {
    private final Path dir;

    FSCache(Path pth) {
        dir = pth;
    }

    public Path getDirectory() {
        return dir;
    }

    @Override
    public boolean hasEntry(Path path) {
        return false;
    }

    @Override
    public Optional<CacheEntry> getEntry(String path) {
        return null;
    }

    @Override
    public CacheEntryWriter openWriter(String path) {
        return null;
    }
}
