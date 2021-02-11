package team16.literaryassociation.services.interfaces;

import org.springframework.web.multipart.MultipartFile;
import team16.literaryassociation.model.LiteraryWork;
import org.springframework.core.io.Resource;

import java.util.List;

public interface LiteraryWorkService {

    LiteraryWork save(LiteraryWork literaryWork);
    String store(MultipartFile file, String processId, String username);
    Resource downloadFile(String processId, String username, String fileName);
    List<LiteraryWork> findAllWithMembershipApplicationId(Long id);
}
