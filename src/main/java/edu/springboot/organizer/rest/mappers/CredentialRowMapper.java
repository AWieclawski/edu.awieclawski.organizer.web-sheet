package edu.springboot.organizer.rest.mappers;

import edu.springboot.organizer.data.models.Credential;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.rest.dtos.CredentialDto;
import edu.springboot.organizer.rest.mappers.base.BaseRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CredentialRowMapper implements BaseRowMapper<Credential, CredentialDto> {

    @Override
    public Credential mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Credential.builder()
                .login(rs.getString(Credential.Const.LOGIN.getColumn()))
                .email(rs.getString(Credential.Const.EMAIL.getColumn()))
                .password(rs.getString(Credential.Const.PASS.getColumn()))
                .id(rs.getString(BaseEntity.BaseConst.ID.getColumn()))
                .build();
    }

    public CredentialDto toDto(Credential entity) {
        return CredentialDto.builder()
                .login(entity.getLogin())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .created(entity.getId())
                .hashId(entity.hashCode())
                .build();
    }

    public Map<String, Object> toMap(Credential entity) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(BaseEntity.BaseConst.ID.getColumn(), entity.getId());
        parameters.put(Credential.Const.LOGIN.getColumn(), entity.getLogin());
        parameters.put(Credential.Const.EMAIL.getColumn(), entity.getEmail());
        parameters.put(Credential.Const.PASS.getColumn(), entity.getPassword());
        return parameters;
    }

}
