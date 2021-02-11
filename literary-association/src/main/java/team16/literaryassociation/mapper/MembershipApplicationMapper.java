package team16.literaryassociation.mapper;

import org.springframework.stereotype.Component;
import team16.literaryassociation.dto.MembershipApplicationDTO;
import team16.literaryassociation.model.MembershipApplication;

@Component
public class MembershipApplicationMapper implements IMapper<MembershipApplication, MembershipApplicationDTO> {

    @Override
    public MembershipApplication toEntity(MembershipApplicationDTO dto){
        return new MembershipApplication(); //uraditi ako bude potrebno
    }

    @Override
    public MembershipApplicationDTO toDto(MembershipApplication entity)
    {
        return new MembershipApplicationDTO(entity);
    }

}
