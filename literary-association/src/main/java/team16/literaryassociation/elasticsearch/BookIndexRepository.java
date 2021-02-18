package team16.literaryassociation.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BookIndexRepository extends ElasticsearchRepository<BookIndexUnit, Long> {
}
