package com.perfulandia.ventas_api.service;

import com.clientes_api.models.Cliente;
import com.perfulandia.ventas_api.models.Vendedor;
import com.perfulandia.ventas_api.repository.VendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VendedorService {
    @Autowired
    private VendedorRepository vendedorRepository;

    public List<Vendedor> getAll() {
        return vendedorRepository.findAll();
    }

    public Vendedor getById(Long id) {
        Optional<Vendedor> vendedor = vendedorRepository.findById(id);
        return vendedor.orElse(null);
    }

    public Vendedor save(Vendedor vendedor) {
        return vendedorRepository.save(vendedor);
    }


    public Vendedor update(Long id, Vendedor vendedor) {
        if (vendedorRepository.existsById(id)) {
            vendedor.setIdVendedor(id);
            return vendedorRepository.save(vendedor);
        }
        return null; // No se encontró la Cliente
    }

    public Vendedor delete(Long id) {
        Optional<Vendedor> vendedor = vendedorRepository.findById(id);
        if (vendedor.isPresent()) {
            vendedorRepository.deleteById(id);
            return vendedor.get();
        }
        return null;
    }
}
