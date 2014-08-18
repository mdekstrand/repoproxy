package net.elehack.repoproxy.repo;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import net.elehack.repoproxy.cache.RepositoryCache;
import net.elehack.repoproxy.layout.Layout;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Optional;

public class RepositoryBuilder {
    private Optional<Path> cacheRoot;
    private String name;
    private URI uri;
    private Layout layout;

    public RepositoryBuilder setCacheRoot(Path path) {
        cacheRoot = Optional.of(path);
        return this;
    }

    public RepositoryBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public RepositoryBuilder setUri(URI uri) {
        this.uri = uri;
        return this;
    }

    public RepositoryBuilder setLayout(Layout layout) {
        this.layout = layout;
        return this;
    }

    public RepositoryBuilder setLayout(String name) {
        return setLayout(Layout.getLayout(name));
    }

    public RepositoryBuilder addConfig(ConfigObject co) {
        Config cfg = co.toConfig();
        setName(cfg.getString("name"));
        try {
            setUri(new URI(cfg.getString("uri")));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid repository URI");
        }
        setLayout(cfg.hasPath("layout") ? cfg.getString("layout") : "raw");
        return this;
    }

    public Repository build() {
        RepositoryCache cache =
                cacheRoot.map((Path p) -> RepositoryCache.directory(p.resolve(name)))
                         .orElseGet(RepositoryCache::blackHole);
        return new Repository(name, uri, layout, cache);
    }
}
