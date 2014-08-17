package net.elehack.repoproxy;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpException;
import org.apache.http.HttpServerConnection;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;

public class RequestLoop implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(RequestLoop.class);

    private final InetAddress client;
    private final HttpService service;
    private final HttpServerConnection connection;

    public RequestLoop(HttpService svc, HttpServerConnection cxn, InetAddress address) {
        client = address;
        service = svc;
        connection = cxn;
    }

    @Override
    public void run() {
        logger.info("servicing requests from client {}", client);
        HttpContext ctx = new BasicHttpContext(null);
        try {
            while (!Thread.interrupted() && connection.isOpen()) {
                service.handleRequest(connection, ctx);
            }
        } catch (ConnectionClosedException e) {
            logger.info("client {} closed connection", client);
        } catch (HttpException e) {
            logger.error("HTTP error serving requests", e);
        } catch (IOException e) {
            logger.error("I/O error serving requests", e);
        } catch (RuntimeException e) {
            logger.error("Internal error serving requests", e);
        } finally {
            try {
                connection.shutdown();
            } catch (IOException ex) {
                logger.error("Error shutting down connection", ex);
            }
        }
    }
}
