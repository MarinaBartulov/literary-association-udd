package team16.literaryassociation.services.interfaces;

import team16.literaryassociation.model.Editor;

import java.util.List;

public interface EditorService {

    Editor findRandomEditor();
    List<Editor> findAll();
    Editor findById(Long id);
}
