package net.elehack.repoproxy;

import org.apache.http.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Request handler for the proxy server.
 */
public class ProxyRequestHandler implements HttpRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(ProxyRequestHandler.class);

    public void handle(HttpRequest request,
                       HttpResponse response,
                       HttpContext context)
            throws HttpException, IOException {
        RequestLine rl = request.getRequestLine();
        logger.info("request: {} {}", rl.getMethod(), rl.getUri());
        response.setStatusCode(200);
        response.setEntity(new StringEntity(rl.getUri()));
    }
}