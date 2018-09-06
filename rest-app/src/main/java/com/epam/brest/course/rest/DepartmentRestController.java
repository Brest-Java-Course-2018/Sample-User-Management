package com.epam.brest.course.rest;

import com.epam.brest.course.dto.DepartmentDTO;
import com.epam.brest.course.model.Department;
import com.epam.brest.course.service.DepartmentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class DepartmentRestController {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private DepartmentService departmentService;

    //curl -v localhost:8088/departments
    @GetMapping(value = "/departments")
    Collection<DepartmentDTO> departments() {
        LOGGER.debug("departments()");
        return departmentService.findAllDepartmentDTOs().collect(Collectors.toList());
    }

    //curl -v localhost:8088/departments/1
    @GetMapping(value = "/departments/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    Department departmentById(@PathVariable(value = "id") Integer id) {
        LOGGER.debug("departmentById({})", id);
        return departmentService.findById(id);
    }

    //curl -H "Content-Type: application/json" -X POST -d '{"departmentName":"xyz","description":"xyz"}' -v localhost:8088/departments
    @PostMapping(value = "/departments")
    @ResponseStatus(HttpStatus.CREATED)
    Integer createDepartment(@RequestBody Department department) {
        LOGGER.debug("create({})", department);
        return departmentService.create(department);
    }

    //curl -X "DELETE" localhost:8088/departments/1
    @DeleteMapping(value = "/departments/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    void deleteDepartment(@PathVariable(value = "id") Integer id) {
        LOGGER.debug("deleteDepartment({})", id);
        departmentService.delete(id);
    }

    @PutMapping(value = "/departments")
    void updateDepartment(@RequestBody Department department){
        LOGGER.debug("update({})", department);
        departmentService.update(department);
    }

    @GetMapping(value = "/departments/names")
    String departmentNames() {
        LOGGER.debug("department names");
        return departmentService.findAllDepartmentDTOs()
                .map(DepartmentDTO::getDepartmentName)
                .collect(Collectors.joining("|"));
    }

    @GetMapping(value = "/departments/paid")
    Collection<DepartmentDTO> departmentsPaid() {
        LOGGER.debug("departments paid");
        return departmentService.findAllDepartmentDTOs()
                .filter(d -> d.getAvgSalary() > 0)
                .collect(Collectors.toList());
    }

}
