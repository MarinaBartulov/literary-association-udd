package team16.literaryassociation.services.interfaces;

import team16.literaryassociation.model.PlagiarismComplaint;

public interface PlagiarismComplaintService {

    PlagiarismComplaint save(PlagiarismComplaint plagiarismComplaint);
    PlagiarismComplaint findById(Long id);
}
