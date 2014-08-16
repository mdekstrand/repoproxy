package net.elehack.repoproxy.cache;

import java.nio.file.Path;

/**
 * Cache that doesn't cache anything.
 */
public class FSCache implements RepositoryCache {
    @Override
    public boolean hasEntry(Path path) {
        return false;
    }
}
