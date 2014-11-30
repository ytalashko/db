package badcoders.logic.util;

/**
 * Utils.
 */
public class Utils {

    public static void checkArgument(boolean expression, String errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
