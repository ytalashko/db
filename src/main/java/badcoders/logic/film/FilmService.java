package badcoders.logic.film;

import badcoders.model.Film;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for performing film operations.
 */
public class FilmService {

    public static List<FilmInfo> getFilmsInfo(List<Film> films) {
        List<FilmInfo> filmInfos = new ArrayList<>(films.size());
        for (Film film : films) {
            filmInfos.add(new FilmInfo(film.getName(),
                    film.getScore(),
                    film.getNumberOfVotes()));
        }
        return filmInfos;
    }
}
