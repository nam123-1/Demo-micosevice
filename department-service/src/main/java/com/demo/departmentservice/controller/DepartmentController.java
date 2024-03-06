package com.demo.departmentservice.controller;


import com.demo.departmentservice.client.EmployeeClient;
import com.demo.departmentservice.model.Department;
import com.demo.departmentservice.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/department")
public class DepartmentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    private DepartmentRepository repository;

    @Autowired
    private EmployeeClient employeeClient;

    @PostMapping
    public Department addDepart(@RequestBody Department department){
        LOGGER.info("Department add: {}", department);
        return repository.addDepartment(department);
    }

    @GetMapping
    public List<Department> findAll(){
        LOGGER.info("Department find");
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Department fillById(@PathVariable Long id){
        LOGGER.info("Department find: id={}", id);
        return repository.findById(id);
    }

    @GetMapping("/with-employee")
    public List<Department> findAllWithEmpolyees(@RequestParam(name = "departmentId", required = false) Long departmentId) {
        LOGGER.info("Department find with employees: departmentId={}", departmentId);

        List<Department> departments = repository.findAll();

        if (departmentId != null) {
            // Nếu DepartmentId được cung cấp, hãy tìm nạp nhân viên cho bộ phận cụ thể đó
            departments.forEach(department -> department.setEmployees(employeeClient.findByDepartment(departmentId)));
        } else {
            // Nếu không có DepartmentId nào được cung cấp, hãy tìm nạp nhân viên cho tất cả các phòng ban (hoặc xử lý nếu cần)
            departments.forEach(department -> department.setEmployees(employeeClient.findByDepartment(department.getId())));
        }

        return departments;
    }


}
