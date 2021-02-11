package team16.literaryassociation.services.interfaces;

import team16.literaryassociation.model.Writer;

public interface WriterService {

    Writer saveWriter(Writer writer);
    Writer findByUsername(String username);
}
