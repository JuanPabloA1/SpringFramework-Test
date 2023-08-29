package com.api.rest.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.willDoNothing;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.rest.empleado.Empleado;
import com.api.rest.empleado.EmpleadoRepository;
import com.api.rest.empleado.EmpleadoService;

@ExtendWith(MockitoExtension.class)
public class EmpleadoServiceTest {
    
    @Mock // @Mock crea un simulacro
    private EmpleadoRepository empleadoRepository;

    /* 
        @InjectMocks crea un objeto de la clase e inyecta
        los objectos sumulados que estan marcados con 
        la anotacion @Mock en el.
    */
    @InjectMocks
    private EmpleadoService empleadoService;

    private Empleado e;

    @BeforeEach
    public void setUp() {
        e = Empleado.builder()
            .id(1L)
            .nombre("Juan Pablo")
            .apellido("Mera Agudelo")
            .email("juan@juan.com")
            .build();
    }

    @Test
    @DisplayName("Test para guardar un empleado")
    public void testSaveEmployee() {
        // given
        given(empleadoRepository.findByEmail(e.getEmail()))
            .willReturn(Optional.empty());
        given(empleadoRepository.save(e))
            .willReturn(e);


        // when
        Empleado empSaved = empleadoService.saveEmpleado(e);

        // then
        assertThat(empSaved).isNotNull();
    }

    @Test
    @DisplayName("Test para guardar un empleado")
    public void testSaveEmployeeWithThrowException() {
        // given
        given(empleadoRepository.findByEmail(e.getEmail()))
            .willReturn(Optional.of(e));

        // when
        assertThrows(RuntimeException.class, () -> 
            empleadoService.saveEmpleado(e)
        );

        // then
        verify(empleadoRepository, never()).save(any(Empleado.class));
    }

    @Test
    @DisplayName("Test para listar a los empleados")
    public void testListEmployee() {
        // given
        Empleado emp1 = Empleado.builder()
            .id(2L)
            .nombre("Julen")
            .apellido("Olivia")
            .email("j2@gmail.com")
            .build();
        given(empleadoRepository.findAll()).willReturn(List.of(e, emp1));
        
        // when
        List<Empleado> employees = empleadoService.getAllEmpleados();

        // then
        assertThat(employees).isNotNull();
        assertThat(employees.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test para retornar una lista vacia")
    public void testListCollectionEmployeEmpty() {
        // given
        Empleado emp1 = Empleado.builder()
            .id(1L)
            .nombre("Julen")
            .apellido("Olivia")
            .email("j2@gmail.com")
            .build();
        given(empleadoRepository.findAll()).willReturn(Collections.emptyList());

        // when
        List<Empleado> listEmployees = empleadoService.getAllEmpleados();

        // then
        assertThat(listEmployees).isEmpty();
        assertThat(listEmployees.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Test para obtener el empleado por id")
    public void testGetEmployeeById() {
        // given
        given(empleadoRepository.findById(1L)).willReturn(Optional.of(e));

        // when
        Empleado empSaved = empleadoService.getEmpleadoById(e.getId()).get();

        // then
        assertThat(empSaved).isNotNull();
    }

    @Test
    @DisplayName("Test para actualizar un empleado")
    public void testUpdateEmployee() {
        // given
        given(empleadoRepository.save(e)).willReturn(e);
        e.setEmail("pablo@gmail.com");
        e.setNombre("Pablo Juan");

        // when
        Empleado empUpdated = empleadoService.updateEmpleado(e);

        // then
        assertThat(empUpdated.getEmail()).isEqualTo("pablo@gmail.com");
        assertThat(empUpdated.getNombre()).isEqualTo("Pablo Juan");
    }

    @Test
    @DisplayName("Test para eliminar un empleado")
    public void testDeleteEmployee() {
        // given
        long employeeId = 1L;
        willDoNothing().given(empleadoRepository).deleteById(employeeId);

        // when
        empleadoService.deleteEmpleado(employeeId);

        // then
        verify(empleadoRepository, times(1)).deleteById(employeeId);
    }
}
