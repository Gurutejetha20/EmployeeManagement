package EmployeeManagement.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(employeecontroller.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get("/employees/1"))
                .andReturn();
    }
}