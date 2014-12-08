package badcoders.logic.film;

/**
 * General film information.
 */
public class FilmInfo {

    public final long id;
    public final String name;
    public final double score;
    public final long numberOfVotes;

    public FilmInfo(long id, String name, double score, long numberOfVotes) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.numberOfVotes = numberOfVotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FilmInfo filmInfo = (FilmInfo) o;

        return numberOfVotes == filmInfo.numberOfVotes &&
                Double.compare(filmInfo.score, score) == 0 && name.equals(filmInfo.name);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name.hashCode();
        temp = Double.doubleToLongBits(score);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (numberOfVotes ^ (numberOfVotes >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "FilmInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", numberOfVotes=" + numberOfVotes +
                '}';
    }
}
