package team16.literaryassociation.elasticsearch;

import com.byteowls.jopencage.JOpenCageGeocoder;
import com.byteowls.jopencage.model.JOpenCageForwardRequest;
import com.byteowls.jopencage.model.JOpenCageLatLng;
import com.byteowls.jopencage.model.JOpenCageResponse;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.Writer;
import team16.literaryassociation.services.interfaces.WriterService;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private WriterService writerService;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Override
    public List<BetaReaderIndexUnit> findBetaReadersForGenre(String genre, String writerUsername) {

        Writer writer = this.writerService.findByUsername(writerUsername);
        JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder("d3ab24f7cbe84dc7b61a1be57b553ae6");
        String query = writer.getCity() + ", " + writer.getCountry();
        JOpenCageForwardRequest request = new JOpenCageForwardRequest(query);
        JOpenCageResponse response = jOpenCageGeocoder.forward(request);
        JOpenCageLatLng locationLatLng = response.getFirstPosition();

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.mustNot(QueryBuilders.geoDistanceQuery("location").geoDistance(GeoDistance.ARC).point(locationLatLng.getLat(),locationLatLng.getLng()).distance("100km"));
        queryBuilder.must(QueryBuilders.queryStringQuery(genre).field("genres"));
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
        List<BetaReaderIndexUnit> betaReadersFound = this.elasticsearchTemplate.queryForList(searchQuery, BetaReaderIndexUnit.class);
        System.out.println(betaReadersFound.size());
        for(BetaReaderIndexUnit bt : betaReadersFound){
            System.out.println("Found " + bt.getFullName());
        }
        return betaReadersFound;
    }
}
