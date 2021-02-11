package team16.literaryassociation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.BookRequest;
import team16.literaryassociation.model.Editor;
import team16.literaryassociation.repository.EditorRepository;
import team16.literaryassociation.services.interfaces.BookRequestService;
import team16.literaryassociation.services.interfaces.EditorService;

import java.util.List;
import java.util.Random;

@Service
public class EditorServiceImpl implements EditorService {

    @Autowired
    private EditorRepository editorRepository;

    @Override
    public Editor findRandomEditor() {

        List<Editor> allEditors = this.editorRepository.findAll();
        Random rand = new Random();
        Editor editor = allEditors.get(rand.nextInt(allEditors.size()));
        return editor;
    }

    @Override
    public List<Editor> findAll() {
        return editorRepository.findAll();
    }

    @Override
    public Editor findById(Long id) {
        return editorRepository.findById(id).orElseGet(null);
    }
}
