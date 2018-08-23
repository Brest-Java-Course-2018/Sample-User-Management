package com.epam.brest.course.dao;

import com.epam.brest.course.dto.DepartmentDTO;
import com.epam.brest.course.model.Department;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class DepartmentDaoImpl implements DepartmentDao {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final String DEPARTMENT_ID = "departmentId";
    public static final String DEPARTMENT_NAME = "departmentName";
    public static final String DESCRIPTION = "description";
    public static final String AVG_SALARY = "avgSalary";

    @Value("${department.select}")
    private String selectSql;

    @Value("${department.selectAvgSalary}")
    private String selectAvgSalarySql;

    @Value("${department.selectById}")
    private String selectByIdSql;

    @Value("${department.checkDepartment}")
    private String checkDepartmentSql;

    @Value("${department.insert}")
    private String insertSql;

    @Value("${department.update}")
    private String updateSql;

    @Value("${department.delete}")
    private String deleteSql;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DepartmentDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Stream<Department> getDepartments() {
        LOGGER.debug("getDepartments()");
        List<Department> departments =
                namedParameterJdbcTemplate.getJdbcOperations().query(selectSql, new DepartmentRowMapper());
        return departments.stream();
    }

    @Override
    public Stream<DepartmentDTO> getDepartmentDTOs() {
        LOGGER.debug("getDepartmentDTOs()");
        List<DepartmentDTO> list =
                namedParameterJdbcTemplate.getJdbcOperations().query(selectAvgSalarySql, new DepartmentDTORowMapper());
        return list.stream();
    }

    @Override
    public Optional<Department> getDepartmentById(Integer departmentId) {
        LOGGER.debug("getDepartmentById({})", departmentId);
        SqlParameterSource namedParameters =
                new MapSqlParameterSource(DEPARTMENT_ID, departmentId);
        Department department = namedParameterJdbcTemplate.queryForObject(selectByIdSql, namedParameters,
                BeanPropertyRowMapper.newInstance(Department.class));
        return Optional.ofNullable(department);
    }

    @Override
    public int addDepartment(Department department) {
        LOGGER.debug("addDepartment({})", department);
        MapSqlParameterSource namedParameters =
                new MapSqlParameterSource("departmentName", department.getDepartmentName());
        Optional.of(namedParameterJdbcTemplate.queryForObject(checkDepartmentSql, namedParameters, Integer.class))
                .filter(r -> r == 0)
                .orElseThrow(() -> new IllegalArgumentException("Department with the same name already exists in DB."));

        namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("departmentName", department.getDepartmentName());
        namedParameters.addValue("description", department.getDescription());

        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        Optional.of(namedParameterJdbcTemplate.update(insertSql, namedParameters, generatedKeyHolder))
                .filter(r -> r > 0)
                .orElseThrow(() -> new RuntimeException("Failed to add a department to DB"));
        return generatedKeyHolder.getKey().intValue();
    }

    @Override
    public int updateDepartment(Department department) {
        SqlParameterSource namedParameter = new BeanPropertySqlParameterSource(department);
        return namedParameterJdbcTemplate.update(updateSql, namedParameter);
    }

    @Override
    public int deleteDepartmentById(Integer departmentId) {
        return namedParameterJdbcTemplate.getJdbcOperations().update(deleteSql, departmentId);
    }

    private class DepartmentRowMapper implements RowMapper<Department> {

        @Override
        public Department mapRow(ResultSet resultSet, int i) throws SQLException {
            Department department = new Department();
            department.setDepartmentId(resultSet.getInt(DEPARTMENT_ID));
            department.setDepartmentName(resultSet.getString(DEPARTMENT_NAME));
            department.setDescription(resultSet.getString(DESCRIPTION));
            return department;
        }
    }

    private class DepartmentDTORowMapper implements RowMapper<DepartmentDTO> {

        @Override
        public DepartmentDTO mapRow(ResultSet resultSet, int i) throws SQLException {
            DepartmentDTO dto = new DepartmentDTO();
            dto.setDepartmentId(resultSet.getInt(DEPARTMENT_ID));
            dto.setDepartmentName(resultSet.getString(DEPARTMENT_NAME));
            dto.setAvgSalary(resultSet.getInt(AVG_SALARY));
            return dto;
        }
    }

}
