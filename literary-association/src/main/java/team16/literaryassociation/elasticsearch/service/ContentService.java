package team16.literaryassociation.elasticsearch.service;

import java.io.IOException;
import java.net.MalformedURLException;

public interface ContentService {

    String getContent(String pdf) throws IOException;
}
