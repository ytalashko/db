package badcoders.logic.http;

import co.cask.http.HttpHandler;
import co.cask.http.NettyHttpService;
import com.google.common.util.concurrent.AbstractIdleService;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * Netty service for running the server.
 */
public class HandlerServer extends AbstractIdleService {

    private final NettyHttpService httpService;

    public HandlerServer(Set<HttpHandler> handlers, String host, int port) {
        NettyHttpService.Builder builder = NettyHttpService.builder();
        builder.addHttpHandlers(handlers);

        builder.setHost(host);
        builder.setPort(port);

        this.httpService = builder.build();
    }

    @Override
    protected void startUp() throws Exception {
        httpService.startAndWait();
    }

    @Override
    protected void shutDown() throws Exception {
        httpService.stopAndWait();
    }

    /**
     * Get the address the service has bound to.
     *
     * @return address the service has bound to.
     */
    public InetSocketAddress getBindAddress() {
        return httpService.getBindAddress();
    }
}
