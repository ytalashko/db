package badcoders.logic.http.handler;

import badcoders.database.Database;
import badcoders.logic.film.FilmInfo;
import badcoders.logic.film.FilmService;
import badcoders.logic.util.Constants;
import badcoders.logic.util.Utils;
import badcoders.model.Account;
import badcoders.model.Film;
import co.cask.http.HttpResponder;
import com.google.gson.reflect.TypeToken;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.sql.SQLException;
import java.util.List;

/**
 * Handler for performing film operations.
 */
@Path(Constants.API_BASE + "/films")
public class FilmHandler extends AbstractAuthHandler {

    /**
     * Get all clusters visible to the user.
     *
     * @param request Request for films.
     * @param responder Responder for sending the response.
     */
    @GET
    public void getFilms(HttpRequest request, HttpResponder responder) {
        Account account = getAndAuthenticateAccount(request, responder);
        if (account == null) {
            return;
        }

        try {
            Database db = Utils.getDatabase();
            List<Film> films = db.getFilms();
            List<FilmInfo> filmInfos = FilmService.getFilmsInfo(films);
            responder.sendJson(HttpResponseStatus.OK, filmInfos, new TypeToken<List<FilmInfo>>() { }.getType());
        } catch (SQLException e) {
            responder.sendString(HttpResponseStatus.INTERNAL_SERVER_ERROR, "Unable to read data from the database");
        }
    }
}
