package team16.literaryassociation.services.interfaces;

import team16.literaryassociation.dto.MembershipApplicationDTO;
import team16.literaryassociation.dto.MembershipApplicationInfoDTO;
import team16.literaryassociation.model.MembershipApplication;

import java.util.List;

public interface MembershipApplicationService {

    MembershipApplication save(MembershipApplication membershipApplication);
    MembershipApplication getOne(Long id);
    List<MembershipApplicationDTO> getAllMembershipApplicationsDTO();
    MembershipApplicationInfoDTO getMembershipApplicationInfoDTO(String id);
    MembershipApplication findById(Long id);
 }
