package team16.literaryassociation.services.interfaces;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;

public interface UploadDownloadService {

    String uploadFile(MultipartFile file, String processId, int counter);
    Resource downloadFile(String filePath) throws MalformedURLException;
}
