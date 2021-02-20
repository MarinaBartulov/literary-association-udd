package team16.literaryassociation.elasticsearch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import team16.literaryassociation.elasticsearch.model.BetaReaderIndexUnit;

public interface BetaReaderIndexRepository extends ElasticsearchRepository<BetaReaderIndexUnit, Long> {

    Page<BetaReaderIndexUnit> findAll();
}
