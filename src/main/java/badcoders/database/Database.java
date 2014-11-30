package badcoders.database;

import badcoders.logic.account.Account;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private final String name;

    public static final String filmSchema = "film (" +
            "id ROWID," +
            "name tinytext NOT NULL," +
            "director tinytext NOT NULL," +
            "actors text NOT NULL," +
            "genre tinytext NOT NULL," +
            "description text NOT NULL," +
            "total_score tinyint(4) NOT NULL," +
            "PRIMARY KEY (id));";

    private final static String commentSchema = "comment (" +
            "id ROWID," +
            "user_id int(11) NOT NULL," +
            "film_id int(11) NOT NULL," +
            "text text NOT NULL," +
            "PRIMARY KEY (id));";

    private final static String filmScoreSchema = "film_score (" +
            "id ROWID," +
            "film_id int(11) NOT NULL," +
            "user_id int(11) NOT NULL," +
            "score tinyint(4) NOT NULL," +
            "PRIMARY KEY (`id`));";

    private final static String recommendationSchema = "recommendation (" +
            "id ROWID," +
            "user_id int(11) NOT NULL," +
            "film_id int(11) NOT NULL," +
            "score tinyint(4) NOT NULL," +
            "PRIMARY KEY (id));";

    private final static String registrationSchema = "registration (" +
            "id ROWID," +
            "user_id int(11) NOT NULL," +
            "code bigint(20) NOT NULL UNIQUE," +
            "PRIMARY KEY (id));";

    private final static String userSchema = "user (" +
            "id ROWID," +
            "login tinytext NOT NULL," +
            "password tinytext NOT NULL," +
            "email tinytext NOT NULL," +
            "date_created timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            "PRIMARY KEY (id));";

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
        Connection connection = createConnection();
        for (String schema : SCHEMAS) {
            createTable(connection, schema);
        }
        connection.close();
    }

    /**
     * @retval null if given user not exists.
     */
    public Account getUser(String name, String password) {
        return null;
    }

    private void createTable(Connection connection, String tableSchema) throws SQLException {
        System.out.println("Creating schema: " + tableSchema);
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableSchema);
        stmt.close();
    }

    private Connection createConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:" + name + ".sdb");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws SQLException {
        new Database("test").createModel();
    }
}
