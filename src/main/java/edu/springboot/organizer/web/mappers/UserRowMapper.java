package edu.springboot.organizer.web.mappers;

import edu.springboot.organizer.data.models.User;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.web.dtos.UserDto;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserRowMapper implements BaseRowMapper<User, UserDto> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .name(rs.getString(User.Const.NAME.getColumn()))
                .surName(rs.getString(User.Const.SURNAME.getColumn()))
                .credentialId(rs.getString(User.Const.CREDENTIAL.getColumn()))
                .id(rs.getString(BaseEntity.BaseConst.ID.getColumn()))
                .build();
    }

    public UserDto toDto(User entity) {
        return UserDto.builder()
                .name(entity.getName())
                .surName(entity.getSurName())
                .created(entity.getId())
                .hashId(entity.hashCode())
                .build();
    }

    public Map<String, Object> toMap(User entity) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(BaseEntity.BaseConst.ID.getColumn(), entity.getId());
        parameters.put(User.Const.NAME.getColumn(), entity.getName());
        parameters.put(User.Const.SURNAME.getColumn(), entity.getSurName());
        parameters.put(User.Const.CREDENTIAL.getColumn(), entity.getCredentialId());
        return parameters;
    }

}
