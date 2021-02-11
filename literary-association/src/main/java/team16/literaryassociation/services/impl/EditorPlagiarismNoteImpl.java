package team16.literaryassociation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.EditorPlagiarismNote;
import team16.literaryassociation.repository.EditorPlagiarismNoteRepository;
import team16.literaryassociation.services.interfaces.EditorPlagiarismNoteService;

@Service
public class EditorPlagiarismNoteImpl implements EditorPlagiarismNoteService {

    @Autowired
    private EditorPlagiarismNoteRepository editorPlagiarismNoteRepository;

    @Override
    public EditorPlagiarismNote save(EditorPlagiarismNote note) {
        return editorPlagiarismNoteRepository.save(note);
    }
}
