package badcoders.database;

import badcoders.logic.account.Account;

import java.sql.*;

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
            "is_admin BOOL," +
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
        try {
            for (String schema : SCHEMAS) {
                createTable(connection, schema);
            }
        } finally {
            connection.close();
        }
    }

    /**
     * @retval null if given user not exists.
     */
    public Account getUser(String name, String password) throws SQLException {
        Connection connection = createConnection();
        final String query = "SELECT is_admin FROM user WHERE login = ? AND password = ?;";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, name);
        stmt.setString(2, password);
        ResultSet dbResult = stmt.executeQuery();
        Account result = null;
        if (!dbResult.isAfterLast()) {
            result = new Account(name, dbResult.getBoolean("is_admin"));
        }
        stmt.close();
        return result;
    }

    public void addUser(String name, String password, boolean is_admin, String email) throws SQLException {
        Connection connection = createConnection();
        final String query = "INSERT INTO user(login, password, is_admin, email) VALUES (?, ?, ?, ?);";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, name);
        stmt.setString(2, password);
        stmt.setBoolean(3, is_admin);
        stmt.setString(4, email);
        stmt.execute();
        stmt.close();
        connection.close();
    }

    private void createTable(Connection connection, String tableSchema) throws SQLException {
        Statement stmt = connection.createStatement();
        try {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableSchema);
        } finally {
            stmt.close();
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

    public static void main(String[] args) throws SQLException {
        Database db = new Database("test");
        db.createModel();
        db.addUser("rasen", "secret", true, "rasen.dubi@gmail.com");
        System.out.println(db.getUser("rasen", "secret"));
        System.out.println(db.getUser("rasen", "hack"));
    }
}
