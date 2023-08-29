package com.api.rest.controller;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.api.rest.empleado.Empleado;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmpleadoControllerWebTestClientTests {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Order(1)
    public void testSaveEmployee() {
        // given
        Empleado empleado = Empleado.builder()
            .id(1L)
            .nombre("Adrian")
            .apellido("Ramirez")
            .email("aab@gmail.com")
            .build();
        
        // when
        webTestClient.post().uri("http://localhost:8080/api/empleados/save/employee")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(empleado)
            .exchange() // envia el request

        // then
            .expectStatus().isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").isEqualTo(empleado.getId())
            .jsonPath("$.nombre").isEqualTo(empleado.getNombre())
            .jsonPath("$.apellido").isEqualTo(empleado.getApellido())
            .jsonPath("$.email").isEqualTo(empleado.getEmail());

    }

    @Test
    @Order(2)
    public void testGetEmployeeById() {
        webTestClient.get().uri("http://localhost:8080/api/empleados/get/employee/by/1").exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").isEqualTo(1)
            .jsonPath("$.nombre").isEqualTo("Adrian")
            .jsonPath("$.apellido").isEqualTo("Ramirez")
            .jsonPath("$.email").isEqualTo("aab@gmail.com");
    }

    @Test
    @Order(3)
    public void testListEmployee() {
        webTestClient.get().uri("http://localhost:8080/api/empleados/get/all/employee")
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(Empleado.class)
            .consumeWith(response -> {
                List<Empleado> listEmployees = response.getResponseBody();
                Assertions.assertEquals(1, listEmployees.size());
                Assertions.assertNotNull(listEmployees);
            });
    }

    @Test
    @Order(4)
    public void testUpdatedEmployee() {
        Empleado empUpdated = Empleado.builder()
            .nombre("Pepe")
            .apellido("Castillo")
            .email("ckk2@gmail.com")
            .build();
        
        webTestClient.put().uri("http://localhost:8080/api/empleados/update/employee/1")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(empUpdated)
            .exchange() // envia la peticion

            .expectStatus().isOk()
            .expectHeader().contentType( MediaType.APPLICATION_JSON);
    }

    @Test
    @Order(5)
    public void testDeletedEmployee() {
        webTestClient.get().uri("http://localhost:8080/api/empleados/get/all/employee").exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(Empleado.class)
            .hasSize(1);

        webTestClient.delete().uri("http://localhost:8080/api/empleados/delete/employee/1")
            .exchange()
            .expectStatus().isOk();
        
        webTestClient.get().uri("http://localhost:8080/api/empleados/get/all/employee")
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(Empleado.class)
            .hasSize(0);
        
            webTestClient.get().uri("http://localhost:8080/api/empleados/get/employee/1")
                .exchange()
                .expectStatus().is4xxClientError();
    }
}
