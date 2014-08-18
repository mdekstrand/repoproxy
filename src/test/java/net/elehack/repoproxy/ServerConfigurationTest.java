package net.elehack.repoproxy;

import com.typesafe.config.ConfigFactory;
import net.elehack.repoproxy.cache.FSCache;
import net.elehack.repoproxy.repo.Repository;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class ServerConfigurationTest {
    ServerConfiguration config;

    @Before
    public void loadConfiguration() {
        config = new ServerConfiguration(ConfigFactory.load("full-testing.conf"));
    }

    @Test
    public void testEmptyConfig() {
        ServerConfiguration config =
                new ServerConfiguration(ConfigFactory.load("empty.conf"));
        assertThat(config.getPort(),
                   equalTo(4080));
        assertThat(config.getCacheBase(),
                   equalTo(Optional.empty()));
        assertThat(config.getRepositories(),
                   hasSize(0));
    }

    @Test
    public void testNoRepositories() {
        ServerConfiguration config =
                new ServerConfiguration(ConfigFactory.load("no-repos.conf"));
        assertThat(config.getPort(),
                   equalTo(8080));
        assertThat(config.getCacheBase(),
                   equalTo(Optional.of(Paths.get("/tmp/repocache"))));
        assertThat(config.getRepositories(),
                   hasSize(0));
    }

    @Test
    public void testFullConfig() {
        assertThat(config.getPort(),
                   equalTo(8080));
        assertThat(config.getCacheBase(),
                   equalTo(Optional.of(Paths.get("/tmp/repocache"))));
        assertThat(config.getRepositories(),
                   hasSize(2));
    }

    @Test
    public void testFindRepository() {
        assertThat(config.findRepository("/foo").orElse(null),
                   nullValue());

        Repository repo = config.findRepository("maven").orElse(null);
        assertThat(repo, notNullValue());
        assertThat(repo.getName(), equalTo("maven"));
        assertThat(repo.getCache(), instanceOf(FSCache.class));
        FSCache cache = (FSCache) repo.getCache();
        assertThat(cache.getDirectory(),
                   equalTo(Paths.get("/tmp/repocache/maven")));
    }
}
