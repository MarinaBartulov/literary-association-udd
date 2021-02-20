package team16.literaryassociation.elasticsearch.service;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ContentServiceImpl implements ContentService {


    @Override
    public String getContent(String pdf) {

        Path path = Paths.get(pdf);
        String content = "";
        try {
            Resource resource = new UrlResource(path.toUri());
            File file = resource.getFile();
            System.out.println(file.getPath());
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            PDFParser parser = new PDFParser(randomAccessFile);
            parser.parse();
            COSDocument cosDoc = parser.getDocument();
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            PDDocument pdDoc = new PDDocument(cosDoc);
            pdfTextStripper.setStartPage(1);
            pdfTextStripper.setEndPage(pdDoc.getNumberOfPages());
            content = pdfTextStripper.getText(pdDoc);
            pdDoc.close();
            return content;

        }catch(IOException exception){
            exception.printStackTrace();
            return null;
        }

    }
}
