package team16.literaryassociation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.Editor;
import team16.literaryassociation.model.Lecturer;
import team16.literaryassociation.repository.LecturerRepository;
import team16.literaryassociation.services.interfaces.LecturerService;

import java.util.List;
import java.util.Random;

@Service
public class LecturerServiceImpl implements LecturerService {

    @Autowired
    private LecturerRepository lecturerRepository;

    @Override
    public Lecturer findRandomEditor() {

        List<Lecturer> allLecturers = this.lecturerRepository.findAll();
        Random rand = new Random();
        Lecturer lecturer = allLecturers.get(rand.nextInt(allLecturers.size()));
        return lecturer;
    }
}
