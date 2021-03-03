package com.chrisgya.tryout.dao;

import com.chrisgya.tryout.model.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Map;

@Repository
public class UserDao extends AbstractDao<UserResponse> {

    SimpleJdbcCall verifyUser;

    @Autowired
    @Override
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);

        create = new SimpleJdbcCall(jdbcTemplate).withProcedureName("proc_create_user").returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(UserResponse.class));
        find = new SimpleJdbcCall(jdbcTemplate).withProcedureName("proc_get_user").returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(UserResponse.class));
        findAll = new SimpleJdbcCall(jdbcTemplate).withProcedureName("proc_get_users")
                .returningResultSet(RESULT_COUNT, new RowCountMapper())
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(UserResponse.class));
        update = new SimpleJdbcCall(jdbcTemplate).withProcedureName("proc_update_user").withReturnValue();
        delete = new SimpleJdbcCall(jdbcTemplate).withProcedureName("proc_deactivate_user").withReturnValue();
        verifyUser = new SimpleJdbcCall(jdbcTemplate).withProcedureName("proc_verify_user").withReturnValue();
    }

    public int verifyUserEmail(String verificationCode) throws DataAccessException {
        MapSqlParameterSource in = (new MapSqlParameterSource())
                .addValue("verification_code", verificationCode);

        Map<String, Object> m = this.verifyUser.execute(in);
        return (int) m.get("RETURN_VALUE");
    }

}
