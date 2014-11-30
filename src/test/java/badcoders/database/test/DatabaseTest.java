package badcoders.database.test;

import badcoders.database.Database;
import badcoders.model.Account;
import badcoders.model.Film;
import junit.framework.TestCase;

import static junit.framework.Assert.*;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseTest extends TestCase {

    Database db;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        db = new Database("test");
        db.createModel();
    }

    @Override
    public void tearDown() throws Exception {
        new File("test.sdb").delete();
        super.tearDown();
    }

    public void testUser() throws SQLException {
        db.addUser("rasen", "secret", true, "rasen.dubi@gmail.com");

        assertEquals(new Account("rasen", true), db.getUser("rasen", "secret"));
        assertEquals(null, db.getUser("rasen", "hack"));
    }

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
    }

}
