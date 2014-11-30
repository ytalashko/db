package badcoders.database;

import badcoders.model.Account;
import badcoders.model.Comment;
import badcoders.model.Film;
import badcoders.model.FilmStats;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private final String name;

    public static final String filmSchema = "film (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name tinytext NOT NULL," +
            "director tinytext NOT NULL," +
            "actors text NOT NULL," +
            "genre tinytext NOT NULL," +
            "description text NOT NULL);";

    private final static String commentSchema = "comment (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "user_id int(11) NOT NULL," +
            "film_id int(11) NOT NULL," +
            "text text NOT NULL);";

    private final static String filmScoreSchema = "film_score (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "film_id int(11) NOT NULL," +
            "user_id int(11) NOT NULL," +
            "score tinyint(4) NOT NULL);";

    private final static String recommendationSchema = "recommendation (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "user_id int(11) NOT NULL," +
            "film_id int(11) NOT NULL," +
            "score tinyint(4) NOT NULL);";

    private final static String registrationSchema = "registration (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "user_id int(11) NOT NULL," +
            "code bigint(20) NOT NULL UNIQUE);";

    private final static String userSchema = "user (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "login tinytext NOT NULL," +
            "password tinytext NOT NULL," +
            "email tinytext NOT NULL," +
            "date_created timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            "is_admin BOOL);";

    public static final String[] SCHEMAS = new String[]{
            commentSchema,
            filmSchema,
            filmScoreSchema,
            recommendationSchema,
            registrationSchema,
            userSchema
    };

    public Database(String name) {
        this.name = name;
    }

    public void createModel() throws SQLException {
        try (Connection connection = createConnection()) {
            for (String schema : SCHEMAS) {
                createTable(connection, schema);
            }
        }
    }

    /**
     * @retval null if given user not exists.
     */
    public Account getUser(String name, String password) throws SQLException {
        Connection connection = createConnection();
        final String query = "SELECT id, is_admin FROM user WHERE login = ? AND password = ?;";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, name);
        stmt.setString(2, password);
        ResultSet dbResult = stmt.executeQuery();
        Account result = null;
        if (!dbResult.isAfterLast()) {
            result = new Account(dbResult.getLong("id"), name, dbResult.getBoolean("is_admin"));
        }
        stmt.close();
        return result;
    }

    public long addUser(String name, String password, boolean is_admin, String email) throws SQLException {
        try (Connection connection = createConnection()) {
            final String query = "INSERT INTO user(login, password, is_admin, email) VALUES (?, ?, ?, ?);";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setString(2, password);
                stmt.setBoolean(3, is_admin);
                stmt.setString(4, email);
                stmt.execute();

                ResultSet dbResult = stmt.getGeneratedKeys();
                return dbResult.getLong(1);
            }
        }
    }

    public Account getUser(long id) throws SQLException {
        try (Connection connection = createConnection()) {
            final String query = "SELECT * FROM user WHERE id = ?;";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, id);

                ResultSet dbResult = stmt.executeQuery();
                if (dbResult.isAfterLast()) {
                    return null;
                }

                return new Account(dbResult.getLong("id"), dbResult.getString("login"), dbResult.getBoolean("is_admin"));
            }
        }
    }

    public FilmStats getFilmStats(long id) throws SQLException {
        try (Connection connection = createConnection()) {
            final String query = "SELECT AVG(score) AS mean_score, COUNT(*) AS vote_count FROM film_score WHERE film_id = ?;";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, id);
                ResultSet dbResult = stmt.executeQuery();
                if (dbResult.isAfterLast()) {
                    return null;
                }

                FilmStats result = new FilmStats();
                result.mean_score = dbResult.getDouble("mean_score");
                result.vote_count = dbResult.getLong("vote_count");
                return result;
            }
        }
    }

    /**
     * @return list of all films.
     */
    public List<Film> getFilms() throws SQLException {
        try (Connection connection = createConnection()) {
            try (Statement stmt = connection.createStatement()) {
                final String query = "SELECT * FROM film";
                ResultSet dbResult = stmt.executeQuery(query);

                ArrayList<Film> result = new ArrayList<>();
                while (dbResult.next()) {
                    FilmStats stats = getFilmStats(dbResult.getLong("id"));
                    result.add(new Film(
                            dbResult.getLong("id"),
                            dbResult.getString("name"),
                            dbResult.getString("director"),
                            dbResult.getString("actors"),
                            dbResult.getString("genre"),
                            dbResult.getString("description"),
                            stats.mean_score,
                            stats.vote_count
                    ));
                }

                return result;
            }
        }
    }

    public long addFilm(Film film) throws SQLException {
        try (Connection connection = createConnection()) {
            final String query = "INSERT INTO film(name, director, actors, genre, description) VALUES(?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, film.getName());
                stmt.setString(2, film.getDirector());
                stmt.setString(3, film.getActors());
                stmt.setString(4, film.getGenre());
                stmt.setString(5, film.getDescription());
                stmt.execute();

                return stmt.getGeneratedKeys().getLong(1);
            }
        }
    }

    public Film getFilm(long id) throws SQLException {
        try (Connection connection = createConnection()) {
            final String query = "SELECT * FROM film WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, id);
                ResultSet dbResult = stmt.executeQuery();

                if (dbResult.isAfterLast()) {
                    return null;
                }

                FilmStats stats = getFilmStats(dbResult.getLong("id"));
                return new Film(
                        dbResult.getLong("id"),
                        dbResult.getString("name"),
                        dbResult.getString("director"),
                        dbResult.getString("actors"),
                        dbResult.getString("genre"),
                        dbResult.getString("description"),
                        stats.mean_score,
                        stats.vote_count
                );
            }
        }
    }

    public long addComment(Account account, long filmId, String text) throws SQLException {
        try (Connection connection = createConnection()) {
            final String query = "INSERT INTO comment(film_id, user_id, text) VALUES (?, ? ,?);";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, filmId);
                stmt.setLong(2, account.getId());
                stmt.setString(3, text);
                stmt.execute();

                return stmt.getGeneratedKeys().getLong(1);
            }
        }
    }

    public Comment getComment(long id) throws SQLException {
        try (Connection connection = createConnection()) {
            final String query = "SELECT * FROM comment WHERE id = ?;";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, id);
                ResultSet dbResult = stmt.executeQuery();

                if (dbResult.isAfterLast()) {
                    return null;
                }

                return new Comment(dbResult.getLong("id"), dbResult.getLong("user_id"),
                        dbResult.getLong("film_id"), dbResult.getString("text"));
            }
        }
    }

    public void deleteComment(long id) throws SQLException {
        try (Connection connection = createConnection()) {
            final String query = "DELETE FROM comment WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, id);
                stmt.execute();
            }
        }
    }

    private void createTable(Connection connection, String tableSchema) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableSchema);
        }
    }

    private Connection createConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:" + name + ".sdb");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
