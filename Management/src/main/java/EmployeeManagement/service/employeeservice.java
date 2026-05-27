package EmployeeManagement.service;
import EmployeeManagement.entity.*;

import EmployeeManagement.dto.Employeedto;
import EmployeeManagement.repository.*;
import EmployeeManagement.messaging.employeeeventpublisher;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.BeanUtils;
import jakarta.validation.*;

import java.util.List;
import java.util.Set;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class employeeservice {
	    @Autowired
	    private employeerepository repo;

	    @Autowired
	    private auditlogrepository auditRepo;

	    @Autowired
	    private employeeeventpublisher publisher;
	    
	    @Autowired
	    private jakarta.validation.Validator validator;

	    public Employee create(Employeedto dto) {
	    	Set<ConstraintViolation<Employeedto>> violations = validator.validate(dto);
	        if (!violations.isEmpty()) {
	            throw new RuntimeException(violations.iterator().next().getMessage());
	        }
	        Employee e = new Employee();
	        BeanUtils.copyProperties(dto, e);
	        repo.save(e);
	        publisher.publish(e.getId(), "CREATE");
	        log(e.getId(), "CREATE", "REST");
	        return e;
	    }

	    public Employee update(Long id, Employeedto dto) {
	        Employee e = repo.findById(id).orElseThrow();
	        BeanUtils.copyProperties(dto, e);
	        e.setUpdatedAt(LocalDateTime.now());
	        repo.save(e);

	        publisher.publish(id, "UPDATE");
	        log(id, "UPDATE", "REST");

	        return e;
	    }

	    public Employee get(Long id) {
	        return repo.findById(id).orElseThrow();
	    }

	    public List<Employee> getAll() {
	        return repo.findAll();
	    }

	    public void delete(Long id) {
	        repo.deleteById(id);

	        publisher.publish(id, "DELETE");
	        log(id, "DELETE", "REST");
	    }

	    private void log(Long id, String action, String source) {
	        AuditLog log = new AuditLog();
	        log.setEmployeeId(id);
	        log.setMessageId(UUID.randomUUID().toString());
	        log.setAction(action);
	        log.setSource(source);
	        log.setTimestamp(LocalDateTime.now());
	        auditRepo.save(log);
	    }
	}

