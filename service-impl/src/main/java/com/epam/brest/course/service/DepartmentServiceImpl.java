package com.epam.brest.course.service;

import com.epam.brest.course.dao.DepartmentDao;
import com.epam.brest.course.dto.DepartmentDTO;
import com.epam.brest.course.model.Department;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger LOGGER = LogManager.getLogger();

    private final DepartmentDao departmentDao;

    public DepartmentServiceImpl(final DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }

    @Override
    public Department findById(final Integer id) {
        LOGGER.debug("findById({})", id);
        return departmentDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Failed to get department from DB"));
    }

    @Override
    public Integer create(Department department) {

        LOGGER.debug("create({})", department);
        return departmentDao.create(department);
    }

    @Override
    public void update(Department department) {
        LOGGER.debug("update({})", department);
        departmentDao.update(department);
    }

    @Override
    public void updateDepartmentDescription(Integer departmentId, String description) {
        LOGGER.debug("updateDepartmentDescription({}, {})", departmentId, description);
        departmentDao.findById(departmentId)
                .map(d -> {
                    d.setDescription(description);
                    return d;
                })
                .ifPresent(departmentDao::update);
   }

    @Override
    public Stream<Department> findAll() {
        LOGGER.debug("findAll()");
        return departmentDao.findAll();
    }

    @Override
    public Stream<DepartmentDTO> findAllDepartmentDTOs() {
        LOGGER.debug("findAllDepartmentDTOs()");
        return departmentDao.findAllDepartmentDTOs();
    }

    @Override
    public void delete(final Integer id) {
        LOGGER.debug("delete({})", id);
        departmentDao.delete(id);
   }
}
