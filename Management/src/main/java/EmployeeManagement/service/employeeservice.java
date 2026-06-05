package EmployeeManagement.service;

import EmployeeManagement.entity.*;
import EmployeeManagement.dto.Employeedto;
import EmployeeManagement.repository.*;
import EmployeeManagement.messaging.employeeeventpublisher;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
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

    public Employee create(Employeedto dto) {
        Employee e = new Employee();
        e.setName(dto.getName());
        e.setEmail(dto.getEmail());
        e.setDepartment(dto.getDepartment());
        e.setDateOfJoining(dto.getDateOfJoining());
        e.setStatus("ACTIVE");
        e.setCreatedAt(LocalDateTime.now());

        Employee saved = repo.save(e);

        try { publisher.publish(saved.getId(), "CREATE"); }
        catch (Exception ex) { System.err.println("JMS error: " + ex.getMessage()); }

        log(saved.getId(), "CREATE", "REST");
        return saved;
    }

    public Employee update(Long id, Employeedto dto) {
        Employee e = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + id));

        e.setName(dto.getName());
        e.setEmail(dto.getEmail());
        e.setDepartment(dto.getDepartment());
        e.setDateOfJoining(dto.getDateOfJoining());
        e.setUpdatedAt(LocalDateTime.now());

        Employee saved = repo.save(e);

        try { publisher.publish(id, "UPDATE"); }
        catch (Exception ex) { System.err.println("JMS error: " + ex.getMessage()); }

        log(id, "UPDATE", "REST");
        return saved;
    }

    public Employee get(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + id));
    }

    public List<Employee> getAll() {
        return repo.findAll();
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Employee not found: " + id);
        }
        repo.deleteById(id);

        try { publisher.publish(id, "DELETE"); }
        catch (Exception ex) { System.err.println("JMS error: " + ex.getMessage()); }

        log(id, "DELETE", "REST");
    }

    private void log(Long id, String action, String source) {
        try {
            AuditLog log = new AuditLog();
            log.setEmployeeId(id);
            log.setMessageId(UUID.randomUUID().toString());
            log.setAction(action);
            log.setSource(source);
            log.setTimestamp(LocalDateTime.now());
            auditRepo.save(log);
        } catch (Exception ex) {
            System.err.println("Audit log error: " + ex.getMessage());
        }
    }
}