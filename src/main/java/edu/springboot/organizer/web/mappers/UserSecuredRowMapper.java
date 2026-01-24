package edu.springboot.organizer.web.mappers;

import edu.springboot.organizer.data.models.User;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.web.dtos.UserSecuredDto;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    @Override
    public UserSecuredDto toDto(User entity) {
        return UserSecuredDto.builder()
                .name(entity.getName())
                .surName(entity.getSurName())
                .credentialId(entity.getCredentialId())
                .created(entity.getId())
                .hashId(entity.hashCode())
                .build();
    }

    @Override
    public Map<String, Object> toMap(User entity) {
        Map<String, Object> parameters = newParameters();
        parameters.put(BaseEntity.BaseConst.ID.getColumn(), entity.getId());
        parameters.put(User.Const.NAME.getColumn(), entity.getName());
        parameters.put(User.Const.SURNAME.getColumn(), entity.getSurName());
        parameters.put(User.Const.CREDENTIAL.getColumn(), entity.getCredentialId());
        return parameters;
    }

    @Override
    public Map<String, Object> toMap(UserSecuredDto dto) {
        Map<String, Object> parameters = newParameters();
        parameters.put(BaseEntity.BaseConst.ID.getColumn(), dto.getCreated());
        parameters.put(User.Const.NAME.getColumn(), dto.getName());
        parameters.put(User.Const.SURNAME.getColumn(), dto.getSurName());
        parameters.put(User.Const.CREDENTIAL.getColumn(), dto.getCredentialId());
        return parameters;
    }

    @Override
    public User toEntity(UserSecuredDto dto) {
        return User.builder()
                .id(dto.getCreated())
                .name(dto.getName())
                .surName(dto.getSurName())
                .credentialId(dto.getCredentialId())
                .build();
    }

}
