package edu.springboot.organizer.generator.mappers;

import edu.springboot.organizer.data.models.User;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.generator.dtos.UserDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static UserDto toDto(User entity) {
        return UserDto.builder()
                .email(entity.getEmail())
                .name(entity.getName())
                .surName(entity.getSurName())
                .login(entity.getLogin())
                .password(entity.getPassword())
                .created(entity.getId())
                .hashId(entity.hashCode())
                .build();
    }

    public static Map<String, Object> toMap(User user) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(BaseEntity.BaseConst.ID.getColumn(), user.getId());
        parameters.put(User.Const.NAME.getColumn(), user.getName());
        parameters.put(User.Const.SUR_NAME.getColumn(), user.getSurName());
        parameters.put(User.Const.LOGIN.getColumn(), user.getLogin());
        parameters.put(User.Const.PASS.getColumn(), user.getPassword());
        parameters.put(User.Const.EMAIL.getColumn(), user.getEmail());
        return parameters;
    }
}
