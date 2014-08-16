package net.elehack.repoproxy.repo;

import net.elehack.repoproxy.cache.RepositoryCache;
import net.elehack.repoproxy.layout.Layout;

import java.net.URI;

public class Repository {
    private String path;
    private URI uri;
    private Layout layout;
    private RepositoryCache cache;

    public static RepositoryBuilder newBuilder() {
        return new RepositoryBuilder();
    }

    Repository(String path, URI uri, Layout layout, RepositoryCache cache) {
        this.path = path;
        this.uri = uri;
        this.layout = layout;
        this.cache = cache;
    }

    public String getPath() {
        return path;
    }

    public URI getUri() {
        return uri;
    }

    public Layout getLayout() {
        return layout;
    }

    public RepositoryCache getCache() {
        return cache;
    }
}