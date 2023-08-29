package com.api.rest.empleado;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {
    
    @Autowired
    private EmpleadoService empleadoService;

    @PostMapping("/save/employee")
    @ResponseStatus(HttpStatus.CREATED)
    public Empleado saveEmployee(@RequestBody Empleado empleado) {
        return empleadoService.saveEmpleado(empleado);
    }

    @GetMapping("/get/all/employee")
    public List<Empleado> getAllEmployee() {
        return empleadoService.getAllEmpleados();
    }

    @GetMapping("/get/employee/by/{id}")
    public ResponseEntity<Empleado> getByIdEmployee(@PathVariable("id") long id) {
        return empleadoService.getEmpleadoById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/employee/{id}")
    public ResponseEntity<Empleado> updateEmployee(@PathVariable("id") long id, @RequestBody Empleado e) {
        return empleadoService.getEmpleadoById(id)
                .map(empleadoSaved -> {
                    empleadoSaved.setNombre(e.getNombre());
                    empleadoSaved.setApellido(e.getApellido());
                    empleadoSaved.setEmail(e.getEmail());

                    Empleado emp = empleadoService.updateEmpleado(empleadoSaved);
                    return new ResponseEntity<>(emp, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/employee/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") long id) {
        empleadoService.deleteEmpleado(id);
        return new ResponseEntity<String>("Empleado eliminado exitosamente", HttpStatus.OK);
    }
}
