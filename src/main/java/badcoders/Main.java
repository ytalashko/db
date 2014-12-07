package badcoders;

import badcoders.logic.http.HandlerServer;
import badcoders.logic.http.handler.FilmHandler;
import badcoders.logic.util.Constants;
import co.cask.http.HttpHandler;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

public class Main {

    public static void main(String[] args) {
        HandlerServer server = new HandlerServer(Sets.newHashSet(ImmutableList.of((HttpHandler) new FilmHandler())),
                "localhost", 27200);
        server.startAndWait();
        System.out.println(String.format("http://%s:%s%s", "localhost",
                server.getBindAddress().getPort(), Constants.API_BASE));

        while (true) {
        }
    }
}
