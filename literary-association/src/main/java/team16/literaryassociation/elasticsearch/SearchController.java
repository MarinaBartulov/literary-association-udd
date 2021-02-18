package team16.literaryassociation.elasticsearch;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/search")
public class SearchController {



    @GetMapping(value = "/indexBooks")
    public ResponseEntity indexBooks() {

        return ResponseEntity.ok().build();

    }

    @GetMapping(value = "/indexBetaReaders")
    public ResponseEntity indexBetaReaders() {

        return ResponseEntity.ok().build();

    }


}
