package net.elehack.repoproxy.cache;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Cache that doesn't cache anything.
 */
class FSCache implements RepositoryCache {
    private final Path root;

    FSCache(Path pth) {
        root = pth;
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
