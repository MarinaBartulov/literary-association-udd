package team16.literaryassociation.elasticsearch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import team16.literaryassociation.elasticsearch.model.BookIndexUnit;

public interface BookIndexRepository extends ElasticsearchRepository<BookIndexUnit, Long> {

    Page<BookIndexUnit> findAll();
}
