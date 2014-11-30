package badcoders.logic.util;

import badcoders.database.Database;

import java.sql.SQLException;

/**
 * Utils.
 */
public class Utils {

    private static Database DATABASE;

    public static Database getDatabase() throws SQLException {
        if (DATABASE == null) {
            DATABASE = createDatabase();
        }
        return DATABASE;
    }

    private static Database createDatabase() throws SQLException {
        Database database = new Database(Constants.DATABASE_NAME);
        database.createModel();
        return database;
    }

    public static void checkArgument(boolean expression, String errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
