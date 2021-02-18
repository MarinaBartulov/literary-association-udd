package team16.literaryassociation;

import com.byteowls.jopencage.JOpenCageGeocoder;
import com.byteowls.jopencage.model.JOpenCageForwardRequest;
import com.byteowls.jopencage.model.JOpenCageLatLng;
import com.byteowls.jopencage.model.JOpenCageResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
@EnableScheduling
public class LiteraryAssociationApplication {

    public static void main(String[] args) {
        createDir();
//        JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder("d3ab24f7cbe84dc7b61a1be57b553ae6");
//        JOpenCageForwardRequest request = new JOpenCageForwardRequest("Belgrade, Serbia");
//        JOpenCageResponse response = jOpenCageGeocoder.forward(request);
//        JOpenCageLatLng firstResultLatLng = response.getFirstPosition();
//        System.out.println(firstResultLatLng.getLat());
//        System.out.println(firstResultLatLng.getLng());

        SpringApplication.run(LiteraryAssociationApplication.class, args);
    }


    private static void createDir() {
        try {
            if (Files.exists(Paths.get("uploaded-files"))) {
                FileSystemUtils.deleteRecursively(Paths.get("uploaded-files").toFile());
            }
            Files.createDirectory(Paths.get("uploaded-files"));
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}
