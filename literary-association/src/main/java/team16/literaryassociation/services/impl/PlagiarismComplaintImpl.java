package team16.literaryassociation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.PlagiarismComplaint;
import team16.literaryassociation.repository.PlagiarismComplaintRepository;
import team16.literaryassociation.services.interfaces.PlagiarismComplaintService;

@Service
public class PlagiarismComplaintImpl implements PlagiarismComplaintService {

    @Autowired
    private PlagiarismComplaintRepository plagiarismComplaintRepository;

    @Override
    public PlagiarismComplaint save(PlagiarismComplaint plagiarismComplaint) {
        return plagiarismComplaintRepository.save(plagiarismComplaint);
    }

    @Override
    public PlagiarismComplaint findById(Long id) {
        return plagiarismComplaintRepository.findById(id).orElseGet(null);
    }
}
