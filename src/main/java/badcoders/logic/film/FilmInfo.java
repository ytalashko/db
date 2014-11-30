package badcoders.logic.film;

/**
 * General film information.
 */
public class FilmInfo {

    public final String name;
    public final double score;
    public final int numberOfVotes;

    public FilmInfo(String name, double score, int numberOfVotes) {
        this.name = name;
        this.score = score;
        this.numberOfVotes = numberOfVotes;
    }

    public double getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfVotes() {
        return numberOfVotes;
    }
}
