package team16.literaryassociation;

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
        SpringApplication.run(LiteraryAssociationApplication.class, args);
    }

    /*private static void createDir() {
        try {
            if (Files.exists(Paths.get("literary-works-dir"))) {
                FileSystemUtils.deleteRecursively(Paths.get("literary-works-dir").toFile());
            }
            Files.createDirectory(Paths.get("literary-works-dir"));
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }*/
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
