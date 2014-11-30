package badcoders.model;

/**
 * Comment representation.
 */
public class Comment {

    private final long userId;
    private final long filmId;
    private final String text;

    public Comment(long userId, long filmId, String text) {
        this.userId = userId;
        this.filmId = filmId;
        this.text = text;
    }

    public long getUserId() {
        return userId;
    }

    public long getFilmId() {
        return filmId;
    }

    public String getText() {
        return text;
    }
}
