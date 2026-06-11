package EmployeeManagement.service;

import EmployeeManagement.dto.Employeedto;
import EmployeeManagement.entity.Employee;
import EmployeeManagement.messaging.employeeeventpublisher;
import EmployeeManagement.repository.auditlogrepository;
import EmployeeManagement.repository.employeerepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @InjectMocks
    private employeeservice service;

    @Mock
    private employeerepository repo;

    @Mock
    private auditlogrepository auditRepo;

    @Mock
    private employeeeventpublisher publisher;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private Employee savedEmployee(Long id, Employeedto dto) {
        Employee e = new Employee();
        e.setId(id);
        e.setName(dto.getName());
        e.setEmail(dto.getEmail());
        e.setDepartment(dto.getDepartment());
        e.setDateOfJoining(dto.getDateOfJoining());
        e.setStatus("ACTIVE");
        return e;
    }

    private Employeedto sampleDto() {
        Employeedto dto = new Employeedto();
        dto.setName("Test User");
        dto.setEmail("test@example.com");
        dto.setDepartment("IT");
        dto.setDateOfJoining(LocalDate.of(2024, 1, 15));
        return dto;
    }

    private static final String SAMPLE_XML =
        "<Employee><name>Test User</name><email>test@example.com</email>" +
        "<department>IT</department><dateOfJoining>2024-01-15</dateOfJoining></Employee>";

    @Test
    void testCreate_returnsSavedEmployee() {
        Employeedto dto = sampleDto();
        Employee saved = savedEmployee(1L, dto);

        when(repo.save(any(Employee.class))).thenReturn(saved);
        doNothing().when(publisher).publish(anyLong(), anyString(), anyString(), anyString());

        Employee result = service.create(dto, SAMPLE_XML);

        assertNotNull(result);
        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("IT", result.getDepartment());
        assertEquals("ACTIVE", result.getStatus());
        verify(repo, times(1)).save(any(Employee.class));
    }

    @Test
    void testCreate_jmsFailureDoesNotThrow() {
        Employeedto dto = sampleDto();
        Employee saved = savedEmployee(1L, dto);

        when(repo.save(any(Employee.class))).thenReturn(saved);
        doThrow(new RuntimeException("JMS down"))
            .when(publisher).publish(anyLong(), anyString(), anyString(), anyString());

        assertDoesNotThrow(() -> service.create(dto, SAMPLE_XML));
    }

    @Test
    void testGetAll_returnsList() {
        Employeedto dto = sampleDto();
        Employee e1 = savedEmployee(1L, dto);
        Employee e2 = savedEmployee(2L, dto);

        when(repo.findAll()).thenReturn(Arrays.asList(e1, e2));
        doNothing().when(publisher).publish(anyLong(), anyString(), anyString(), anyString());

        List<Employee> result = service.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repo, times(1)).findAll();
    }

    @Test
    void testGet_found() {
        Employeedto dto = sampleDto();
        Employee emp = savedEmployee(1L, dto);

        when(repo.findById(1L)).thenReturn(Optional.of(emp));
        doNothing().when(publisher).publish(anyLong(), anyString(), anyString(), anyString());

        Employee result = service.get(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test User", result.getName());
    }

    @Test
    void testGet_notFound_throwsException() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.get(99L));
        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    void testUpdate_success() {
        Employeedto dto = sampleDto();
        Employee existing = savedEmployee(1L, dto);

        Employeedto updatedDto = new Employeedto();
        updatedDto.setName("Updated Name");
        updatedDto.setEmail("updated@example.com");
        updatedDto.setDepartment("HR");
        updatedDto.setDateOfJoining(LocalDate.of(2024, 6, 1));

        Employee updatedEmp = savedEmployee(1L, updatedDto);

        String updatedXml =
            "<Employee><name>Updated Name</name><email>updated@example.com</email>" +
            "<department>HR</department><dateOfJoining>2024-06-01</dateOfJoining></Employee>";

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any(Employee.class))).thenReturn(updatedEmp);
        doNothing().when(publisher).publish(anyLong(), anyString(), anyString(), anyString());

        Employee result = service.update(1L, updatedDto, updatedXml);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("HR", result.getDepartment());
    }

    @Test
    void testUpdate_notFound_throwsException() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.update(99L, sampleDto(), SAMPLE_XML));
        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    void testDelete_success() {
        when(repo.existsById(1L)).thenReturn(true);
        doNothing().when(repo).deleteById(1L);
        doNothing().when(publisher).publish(anyLong(), anyString(), anyString(), anyString());

        assertDoesNotThrow(() -> service.delete(1L));
        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_notFound_throwsException() {
        when(repo.existsById(99L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.delete(99L));
        assertTrue(ex.getMessage().contains("99"));
    }
}