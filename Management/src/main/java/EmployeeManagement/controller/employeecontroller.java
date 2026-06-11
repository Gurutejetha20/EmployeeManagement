package EmployeeManagement.controller;

import java.util.List;
import EmployeeManagement.service.employeeservice;
import EmployeeManagement.dto.Employeedto;
import EmployeeManagement.entity.Employee;
import EmployeeManagement.config.xmlvalidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
public class employeecontroller {

    @Autowired private employeeservice service;
    @Autowired private xmlvalidator    validator;

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> create(@RequestBody String xml) {
        validator.validate(xml);
        Employeedto dto = validator.convert(xml);
        return ResponseEntity.ok(service.create(dto, xml));
    }

    @PutMapping(value = "/update/{id}",
                consumes = MediaType.APPLICATION_XML_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> update(@PathVariable Long id, @RequestBody String xml) {
        validator.validate(xml);
        return ResponseEntity.ok(service.update(id, validator.convert(xml), xml));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Employee>> all() {
        return ResponseEntity.ok(service.getAll());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Employee deleted successfully.");
    }
}