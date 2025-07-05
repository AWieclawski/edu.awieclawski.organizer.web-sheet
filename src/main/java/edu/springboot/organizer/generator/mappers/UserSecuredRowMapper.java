package edu.springboot.organizer.generator.mappers;

import edu.springboot.organizer.data.models.User;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.generator.dtos.UserSecuredDto;
import edu.springboot.organizer.generator.mappers.base.BaseRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * used to safe handling credentials
 */
public class UserSecuredRowMapper implements BaseRowMapper<User, UserSecuredDto> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .name(rs.getString(User.Const.NAME.getColumn()))
                .surName(rs.getString(User.Const.SURNAME.getColumn()))
                .credentialId(rs.getString(User.Const.CREDENTIAL.getColumn()))
                .id(rs.getString(BaseEntity.BaseConst.ID.getColumn()))
                .build();
    }

    public UserSecuredDto toDto(User entity) {
        return UserSecuredDto.builder()
                .name(entity.getName())
                .surName(entity.getSurName())
                .credentialId(entity.getCredentialId())
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
