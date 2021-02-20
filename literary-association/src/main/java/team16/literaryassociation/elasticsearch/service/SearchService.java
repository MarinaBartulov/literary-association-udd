package team16.literaryassociation.elasticsearch.service;

import team16.literaryassociation.elasticsearch.model.BetaReaderIndexUnit;

import java.util.List;

public interface SearchService {

    List<BetaReaderIndexUnit> findBetaReadersForGenre(String genre, String writerUsername);
}
