package EmployeeManagement.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import EmployeeManagement.repository.*;
import EmployeeManagement.entity.*;
import EmployeeManagement.dto.*;

class EmployeeServiceTest {

    @InjectMocks
    private employeeservice service;

    @Mock
    private employeerepository repo;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        Employeedto dto = new Employeedto();
        dto.setName("test");
        dto.setEmail("test@test.com");
        dto.setDepartment("IT");

        when(repo.save(any())).thenReturn(new Employee());

        Employee e = service.create(dto);
        assertNotNull(e);
    }
}