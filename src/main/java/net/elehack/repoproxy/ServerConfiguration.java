package net.elehack.repoproxy;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import net.elehack.repoproxy.repo.Repository;
import net.elehack.repoproxy.repo.RepositoryBuilder;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ServerConfiguration {
    private Config config;
    private Optional<Path> cacheBase;
    private List<Repository> repositories;

    public ServerConfiguration(Config cfg) {
        config = cfg;

        cacheBase = Optional.ofNullable(config.getString("cache.baseDir"))
                            .map(Paths::get);
        repositories = config.getObjectList("repositories")
                             .stream()
                             .map(this::createRepository)
                             .collect(Collectors.toList());
    }

    public Config getConfig() {
        return config;
    }

    public List<Repository> getRepositories() {
        return repositories;
    }

    public Optional<Path> getCacheBase() {
        return cacheBase;
    }

    private Repository createRepository(ConfigObject co) {
        RepositoryBuilder rb = Repository.newBuilder();
        cacheBase.map(rb::setCacheRoot);
        rb.addConfig(co);
        return rb.build();
    }

    public static ServerConfiguration load(File configFile) {
        Config config = ConfigFactory.parseFile(configFile);
        return new ServerConfiguration(config);
    }
}
