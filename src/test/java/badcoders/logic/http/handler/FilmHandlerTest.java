package badcoders.logic.http.handler;

import badcoders.database.Database;
import badcoders.logic.film.FilmInfo;
import badcoders.logic.http.HandlerServer;
import badcoders.logic.util.Constants;
import badcoders.logic.util.Utils;
import badcoders.model.Film;
import co.cask.http.HttpHandler;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.gson.reflect.TypeToken;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.junit.*;

import java.io.InputStreamReader;
import java.io.File;
import java.io.Reader;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests for {@link FilmHandler}
 */
public class FilmHandlerTest {

    private static final String DATABASE_NAME = "film_handler_test";

    private static String BASE;
    private static HandlerServer HANDLER_SERVER;
    private static Database DATABASE;

    @BeforeClass
    public static void mockDatabaseName() throws IllegalAccessException, NoSuchFieldException, SQLException {
        Database database = new Database(DATABASE_NAME);
        database.createModel();

        Field field = Utils.class.getDeclaredField("DATABASE");
        field.setAccessible(true);
        field.set(null, database);
    }

    @Before
    public void initialize() throws Exception {
        DATABASE = Utils.getDatabase();

        HANDLER_SERVER = new HandlerServer(Sets.newHashSet(ImmutableList.of((HttpHandler) new FilmHandler())),
                "localhost", 0);
        HANDLER_SERVER.startAndWait();
        BASE = String.format("http://%s:%s%s", "localhost",
                HANDLER_SERVER.getBindAddress().getPort(), Constants.API_BASE);
    }

    @After
    public void shutDown() throws Exception {
        HANDLER_SERVER.stopAndWait();
        new File(String.format("%s.sdb", DATABASE_NAME)).delete();
    }

    @Test
    public void getFilms() throws Exception {
        Header[] headers = {
                new BasicHeader(Constants.LOGIN_HEADER, "login"),
                new BasicHeader(Constants.PASSWORD_HEADER, "password")
        };

        DATABASE.addUser("login", "password", false, "email@tral.mode");

        List<Film> films = new ArrayList<>();
        films.add(new Film("Loha trall!", "Iurii", "Iurii, Alexey", "trash", "The most stupid film ever"));
        films.add(new Film("filmetz", "-", "no", "horror", "null"));

        List<FilmInfo> expected = new ArrayList<>();
        expected.add(new FilmInfo("Loha trall!", 0, 0));
        expected.add(new FilmInfo("filmetz", 0, 0));

        for (Film film : films) {
            DATABASE.addFilm(film);
        }

        HttpResponse response = doGet("/films", headers);
        Reader reader = new InputStreamReader(response.getEntity().getContent(), Charsets.UTF_8);
        List<FilmInfo> actual = Utils.getGson().fromJson(reader,
                new TypeToken<List<FilmInfo>>() { }.getType());

        Assert.assertEquals(expected.size(), actual.size());
        for (FilmInfo filmInfo : expected) {
            Assert.assertTrue(actual.contains(filmInfo));
        }
    }

    private static HttpResponse doGet(String resource, Header[] headers) throws Exception {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(BASE + resource);

        if (headers != null) {
            get.setHeaders(headers);
        }

        return client.execute(get);
    }
}
