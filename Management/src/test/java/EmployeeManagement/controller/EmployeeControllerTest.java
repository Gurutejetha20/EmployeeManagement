package EmployeeManagement.controller;

import EmployeeManagement.config.xmlvalidator;
import EmployeeManagement.entity.Employee;
import EmployeeManagement.security.JwtUtil;
import EmployeeManagement.service.employeeservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(employeecontroller.class)
@Import(EmployeeControllerTest.TestSecurityConfig.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private employeeservice service;

    @MockBean
    private xmlvalidator validator;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void testGetAll() throws Exception {
        Employee emp = new Employee();
        emp.setName("John Doe");
        emp.setEmail("john@example.com");
        emp.setDepartment("IT");

        when(service.getAll()).thenReturn(Arrays.asList(emp));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetById() throws Exception {
        Employee emp = new Employee();
        emp.setName("Jane Doe");
        emp.setEmail("jane@example.com");

        when(service.get(1L)).thenReturn(emp);

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetByIdNotFound() throws Exception {
        when(service.get(99L)).thenThrow(new RuntimeException("Employee not found: 99"));

        mockMvc.perform(get("/employees/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/employees/delete/1"))
                .andExpect(status().isOk());
    }

    @Configuration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain testFilterChain(HttpSecurity http) throws Exception {
            http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }
}