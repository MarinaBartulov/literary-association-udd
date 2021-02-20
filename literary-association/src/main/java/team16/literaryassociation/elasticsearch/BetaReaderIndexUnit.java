package team16.literaryassociation.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Document(indexName = "beta_readers_index", shards = 1, replicas = 0)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BetaReaderIndexUnit {

    @Id
    private Long id;

    @Field(type = FieldType.Long, store = true)
    private Long betaReaderId;

    @Field(type = FieldType.Text, store = true, analyzer = "serbian")
    private String fullName;

    @Field(type = FieldType.Text, store = true, analyzer = "serbian")
    private String genres;

    @GeoPointField
    private GeoPoint location;


}
