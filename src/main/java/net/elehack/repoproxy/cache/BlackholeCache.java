package net.elehack.repoproxy.cache;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Cache that doesn't cache anything.
 */
public class BlackholeCache implements RepositoryCache {
    @Override
    public boolean hasEntry(Path path) {
        return false;
    }

    @Override
    public Optional<CacheEntry> getEntry(String path) {
        return Optional.empty();
    }

    @Override
    public CacheEntryWriter openWriter(String path) {
        return new EntryWriter();
    }

    private static class EntryWriter implements CacheEntryWriter {
        @Override
        public void close() { }
    }
}
