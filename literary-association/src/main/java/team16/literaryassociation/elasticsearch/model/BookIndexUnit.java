package team16.literaryassociation.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "books_index", shards = 1, replicas = 0)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookIndexUnit {

    @Id
    private Long id;

    @Field(type = FieldType.Text, store = true, analyzer = "serbian")
    private String title;

    @Field(type = FieldType.Text, store = true, analyzer = "serbian")
    private String writer;

    @Field(type = FieldType.Text, store = true, analyzer = "serbian")
    private String content;

    @Field(type = FieldType.Text, store = true)
    private String genre;

    //Po ovome se ne pretrazuje

    @Field(type = FieldType.Boolean, store = true)
    private boolean openAccess;


}
