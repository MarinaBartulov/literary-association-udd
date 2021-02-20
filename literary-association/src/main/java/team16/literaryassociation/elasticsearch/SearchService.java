package team16.literaryassociation.elasticsearch;

import java.util.List;

public interface SearchService {

    List<BetaReaderIndexUnit> findBetaReadersForGenre(String genre, String writerUsername);
}
