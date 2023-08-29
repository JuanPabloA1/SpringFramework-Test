package com.api.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import com.api.rest.empleado.Empleado;
import com.api.rest.empleado.EmpleadoService;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest // Sirve para poder probar los controladores
public class EmpleadoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Sirve para agregar objetos simulados al contexto de la aplicacion
    private EmpleadoService empleadoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSaveEmployee() {
        // given
        Empleado e = Empleado.builder()
                .id(1L)
                .nombre("Juan Pablo")
                .apellido("Mera Agudelo")
                .email("juan@juan.com")
                .build();

        given(empleadoService.saveEmpleado(any(Empleado.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        try {
            // when
            ResultActions response = mockMvc.perform(post("/api/empleados/save/employee")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(e)));

            // then
            response.andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.nombre", is(e.getNombre())))
                    .andExpect(jsonPath("$.apellido", is(e.getApellido())))
                    .andExpect(jsonPath("$.email", is(e.getEmail())));
        } catch (Exception e1) {

        }
    }

    @Test
    @DisplayName("Test para listar empleados")
    public void testListEmployee() throws Exception {
        // given
        List<Empleado> listaEmpleados = new ArrayList<>();
        listaEmpleados.add(Empleado.builder().nombre("Christian").apellido("Ramirez").email("c1@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Gabriel").apellido("Ramirez").email("g1@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Julen").apellido("Ramirez").email("cj@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Biaggio").apellido("Ramirez").email("b1@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Adrian").apellido("Ramirez").email("a@gmail.com").build());
        given(empleadoService.getAllEmpleados()).willReturn(listaEmpleados);
        
        // when
        ResultActions response = mockMvc.perform(get("/api/empleados/get/all/employee"));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listaEmpleados.size())));
    }

    @Test
    @DisplayName("Test para obtener empleado por id")
    public void testGetEmployeeById() throws Exception {
        // given
        long id = 1L;
        Empleado emp = Empleado.builder()
                .id(1L)
                .nombre("Pablo")
                .apellido("Agudelo")
                .email("pablo@juan.com")
                .build();
        given(empleadoService.getEmpleadoById(id)).willReturn(Optional.of(emp));

        // when
        ResultActions response = mockMvc.perform(get("/api/empleados/get/employee/by/{id}", id));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre", is(emp.getNombre())))
                .andExpect(jsonPath("$.apellido", is(emp.getApellido())))
                .andExpect(jsonPath("$.email", is(emp.getEmail())));
    }

    @Test
    @DisplayName("Test para obtener empleado no encontrado")
    public void testGetEmployeeNotFounded() throws Exception {
        // given
        long id = 1L;
        Empleado emp = Empleado.builder()
                .id(1L)
                .nombre("Pablo")
                .apellido("Agudelo")
                .email("pablo@juan.com")
                .build();
        given(empleadoService.getEmpleadoById(id)).willReturn(Optional.empty());

        // when
        ResultActions response = mockMvc.perform(get("/api/empleados/get/employee/by/{id}", id));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Test para actualizar un empleado")
    public void testUpdatedEmployee() throws Exception {
        // given
        long id = 1L;
        Empleado empSaved = Empleado.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Mera")
                .email("juan@juan.com")
                .build();
        
        Empleado empUpdated = Empleado.builder()
                .id(1L)
                .nombre("Pablo")
                .apellido("Agudelo")
                .email("pablo@juan.com")
                .build();

        given(empleadoService.getEmpleadoById(id)).willReturn(Optional.of(empSaved));
        given(empleadoService.updateEmpleado(any(Empleado.class)))
               .willAnswer((invocation) -> invocation.getArgument(0));

        // when
        ResultActions response = mockMvc.perform(put("/api/empleados/update/employee/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empUpdated)));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre", is(empUpdated.getNombre())))
                .andExpect(jsonPath("$.apellido", is(empUpdated.getApellido())))
                .andExpect(jsonPath("$.email", is(empUpdated.getEmail())));
    }

    @Test
    @DisplayName("Test para actualizar un empleado no encontrado")
    public void testUpdateEmployeeNotFound() throws Exception {
        // given
        long id = 1L;
        Empleado empSaved = Empleado.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Mera")
                .email("juan@juan.com")
                .build();
        
        Empleado empUpdated = Empleado.builder()
                .id(1L)
                .nombre("Pablo")
                .apellido("Agudelo")
                .email("pablo@juan.com")
                .build();

        given(empleadoService.getEmpleadoById(id)).willReturn(Optional.empty());
        given(empleadoService.updateEmpleado(any(Empleado.class)))
               .willAnswer((invocation) -> invocation.getArgument(0));

        // when
        ResultActions response = mockMvc.perform(put("/api/empleados/update/employee/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empUpdated)));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
                
    }

    @Test
    @DisplayName("Test para eliminar un empleado")
    public void testDeletedEmployee() throws Exception {
        // given
        long id = 1L;
        willDoNothing().given(empleadoService).deleteEmpleado(id);

        // when
        ResultActions response = mockMvc.perform(delete("/api/empleados/delete/employee/{id}", id));

        // then
        response.andExpect(status().isOk())
                .andDo(print());
    }

}
