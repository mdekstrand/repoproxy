package net.elehack.repoproxy;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import net.elehack.repoproxy.repo.Repository;
import net.elehack.repoproxy.repo.RepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ServerConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(ServerConfiguration.class);

    private Config config;
    private Optional<Path> cacheBase;
    private List<Repository> repositories;

    public ServerConfiguration(Config cfg) {
        config = cfg;

        cacheBase = getStringValue("cache.baseDir").map(Paths::get);

        if (config.hasPath("repositories")) {
            repositories = config.getObjectList("repositories")
                                 .stream()
                                 .map(this::createRepository)
                                 .collect(Collectors.toList());
        } else {
            repositories = Collections.emptyList();
        }
    }

    public Config getConfig() {
        return config;
    }

    public Optional<String> getStringValue(String path) {
        if (config.hasPath(path)) {
            return Optional.of(config.getString(path));
        } else {
            return Optional.empty();
        }
    }

    public List<Repository> getRepositories() {
        return repositories;
    }

    public Optional<Path> getCacheBase() {
        return cacheBase;
    }

    public int getPort() {
        if (config.hasPath("server.port")) {
            return config.getInt("server.port");
        } else {
            return 4080;
        }
    }

    private Repository createRepository(ConfigObject co) {
        RepositoryBuilder rb = Repository.newBuilder();
        cacheBase.map(rb::setCacheRoot);
        rb.addConfig(co);
        return rb.build();
    }

    public static ServerConfiguration load(File configFile) {
        logger.info("loading configuration from {}", configFile);
        Config config = ConfigFactory.parseFile(configFile);
        return new ServerConfiguration(config);
    }
}
