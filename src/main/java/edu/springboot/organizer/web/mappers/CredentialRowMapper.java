package edu.springboot.organizer.web.mappers;

import edu.springboot.organizer.data.models.Credential;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.web.dtos.CredentialDto;
import edu.springboot.organizer.web.dtos.Role;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class CredentialRowMapper implements BaseRowMapper<Credential, CredentialDto> {

    @Override
    public Credential mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Credential.builder()
                .login(rs.getString(Credential.Const.LOGIN.getColumn()))
                .role(rs.getString(Credential.Const.ROLE.getColumn()))
                .email(rs.getString(Credential.Const.EMAIL.getColumn()))
                .password(rs.getString(Credential.Const.PASS.getColumn()))
                .id(rs.getString(BaseEntity.BaseConst.ID.getColumn()))
                .build();
    }

    public CredentialDto toDto(Credential entity) {
        return CredentialDto.builder()
                .login(entity.getLogin())
                .role(Role.findByName(entity.getRole()))
                .email(entity.getEmail())
                .password(entity.getPassword())
                .created(entity.getId())
                .hashId(entity.hashCode())
                .build();
    }

    public Map<String, Object> toMap(Credential entity) {
        Map<String, Object> parameters = newParameters();
        parameters.put(BaseEntity.BaseConst.ID.getColumn(), entity.getId());
        parameters.put(Credential.Const.LOGIN.getColumn(), entity.getLogin());
        parameters.put(Credential.Const.ROLE.getColumn(), entity.getRole());
        parameters.put(Credential.Const.EMAIL.getColumn(), entity.getEmail());
        parameters.put(Credential.Const.PASS.getColumn(), entity.getPassword());
        return parameters;
    }

    public Map<String, Object> toMap(CredentialDto dto) {
        Map<String, Object> parameters = newParameters();
        parameters.put(BaseEntity.BaseConst.ID.getColumn(), dto.getCreated());
        parameters.put(Credential.Const.LOGIN.getColumn(), dto.getLogin());
        parameters.put(Credential.Const.ROLE.getColumn(), dto.getRole());
        parameters.put(Credential.Const.EMAIL.getColumn(), dto.getEmail());
        parameters.put(Credential.Const.PASS.getColumn(), dto.getPassword());
        return parameters;
    }

    @Override
    public Credential toEntity(CredentialDto dto) {
        return Credential.builder().id(dto.getCreated())
                .login(dto.getLogin())
                .role(dto.getRole().name())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }

}
