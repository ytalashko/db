package badcoders.logic.http.handler;

import badcoders.database.Database;
import badcoders.model.Account;
import badcoders.logic.util.Constants;
import badcoders.logic.util.Utils;
import co.cask.http.AbstractHttpHandler;
import co.cask.http.HttpResponder;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import java.sql.SQLException;

/**
 * Abstract handler that provides some base method for authenticating.
 */
public abstract class AbstractAuthHandler extends AbstractHttpHandler {

    protected AbstractAuthHandler() {
    }

    /**
     * Gets the user login and password from the request and authenticates,
     * returning null and writing an error message to the
     * responder if there was an error getting or authenticating the user.
     *
     * @param request Request containing the user login and password.
     * @param responder Responder to use when there is an issue getting or authenticating the user.
     * @return {@link Account} if it exists and authentication passes.
     */
    protected Account getAndAuthenticateAccount(HttpRequest request, HttpResponder responder) {
        String login = request.getHeader(Constants.LOGIN_HEADER);
        String password = request.getHeader(Constants.PASSWORD_HEADER);
        if (login == null) {
            responder.sendString(HttpResponseStatus.UNAUTHORIZED, String.format("%s not found in request headers.",
                    Constants.LOGIN_HEADER));
            return null;
        }
        if (password == null) {
            responder.sendString(HttpResponseStatus.UNAUTHORIZED, String.format("%S not found in request headers.",
                    Constants.PASSWORD_HEADER));
            return null;
        }

        try {
            Database db = Utils.getDatabase();
            return db.getUser(login, password);
        } catch (SQLException e) {
            responder.sendString(HttpResponseStatus.INTERNAL_SERVER_ERROR, "Unable to read data from the database");
            return null;
        }
    }
}
