package EmployeeManagement.controller;
import java.util.List;

import EmployeeManagement.service.*;
import EmployeeManagement.dto.*;
import EmployeeManagement.entity.Employee;
import EmployeeManagement.config.xmlvalidator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
@RestController
@RequestMapping("/employees")
public class employeecontroller {

	    @Autowired
	    private employeeservice service;

	    @Autowired
	    private xmlvalidator validator;

	    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE)
	    public Employee create(@RequestBody String xml) {
	        validator.validate(xml);
	        Employeedto dto = validator.convert(xml);
	        return service.create(dto);
	    }

	    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_XML_VALUE)
	    public Employee update(@PathVariable Long id, @RequestBody String xml) {
	        validator.validate(xml);
	        return service.update(id, validator.convert(xml));
	    }

	    @GetMapping("/{id}")
	    public Employee get(@PathVariable Long id) {
	        return service.get(id);
	    }

	    @GetMapping
	    public List<Employee> all() {
	        return service.getAll();
	    }

	    @DeleteMapping("/delete/{id}")
	    public void delete(@PathVariable Long id) {
	        service.delete(id);
	    }
	}

