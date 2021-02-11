package team16.literaryassociation.mapper;

public interface IMapper<T, U> {

    T toEntity(U dto);

    U toDto(T entity);
}
