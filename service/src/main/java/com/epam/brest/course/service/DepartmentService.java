package com.epam.brest.course.service;

import com.epam.brest.course.dto.DepartmentDTO;
import com.epam.brest.course.model.Department;

import java.util.stream.Stream;

/**
 * Department Service Interface.
 */
public interface DepartmentService {

    /**
     * Get Department By Id.
     *
     * @param id id
     * @return Department
     */
    Department findById(final Integer id);

    /**
     * Persist new department.
     *
     * @param department new department
     * @return department with id.
     *
     */
    Integer create(final Department department);

    /**
     * Update department.
     *
     * @param department department
     */
    void update(Department department);

    /**
     * Update department description.
     *
     * @param departmentId id
     * @param description new description
     */
    @Deprecated
    void updateDepartmentDescription(Integer departmentId, String description);

    /**
     * Find all departments stream.
     *
     * @return departments .
     */
    Stream<Department> findAll();

    /**
     * Get department DTOs list.
     *
     * @return department DTOs list.
     */
    Stream<DepartmentDTO> findAllDepartmentDTOs();

    /**
     * Delete department with specified id.
     *
     * @param id department id
     */
    void delete(final Integer id);
}
