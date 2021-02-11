package team16.literaryassociation.mapper;

import org.springframework.stereotype.Component;
import team16.literaryassociation.dto.EditorDTO;
import team16.literaryassociation.model.Editor;

@Component
public class EditorMapper implements IMapper<Editor, EditorDTO> {
    @Override
    public Editor toEntity(EditorDTO dto) {
        return new Editor(dto);
    }

    @Override
    public EditorDTO toDto(Editor entity) {
        return new EditorDTO(entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getCity(),
                entity.getCountry(), entity.getEmail(), entity.getUsername());
    }
}
