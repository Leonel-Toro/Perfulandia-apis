package com.perfulandia.ventas_api.service;

import com.perfulandia.ventas_api.dto.RegistroVendedorDTO;
import com.perfulandia.ventas_api.models.Vendedor;
import com.perfulandia.ventas_api.repository.VendedorRepository;
import com.usuarios_api.dto.RegistroUsuarioRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class VendedorService {
    @Autowired
    private VendedorRepository vendedorRepository;
    @Autowired
    private RestTemplate restTemplate;

    public List<Vendedor> getAll() {
        return vendedorRepository.findAll();
    }

    public Vendedor getById(Long id) {
        Optional<Vendedor> vendedor = vendedorRepository.findById(id);
        return vendedor.orElse(null);
    }

    public void registrarVendedor(RegistroVendedorDTO request){
        Vendedor vendedor = new Vendedor();
        vendedor.setSucursal(request.getSucursal());
        vendedor.setMetaMensual(request.getMetaMensual());

        RegistroUsuarioRequest newUser = new RegistroUsuarioRequest();
        newUser.setCorreo(request.getCorreo());
        newUser.setPassword(request.getPassword());
        newUser.setRol("VENDEDOR");

        try {
            restTemplate.postForEntity("http://localhost:8081/api/usuarios", newUser, Void.class);
            vendedorRepository.save(vendedor);
        }catch (HttpClientErrorException error){
            throw new RuntimeException(error.getResponseBodyAsString());
        }
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
