package net.elehack.repoproxy;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.annotation.Arg;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpServerConnection;
import org.apache.http.impl.DefaultBHttpServerConnectionFactory;
import org.apache.http.protocol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RepoProxy implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RepoProxy.class);

    @Arg(dest="config_file")
    File configFile;

    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("repoproxy");
        parser.addArgument("--config-file", "-c")
                .metavar("FILE")
                .type(File.class)
                .required(true)
                .help("read configuration from FILE");

        RepoProxy proxy = new RepoProxy();
        try {
            parser.parseArgs(args, proxy);
        } catch (ArgumentParserException e) {
            System.err.println("error parsing command line");
            System.err.println(e.getMessage());
            System.exit(2);
        }
        proxy.run();
    }

    ServerConfiguration config;
    DefaultBHttpServerConnectionFactory factory;
    HttpService service;
    ExecutorService pool;

    @Override
    public void run() {
        config = ServerConfiguration.load(configFile);

        service = createService();
        factory = DefaultBHttpServerConnectionFactory.INSTANCE;

        pool = Executors.newCachedThreadPool();

        try {
            try (ServerSocket server = new ServerSocket(config.getPort())) {
                logger.info("listening on {}:{}",
                            server.getInetAddress().getHostAddress(),
                            server.getLocalPort());

                handleConnections(server);
            }
        } catch (IOException e) {
            logger.error("I/O error in server loop", e);
            throw new RuntimeException("Server error", e);
        } finally {
            pool.shutdown();
        }
    }

    private HttpService createService() {
        HttpProcessor proc =
                HttpProcessorBuilder.create()
                                    .add(new ResponseDate())
                                    .add(new ResponseServer("RepoProxy/0.1-SNAPSHOT"))
                                    .add(new ResponseContent())
                                    .add(new ResponseConnControl())
                                    .build();
        return new HttpService(proc, (HttpRequest req) -> new ProxyRequestHandler(config));
    }

    private void handleConnections(ServerSocket server) throws IOException {
        while (!Thread.interrupted()) {
            Socket socket = server.accept();
            HttpServerConnection cxn = factory.createConnection(socket);
            pool.submit(new RequestLoop(service, cxn, socket.getInetAddress()));
        }
    }
}
