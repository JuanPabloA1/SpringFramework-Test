package com.api.rest.empleado;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoService {
    
    @Autowired
    private EmpleadoRepository empleadoRepository;

    public Empleado saveEmpleado(Empleado e) {
        Optional<Empleado> employe = empleadoRepository.findByEmail(e.getEmail());
        if (employe.isPresent()) {
            throw new RuntimeException("El empleado con ese email ya no existe: " + e.getEmail());
        }
        return empleadoRepository.save(e);
    }

    public List<Empleado> getAllEmpleados() {
        return empleadoRepository.findAll();
    }

    public Optional<Empleado> getEmpleadoById(long id) {
        return empleadoRepository.findById(id);
    }

    public Empleado updateEmpleado(Empleado e) {
        return empleadoRepository.save(e);
    }

    public void deleteEmpleado(long id) {
        empleadoRepository.deleteById(id);
    }
}
