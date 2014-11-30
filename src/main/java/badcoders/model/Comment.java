package badcoders.model;

/**
 * Comment representation.
 */
public class Comment {

    public final long id;
    public final long userId;
    public final long filmId;
    public final String text;

    public Comment(long id, long userId, long filmId, String text) {
        this.id = id;
        this.userId = userId;
        this.filmId = filmId;
        this.text = text;
    }
}
