package net.elehack.repoproxy.cache;

import java.nio.file.Path;

public interface RepositoryCache {
    boolean hasEntry(Path path);

    public static RepositoryCache blackHole() {
        return new BlackholeCache();
    }

    public static RepositoryCache directory(Path pth) {
        return null;
    }
}
