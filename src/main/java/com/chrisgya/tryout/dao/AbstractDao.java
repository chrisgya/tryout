package com.chrisgya.tryout.dao;

import com.chrisgya.tryout.model.Page;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public abstract class AbstractDao<T> {
    protected static final String SINGLE_RESULT = "single";
    protected static final String MULTIPLE_RESULT = "list";
    protected static final String RESULT_COUNT = "count";
    protected static final String RETURN_VALUE = "RETURN_VALUE";
    protected JdbcTemplate jdbcTemplate;
    protected SimpleJdbcCall create, update, delete, find, findAll;

    public AbstractDao() {
    }

    public abstract void setDataSource(DataSource var1);

    public <T1, T2> T2 create(T1 model) throws DataAccessException {
        SqlParameterSource in = new SqlParameterSourceHelper(model);
        Map<String, Object> m = this.create.execute(in);
        List<T2> result = (List) m.get(SINGLE_RESULT);
        return !result.isEmpty() ? (T2) result.get(0) : null;
    }


    public <T> int update(T model) throws DataAccessException {
        SqlParameterSource in = new SqlParameterSourceHelper(model);
        Map<String, Object> m = this.update.execute(in);
        return  (int) m.get(RETURN_VALUE);
    }

    public <T> int delete(T id) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("id", id);
        Map<String, Object> m =this.delete.execute(in);
        return (Integer) m.get(RETURN_VALUE);
    }

    public <T1, T2> T2 find(T1 id) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("id", id);
        Map<String, Object> m = this.find.execute(in);
        List<T2> result = (List) m.get(SINGLE_RESULT);
        return !result.isEmpty() ? (T2) result.get(0) : null;
    }

    public Page<T> findAll(Integer pageNumber, Integer pageSize) throws DataAccessException {
        SqlParameterSource in = (new MapSqlParameterSource()).addValue("page_number", pageNumber).addValue("page_size", pageSize);
        Map<String, Object> m = this.findAll.execute(in);
        List<T> content = (List) m.get(MULTIPLE_RESULT);
        Long totalPages = (Long) ((List) m.get(RESULT_COUNT)).get(0);

        return new Page(pageNumber, pageSize, totalPages, content);
    }

    public class RowCountMapper implements RowMapper {
        public RowCountMapper() {
        }

        public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getLong(1);
        }
    }

}
