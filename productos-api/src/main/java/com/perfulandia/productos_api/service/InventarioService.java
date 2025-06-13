package com.perfulandia.productos_api.service;

import com.perfulandia.productos_api.models.Inventario;
import com.perfulandia.productos_api.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioService {
    @Autowired
    private InventarioRepository inventarioRepository;

    public List<Inventario> findAll() {
        return inventarioRepository.findAll();
    }

    public Inventario guardarInventario(Inventario inventario) {
        // Validación del producto
        if (inventario.getProducto() == null || inventario.getProducto().getId() == null) {
            throw new RuntimeException("Debe asignarse un producto válido al inventario.");
        }

        // Validación de cantidad
        if (inventario.getCantidad() < 0) {
            throw new RuntimeException("La cantidad no puede ser negativa.");
        }

        // Validación de ubicación
        if (inventario.getUbicacion() == null || inventario.getUbicacion().isBlank()) {
            throw new RuntimeException("La ubicación no puede estar vacía.");
        }

        return inventarioRepository.save(inventario);
    }

    // Actualizar inventario
    public Inventario actualizarInventario(Long id, Inventario nuevoInventario) {
        Inventario existente = inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario con ID " + id + " no encontrado."));

        if (nuevoInventario.getCantidad() < 0) {
            throw new RuntimeException("La cantidad no puede ser negativa.");
        }

        if (nuevoInventario.getUbicacion() == null || nuevoInventario.getUbicacion().isBlank()) {
            throw new RuntimeException("La ubicación no puede estar vacía.");
        }

        existente.setCantidad(nuevoInventario.getCantidad());
        existente.setUbicacion(nuevoInventario.getUbicacion());

        return inventarioRepository.save(existente);
    }

    public Inventario findByIdProducto(Long idProducto){
        Optional<Inventario> inventarioOpt = inventarioRepository.findByProducto_Id(idProducto);
        if(inventarioOpt.isPresent()){
            return inventarioOpt.get();
        }
        return null;
    }
}
