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
        result = 31 * result + numberOfVotes;
        return result;
    }

    @Override
    public String toString() {
        return "FilmInfo{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", numberOfVotes=" + numberOfVotes +
                '}';
    }
}
