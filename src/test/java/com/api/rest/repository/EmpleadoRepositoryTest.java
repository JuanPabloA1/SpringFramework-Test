package com.api.rest.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.api.rest.empleado.Empleado;
import com.api.rest.empleado.EmpleadoRepository;

@DataJpaTest
public class EmpleadoRepositoryTest {
    
    @Autowired
    private EmpleadoRepository empleadoRepository;

    private Empleado e;

    @BeforeEach // Indica que se ejecuta antes de cada metodo
    public void setUp() {
        e = Empleado.builder()
            .nombre("Julian")
            .apellido("Chavarro")
            .email("julian@gmail.com")
            .build();
    }

    @Test
    @DisplayName("Test para guardar un empleado")
    public void testSaveEmployee() {
        
        // given                                           //  TDD Test-Driven Development:
        Empleado e = Empleado.builder()                    //    se centra en como se
                .nombre("Pepe")                    //     implementa la funcionalidad
                .apellido("Lopez")
                .email("p12@gmail.com")             //    BDD Behavior Driven Development:
                .build();                                  //      se centra en el comportamiento de
                                                           //      una aplicacion para el usuario final
        // when                                         
        Empleado eSaved = empleadoRepository.save(e);      //      given - dado o condicion previa o configuracion
                                                           //      when - accion o el comportamiento que vamos a probar
        // then                                            //      then - verificar la salida
        assertThat(eSaved).isNotNull();
        assertThat(eSaved.getId()).isGreaterThan(0);       // Ejemplo:      
                                                        
        }                                                  // Given:
                                                           //      Dado que el usuario no ha introducido
        @Test                                              //      ningun dato en el formulario.
        @DisplayName("Test para listar a los empleados")                                                        
        public void testShowEmployee() {                   // When:
            // given                                       //      Cuando hace clic en el boton Enviar.
            Empleado e1 = Empleado.builder()
                .nombre("Julian")                  // Then:
                .apellido("Chavarro")            //      Se deben mostrar los mensajes
                .email("julian@gmail.com")          //      de validacion apropiados.
                .build();
            empleadoRepository.save(e1);
            empleadoRepository.save(e);

            // when
            List<Empleado> listEmployee = empleadoRepository.findAll();


            // then
            assertThat(listEmployee).isNotNull();
            assertThat(listEmployee.size()).isEqualTo(2);
        }

        @Test
        @DisplayName("Test para obtener un empleado por ID")
        public void testGetEmployeeById() {
            empleadoRepository.save(e);

            // when - comportamiento o accion que vamos a probar
            Empleado emp = empleadoRepository.findById(e.getId()).get();

            // then
            assertThat(emp).isNotNull();
        }

        @Test
        @DisplayName("Test para actualizar un empleado")
        public void testUpdateEmployee() {
            empleadoRepository.save(e);

            // when
            Empleado empSaved = empleadoRepository.findById(e.getId()).get();
            empSaved.setNombre("Juan");
            empSaved.setApellido("Mera");
            empSaved.setEmail("juan@juan.com");
            Empleado empUpdated = empleadoRepository.save(empSaved);

            // then
            assertThat(empUpdated.getEmail()).isEqualTo("juan@juan.com");
            assertThat(empUpdated.getNombre()).isEqualTo("Juan");
            assertThat(empUpdated.getApellido()).isEqualTo("Mera");
        }

        @Test
        @DisplayName("Test para eliminar un employee")
        public void testDeleteEmployee() {
            empleadoRepository.save(e);

            // when
            empleadoRepository.deleteById(e.getId());
            Optional<Empleado> empOptional = empleadoRepository.findById(e.getId());

            // then
            assertThat(empOptional).isEmpty();
        }

    
}
