package edu.springboot.organizer.generator.mappers;

import edu.springboot.organizer.data.models.User;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.generator.dtos.UserDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<UserDto> {

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


}
