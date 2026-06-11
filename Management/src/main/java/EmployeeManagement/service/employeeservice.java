package EmployeeManagement.service;

import EmployeeManagement.entity.*;
import EmployeeManagement.dto.Employeedto;
import EmployeeManagement.exception.AppException;
import EmployeeManagement.repository.*;
import EmployeeManagement.messaging.employeeeventpublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.time.LocalDateTime;

@Service
public class employeeservice {

    @Autowired private employeerepository     repo;
    @Autowired private employeeeventpublisher publisher;

    private final ObjectMapper mapper;

    public employeeservice() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public Employee create(Employeedto dto, String rawXml) {
        try {
            Employee e = new Employee();
            e.setName(dto.getName());
            e.setEmail(dto.getEmail());
            e.setDepartment(dto.getDepartment());
            e.setDateOfJoining(dto.getDateOfJoining());
            e.setStatus("ACTIVE");
            e.setCreatedAt(LocalDateTime.now());

            Employee saved = repo.save(e);
            String responseJson = toJson(saved);
            publishEvent(saved.getId(), "CREATE", rawXml, responseJson);
            return saved;

        } catch (DataAccessException ex) {
            throw AppException.databaseError(ex.getMostSpecificCause().getMessage());
        }
    }

    public Employee update(Long id, Employeedto dto, String rawXml) {
        Employee e = repo.findById(id)
                .orElseThrow(() -> AppException.employeeNotFound(id));
        try {
            e.setName(dto.getName());
            e.setEmail(dto.getEmail());
            e.setDepartment(dto.getDepartment());
            e.setDateOfJoining(dto.getDateOfJoining());
            e.setUpdatedAt(LocalDateTime.now());

            Employee saved = repo.save(e);
            String responseJson = toJson(saved);
            publishEvent(id, "UPDATE", rawXml, responseJson);
            return saved;

        } catch (DataAccessException ex) {
            throw AppException.databaseError(ex.getMostSpecificCause().getMessage());
        }
    }

    public Employee get(Long id) {
        Employee found = repo.findById(id)
                .orElseThrow(() -> AppException.employeeNotFound(id));
        publishEvent(id, "GET", "ID: " + id, toJson(found));
        return found;
    }

    public List<Employee> getAll() {
        try {
            List<Employee> list = repo.findAll();
            publishEvent(0L, "GET_ALL", "fetch all", "Total records: " + list.size());
            return list;
        } catch (DataAccessException ex) {
            throw AppException.databaseError(ex.getMostSpecificCause().getMessage());
        }
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw AppException.employeeNotFound(id);
        }
        try {
            repo.deleteById(id);
            publishEvent(id, "DELETE", "ID: " + id, "Employee deleted successfully.");
        } catch (DataAccessException ex) {
            throw AppException.databaseError(ex.getMostSpecificCause().getMessage());
        }
    }

    private void publishEvent(Long id, String action, String request, String response) {
        try {
            publisher.publish(id, action, request, response);
        } catch (Exception ex) {
            System.err.println("[JMS] Publish failed for " + action + " ID " + id + ": " + ex.getMessage());
        }
    }

    private String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            return obj.toString();
        }
    }
}