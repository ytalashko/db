package badcoders.model;

public class Recommendation {
    public final long userId;
    public final long filmId;
    public final double score;

    public Recommendation(long userId, long filmId, double score) {
        this.userId = userId;
        this.filmId = filmId;
        this.score = score;
    }
}
