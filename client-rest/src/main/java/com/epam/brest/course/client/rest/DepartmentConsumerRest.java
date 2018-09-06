package com.epam.brest.course.client.rest;

import com.epam.brest.course.dto.DepartmentDTO;
import com.epam.brest.course.model.Department;
import com.epam.brest.course.service.DepartmentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Stream;

public class DepartmentConsumerRest implements DepartmentService {


    private static final Logger LOGGER = LogManager.getLogger();
    private String url;

    private RestTemplate restTemplate;

    public DepartmentConsumerRest(final String url, final RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    public Department findById(final Integer id) {
        LOGGER.debug("findByID({})",id);
        final String path = this.url + "/" + id;
        ResponseEntity<Department> responseEntity = restTemplate.getForEntity(path, Department.class);
        final Department department = responseEntity.getBody();
        return department;
    }

    @Override
    public Integer create(Department department) {
        ResponseEntity<Integer> responseEntity = restTemplate.postForEntity(url, department, Integer.class);
        final Integer id = responseEntity.getBody();
        return id;
    }

    @Override
    public void update(final Department department) {
        LOGGER.debug("update({})",department);
        restTemplate.put(url, department);
    }

    @Override
    @Deprecated
    public void updateDepartmentDescription(Integer departmentId, String description) {
        //FIXME remove deprecated
    }

    @Override
    public Stream<Department> findAll() {
        //FIXME implement
        return null;
    }

    @Override
    public Stream<DepartmentDTO> findAllDepartmentDTOs() {
        LOGGER.debug("findAllDepartmentDTOs()");
        ResponseEntity<List<DepartmentDTO>> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<DepartmentDTO>>() {});
        return responseEntity.getBody().stream();
    }

    @Override
    public void delete(Integer id) {
        LOGGER.debug("delete({})",id);
        final String path = this.url + "/" + id;
        restTemplate.delete(path);
    }
}
