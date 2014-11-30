package badcoders.logic.util;

import badcoders.database.Database;
import com.google.gson.Gson;

import java.sql.SQLException;

public class Utils {

    private static final Gson GSON = new Gson();

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

    public static Gson getGson() {
        return GSON;
    }


    public static void checkArgument(boolean expression, String errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
