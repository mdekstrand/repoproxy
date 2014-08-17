package net.elehack.repoproxy;

import net.elehack.repoproxy.util.PathUtils;
import org.apache.http.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

/**
 * Request handler for the proxy server.
 */
public class ProxyRequestHandler implements HttpRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(ProxyRequestHandler.class);
    private final ServerConfiguration config;

    public ProxyRequestHandler(ServerConfiguration cfg) {
        if (cfg == null) {
            throw new NullPointerException("server configuration");
        }
        config = cfg;
    }

    public void handle(HttpRequest request,
                       HttpResponse response,
                       HttpContext context)
            throws HttpException, IOException {
        RequestLine rl = request.getRequestLine();
        logger.info("request: {} {}", rl.getMethod(), rl.getUri());

        Optional<String> path = PathUtils.normalize(rl.getUri());
        if (!path.isPresent() || !PathUtils.isAbsolute(path.get())) {
            response.setStatusCode(400);
            response.setEntity(new StringEntity("Invalid path\n", "UTF-8"));
            return;
        }

        switch (rl.getMethod().toUpperCase()) {
        case "GET":
            handleGet(path.get(), response);
            break;
        case "HEAD":
            handleHead(path.get(), response);
            break;
        default:
            throw new MethodNotSupportedException("invalid method " + rl.getMethod());
        }
    }

    private void handleGet(String path, HttpResponse response) {

    }

    private void handleHead(String path, HttpResponse response) {

    }
}