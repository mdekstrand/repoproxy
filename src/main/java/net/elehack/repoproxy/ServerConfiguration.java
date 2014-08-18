package net.elehack.repoproxy;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import net.elehack.repoproxy.repo.Repository;
import net.elehack.repoproxy.repo.RepositoryBuilder;
import net.elehack.repoproxy.util.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ServerConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(ServerConfiguration.class);

    private Config config;
    private Optional<Path> cacheBase;
    private Map<String,Repository> repositories;

    public ServerConfiguration(Config cfg) {
        config = cfg;

        cacheBase = getStringValue("cache.baseDir").map(Paths::get);

        if (config.hasPath("repositories")) {
            repositories = config.getObjectList("repositories")
                                 .stream()
                                 .map(this::createRepository)
                                 .collect(Collectors.toMap(Repository::getName,
                                                           Function.identity()));
        } else {
            repositories = Collections.emptyMap();
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
        return repositories.values().stream().collect(Collectors.toList());
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

    public Optional<Repository> findRepository(String path) {
        String search = PathUtils.tokenize(path)
                                 .stream()
                                 .filter((String s) -> !s.isEmpty())
                                 .findFirst()
                                 .orElseThrow(() -> new IllegalArgumentException(path));
        return Optional.ofNullable(repositories.get(search));
    }

    private Repository createRepository(ConfigObject co) {
        RepositoryBuilder rb = Repository.newBuilder();
        if (cacheBase.isPresent()) {
            rb.setCacheRoot(cacheBase.get());
        }
        rb.addConfig(co);
        return rb.build();
    }

    public static ServerConfiguration load(File configFile) {
        logger.info("loading configuration from {}", configFile);
        Config config = ConfigFactory.parseFile(configFile);
        return new ServerConfiguration(config);
    }
}
