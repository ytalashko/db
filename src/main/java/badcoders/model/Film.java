package badcoders.model;

/**
 * Film representation.
 */
public class Film {

    public final String name;
    public final String director;
    public final String actors;
    public final String genre;
    public final String description;
    public final double score;
    public final int numberOfVotes;

    public Film(String name, String director, String actors, String genre, String description,
                         double score, int numberOfVotes) {
        this.name = name;
        this.director = director;
        this.actors = actors;
        this.genre = genre;
        this.description = description;
        this.score = score;
        this.numberOfVotes = numberOfVotes;
    }

    public String getName() {
        return name;
    }

    public String getDirector() {
        return director;
    }

    public String getActors() {
        return actors;
    }

    public String getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }

    public double getScore() {
        return score;
    }

    public int getNumberOfVotes() {
        return numberOfVotes;
    }
}
