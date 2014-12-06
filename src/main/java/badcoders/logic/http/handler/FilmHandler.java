package badcoders.logic.http.handler;

import badcoders.database.Database;
import badcoders.logic.film.FilmInfo;
import badcoders.logic.film.FilmService;
import badcoders.logic.util.Constants;
import badcoders.logic.util.Utils;
import badcoders.model.Account;
import badcoders.model.Comment;
import badcoders.model.Film;
import co.cask.http.HttpResponder;
import com.google.common.base.Charsets;
import com.google.gson.reflect.TypeToken;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import javax.ws.rs.*;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLException;
import java.util.List;

/**
 * Handler for performing film operations.
 */
@Path(Constants.API_BASE + "/films")
public class FilmHandler extends AbstractAuthHandler {

    /**
     * Gets all films.
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
            responder.sendJson(HttpResponseStatus.OK, filmInfos,
                    new TypeToken<List<FilmInfo>>() { }.getType(), Utils.getGson());
        } catch (SQLException e) {
            responder.sendString(HttpResponseStatus.INTERNAL_SERVER_ERROR, "Unable to read data from the database");
        }
    }

    /**
     * Get a film by it's id.
     *
     * @param request Request for films.
     * @param responder Responder for sending the response.
     */
    @GET
    @Path("/{film-id}")
    public void getFilm(HttpRequest request, HttpResponder responder, @PathParam("film-id") long filmId) {
        Account account = getAndAuthenticateAccount(request, responder);
        if (account == null) {
            return;
        }

        try {
            Database db = Utils.getDatabase();
            Film film = db.getFilm(filmId);
            responder.sendJson(HttpResponseStatus.OK, film, Film.class, Utils.getGson());
        } catch (SQLException e) {
            responder.sendString(HttpResponseStatus.INTERNAL_SERVER_ERROR, "Unable to read data from the database");
        }
    }

    /**
     * Gets film comments.
     *
     * @param request Request for films.
     * @param responder Responder for sending the response.
     */
    @GET
    @Path("/{film-id}/comments")
    public void getComments(HttpRequest request, HttpResponder responder, @PathParam("film-id") String filmId) {
        Account account = getAndAuthenticateAccount(request, responder);
        if (account == null) {
            return;
        }

        try {
            Database db = Utils.getDatabase();
            List<Comment> comments = db.getFilmComments(Long.valueOf(filmId));
            responder.sendJson(HttpResponseStatus.OK, comments,
                    new TypeToken<List<Comment>>() { }.getType(), Utils.getGson());
        } catch (SQLException e) {
            responder.sendString(HttpResponseStatus.INTERNAL_SERVER_ERROR, "Unable to read data from the database");
        }
    }

    /**
     * Adds comment to film.
     *
     * @param request Request for films.
     * @param responder Responder for sending the response.
     */
    @POST
    @Path("/{film-id}/comments")
    public void addComment(HttpRequest request, HttpResponder responder, @PathParam("film-id") String filmId) {
        Account account = getAndAuthenticateAccount(request, responder);
        if (account == null) {
            return;
        }

        Reader reader = new InputStreamReader(new ChannelBufferInputStream(request.getContent()), Charsets.UTF_8);
        String text = Utils.getGson().fromJson(reader, String.class);

        try {
            Database db = Utils.getDatabase();
            Long id = db.addComment(account, Long.valueOf(filmId), text);
            responder.sendString(HttpResponseStatus.OK, id.toString());
        } catch (SQLException e) {
            responder.sendString(HttpResponseStatus.INTERNAL_SERVER_ERROR, "Unable to read data from the database");
        }
    }

    /**
     * Deletes a comment form specified film by comment's id.
     *
     * @param request Request for films.
     * @param responder Responder for sending the response.
     */
    @DELETE
    @Path("/{film-id}/comments/{comment-id}")
    public void deleteComment(HttpRequest request, HttpResponder responder, @PathParam("film-id") String filmId,
                              @PathParam("comment-id") String commentId) {
        Account account = getAndAuthenticateAccount(request, responder);
        if (account == null) {
            return;
        }

        try {
            Database db = Utils.getDatabase();
            Comment comment = db.getComment(Long.valueOf(commentId));
            if (!account.isAdmin && !db.getUser(comment.userId).login.equals(account.login)) {
                responder.sendString(HttpResponseStatus.METHOD_NOT_ALLOWED, "Can't delete comment of another user");
                return;
            }
            db.deleteComment(Long.valueOf(commentId));
            responder.sendStatus(HttpResponseStatus.OK);
        } catch (SQLException e) {
            responder.sendString(HttpResponseStatus.INTERNAL_SERVER_ERROR, "Unable to read data from the database");
        }
    }

    /**
     * Rates the film.
     *
     * @param request Request for films.
     * @param responder Responder for sending the response.
     */
    @POST
    @Path("/{film-id}/{rating}")
    public void rateFilm(HttpRequest request, HttpResponder responder, @PathParam("film-id") String filmId,
                         @PathParam("rating") String rating) {
        Account account = getAndAuthenticateAccount(request, responder);
        if (account == null) {
            return;
        }

        try {
            Database db = Utils.getDatabase();
            if (db.canRateFilm(account, Long.valueOf(filmId))) {
                db.rateFilm(account, Long.valueOf(filmId), Integer.valueOf(rating));
                responder.sendStatus(HttpResponseStatus.OK);
            } else {
                responder.sendString(HttpResponseStatus.BAD_REQUEST, "Can't rate film more than once");
            }
        } catch (SQLException e) {
            responder.sendString(HttpResponseStatus.INTERNAL_SERVER_ERROR, "Unable to read data from the database");
        }
    }

    /**
     * Retrieves user recommendation.
     *
     * @param request Request for films.
     * @param responder Responder for sending the response.
     */
    @POST
    @Path("/recommendation")
    public void getRecommendation(HttpRequest request, HttpResponder responder) {
        Account account = getAndAuthenticateAccount(request, responder);
        if (account == null) {
            return;
        }

        try {
            Database db = Utils.getDatabase();
            db.getRecommendation(account);
//            responder.sendStatus(HttpResponseStatus.OK);
        } catch (SQLException e) {
            responder.sendString(HttpResponseStatus.INTERNAL_SERVER_ERROR, "Unable to read data from the database");
        }
    }
}
