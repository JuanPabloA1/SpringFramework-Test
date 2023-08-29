package com.api.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.api.rest.empleado.Empleado;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmpleadoControllerRestTemplateTests {
    
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @Order(1)
    @DisplayName("Test para guardar un empleado")
    public void testSaveEmployee() {
        Empleado emp = Empleado.builder()
            .id(1L)
            .nombre("Christian")
            .apellido("Ramirez")
            .email("c1@gmail.com")
            .build();

        ResponseEntity<Empleado> response = testRestTemplate.postForEntity(
            "http://localhost:8080/api/empleados/save/employee",emp,Empleado.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        Empleado empCreated = response.getBody();
        assertNotNull(empCreated);

        assertEquals(1L, empCreated.getId());
        assertNotNull("Christian", empCreated.getNombre());
        assertEquals("Ramirez", empCreated.getApellido());
        assertEquals("c1@gmail.com", empCreated.getEmail());
    }

    @Test
    @Order(2)
    @DisplayName("Test para listar empleados")
    public void testListEmployee() {
        ResponseEntity<Empleado[]> response = testRestTemplate.getForEntity(
            "http://localhost:8080/api/empleados/get/all/employee", Empleado[].class);
        List<Empleado> employees = Arrays.asList(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        assertEquals(1L, employees.get(0).getId());
        assertEquals("Christian", employees.get(0).getNombre());
        assertEquals("Ramirez", employees.get(0).getApellido());
        assertEquals("c1@gmail.com", employees.get(0).getEmail());
    }

    @Test
    @Order(3)
    @DisplayName("Test para obtener empleado por id")
    public void testGetEmployeeById() {
        ResponseEntity<Empleado> response = testRestTemplate.getForEntity(
            "http://localhost:8080/api/empleados/get/employee/by/1", Empleado.class);
        Empleado employee = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        assertNotNull(employee);
        assertEquals(1L, employee.getId());
        assertEquals("Christian", employee.getNombre());
        assertEquals("Ramirez", employee.getApellido());
        assertEquals("c1@gmail.com", employee.getEmail());
    }

    // @Test
    // @DisplayName("Test para actualizar un empleado")
    // public void testUpdatedEmployee() {
    //     Empleado emp = Empleado.builder()
    //         .id(1L)
    //         .nombre("Christian")
    //         .apellido("Ramirez")
    //         .email("c1@gmail.com")
    //         .build();

    //     ResponseEntity<Empleado> response = testRestTemplate.postForEntity(
    //         "http://localhost:8080/api/empleados/update/employee/1",emp, Empleado.class);
    //     Empleado employee = response.getBody();

    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

    //     assertEquals("1L", );
        
    // }

    @Test
    @Order(4)
    @DisplayName("Test para eliminar un empleado")
    public void testDeletedEmployee() {
        ResponseEntity<Empleado[]> response = testRestTemplate.getForEntity(
            "http://localhost:8080/api/empleados/get/all/employee", Empleado[].class);
        List<Empleado> employees = Arrays.asList(response.getBody());
        assertEquals(1, employees.size());

        Map<String, Long> pathVariables = new HashMap<>();
        pathVariables.put("id", 1L);
        ResponseEntity<Void> exchange = testRestTemplate.exchange(
            "http://localhost:8080/api/empleados/delete/employee/{id}", HttpMethod.DELETE, null, Void.class, pathVariables);
        

        assertEquals(HttpStatus.OK, exchange.getStatusCode());
        assertFalse(exchange.hasBody());

        response = testRestTemplate.getForEntity(
            "http://localhost:8080/api/empleados/get/all/employee",Empleado[].class);
        employees = Arrays.asList(response.getBody());
        assertEquals(0,employees.size());

        ResponseEntity<Empleado> respuestaDetalle = testRestTemplate.getForEntity(
            "http://localhost:8080/api/empleados/get/employee/by/2",Empleado.class);
        assertEquals(HttpStatus.NOT_FOUND,respuestaDetalle.getStatusCode());
        assertFalse(respuestaDetalle.hasBody());
    }
}
