package team16.literaryassociation.services.interfaces;

import team16.literaryassociation.model.Role;

public interface RoleService {

    Role findByName(String name);
}
