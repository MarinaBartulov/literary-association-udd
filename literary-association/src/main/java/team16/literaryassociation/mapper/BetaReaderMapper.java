package team16.literaryassociation.mapper;

import org.springframework.stereotype.Component;
import team16.literaryassociation.dto.BetaReaderDTO;
import team16.literaryassociation.model.Reader;

@Component
public class BetaReaderMapper implements IMapper<Reader, BetaReaderDTO> {
    @Override
    public Reader toEntity(BetaReaderDTO dto) {
        return new Reader(dto.getId(), dto.getFirstName(), dto.getLastName(), dto.getCity(), dto.getCountry(),
                dto.getEmail(), dto.getUsername(), dto.isBetaReader(), dto.getPenaltyPoints());
    }

    @Override
    public BetaReaderDTO toDto(Reader entity) {
        return new BetaReaderDTO(entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getCity(),
                entity.getCountry(), entity.getEmail(), entity.getUsername(), entity.isBetaReader(),
                entity.getPenaltyPoints());
    }
}
