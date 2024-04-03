package com.example.authservice.mapper;

import com.example.authservice.model.dto.AuthUserDto;
import com.example.authservice.model.entity.Role;
import com.example.authservice.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", source = "entity.roles", qualifiedByName = "getRoleNames")
    AuthUserDto toAuthUserDto(User entity);

    @Named("getRoleNames")
    default List<String> getRoleNames(List<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream().map(Role::getRoleName).toList();
    }
}
