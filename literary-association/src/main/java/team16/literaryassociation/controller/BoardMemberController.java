package team16.literaryassociation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team16.literaryassociation.dto.MembershipApplicationDTO;;
import team16.literaryassociation.dto.MembershipApplicationInfoDTO;
import team16.literaryassociation.services.interfaces.MembershipApplicationService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/boardMember")
public class BoardMemberController {

    @Autowired
    private MembershipApplicationService membershipApplicationService;

    @GetMapping(value = "/membershipApplications")
    public ResponseEntity<?> getAllMembershipApplications(){
        List<MembershipApplicationDTO> membershipApplicationsDTO = membershipApplicationService.getAllMembershipApplicationsDTO();
        return new ResponseEntity<>(membershipApplicationsDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/membershipApplication/{processId}")
    public ResponseEntity<?> getMembershipApplication(@PathVariable String processId){
        MembershipApplicationInfoDTO membershipApplicationInfoDTO = membershipApplicationService.getMembershipApplicationInfoDTO(processId);
        return new ResponseEntity<>(membershipApplicationInfoDTO, HttpStatus.OK);
    }
}
