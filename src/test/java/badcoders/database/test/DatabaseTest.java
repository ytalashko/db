package badcoders.database.test;

import badcoders.database.Database;
import badcoders.model.Account;
import badcoders.model.Film;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DatabaseTest {

    Database db;

    @Before
    public void setUp() throws SQLException {
        db = new Database("test");
        db.createModel();
    }

    @After
    public void tearDown() {
        new File("test.sdb").delete();
    }

    @Test
    public void testUser() throws SQLException {
        db.addUser("rasen", "secret", true, "rasen.dubi@gmail.com");

        assertEquals(new Account("rasen", true), db.getUser("rasen", "secret"));
        assertEquals(null, db.getUser("rasen", "hack"));
    }

    @Test
    public void testFilm() throws SQLException {
        assertEquals(Collections.emptyList(), db.getFilms());

        List<Film> films = new ArrayList<>();
        films.add(new Film("Hello world!", "Iurii", "Iurii, Alexey", "trash", "The most stupid film ever"));
        films.add(new Film("null", "-", "no", "horror", "null"));

        for (Film film : films) {
            db.addFilm(film);
        }

        List<Film> result = db.getFilms();

        for (Film film : films) {
            assertTrue("Doesn't contain " + film, result.contains(film));
        }
        assertEquals(films.size(), result.size());

        for (Film film : result) {
            assertEquals(film, db.getFilm(film.getId()));
        }
    }

}
