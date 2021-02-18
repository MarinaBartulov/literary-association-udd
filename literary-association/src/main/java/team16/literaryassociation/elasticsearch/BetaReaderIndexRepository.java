package team16.literaryassociation.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BetaReaderIndexRepository extends ElasticsearchRepository<BetaReaderIndexUnit, Long> {
}
