package edu.springboot.organizer.generator.mappers;

import edu.springboot.organizer.data.models.User;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.generator.dtos.UserDto;
import edu.springboot.organizer.generator.mappers.base.BaseRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserRowMapper implements BaseRowMapper<User, UserDto> {

    @Override
    public UserDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserDto dto = UserDto.builder()
                .name(rs.getString(User.Const.NAME.getColumn()))
                .surName(rs.getString(User.Const.SUR_NAME.getColumn()))
                .email(rs.getString(User.Const.EMAIL.getColumn()))
                .login(rs.getString(User.Const.LOGIN.getColumn()))
                .password(rs.getString(User.Const.PASS.getColumn()))
                .created(rs.getString(BaseEntity.BaseConst.ID.getColumn()))
                .build();
        dto.hashCode();
        return dto;
    }

    public UserDto toDto(User entity) {
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

    public Map<String, Object> toMap(User entity) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(BaseEntity.BaseConst.ID.getColumn(), entity.getId());
        parameters.put(User.Const.NAME.getColumn(), entity.getName());
        parameters.put(User.Const.SUR_NAME.getColumn(), entity.getSurName());
        parameters.put(User.Const.LOGIN.getColumn(), entity.getLogin());
        parameters.put(User.Const.PASS.getColumn(), entity.getPassword());
        parameters.put(User.Const.EMAIL.getColumn(), entity.getEmail());
        return parameters;
    }

}
