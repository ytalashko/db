package badcoders.model;

/**
 * Film representation.
 */
public class Film {

    public final long id;
    public final String name;
    public final String director;
    public final String actors;
    public final String genre;
    public final String description;
    public final double score;
    public final int numberOfVotes;

    public Film(long id, String name, String director, String actors, String genre, String description,
                         double score, int numberOfVotes) {
        this.id = id;
        this.name = name;
        this.director = director;
        this.actors = actors;
        this.genre = genre;
        this.description = description;
        this.score = score;
        this.numberOfVotes = numberOfVotes;
    }

    public Film(String name, String director, String actors, String genre, String description) {
        this(0, name, director, actors, genre, description, 0, 0);
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Film film = (Film) o;

        return actors.equals(film.actors) && description.equals(film.description) &&
                director.equals(film.director) && genre.equals(film.genre) && name.equals(film.name);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + director.hashCode();
        result = 31 * result + actors.hashCode();
        result = 31 * result + genre.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }
}
