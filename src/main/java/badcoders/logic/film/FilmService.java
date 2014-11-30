package badcoders.logic.film;

import badcoders.logic.util.Utils;
import badcoders.model.Film;
import badcoders.model.FilmScore;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for performing film operations.
 */
public class FilmService {

    public static List<FilmInfo> getFilmsInfo(List<Film> films) throws SQLException {
        List<FilmInfo> filmInfos = new ArrayList<>(films.size());
        for (Film film : films) {
            filmInfos.add(new FilmInfo(film.getName(),
                    getFilmScore(Utils.getDatabase().getFilmScores()),
                    films.size()));
        }
        return filmInfos;
    }

    public static double getFilmScore(List<FilmScore> scores) {
        if (scores.isEmpty()) {
            return 0;
        }
        double totalScore = 0;
        for (FilmScore score : scores) {
            totalScore += score.getScore();
        }
        return totalScore/scores.size();
    }
}
