package badcoders;

import badcoders.logic.http.HandlerServer;
import badcoders.logic.http.handler.FileHandler;
import badcoders.logic.http.handler.FilmHandler;
import badcoders.logic.util.Constants;
import badcoders.logic.util.Utils;
import badcoders.model.Film;
import co.cask.http.HttpHandler;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, SQLException {
        final CountDownLatch shutdownLatch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                shutdownLatch.countDown();
            }
        });

        HandlerServer server = new HandlerServer(Sets.newHashSet(ImmutableList.of((HttpHandler) new FileHandler(),
                new FilmHandler())),
                "localhost", 27200);
        server.startAndWait();
        System.out.println(String.format("http://%s:%s%s", "localhost",
                server.getBindAddress().getPort(), Constants.API_BASE));

        shutdownLatch.await();
    }
}
