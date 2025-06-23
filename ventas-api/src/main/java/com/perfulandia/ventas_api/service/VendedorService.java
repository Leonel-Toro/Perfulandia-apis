package com.perfulandia.ventas_api.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.perfulandia.ventas_api.dto.RegistroUsuarioRequest;
import com.perfulandia.ventas_api.dto.RegistroVendedorDTO;
import com.perfulandia.ventas_api.models.Vendedor;
import com.perfulandia.ventas_api.repository.VendedorRepository;

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
            restTemplate.postForEntity("http://localhost:8081/api/usuarios/", newUser, Void.class);
            vendedorRepository.save(vendedor);
        }catch (HttpClientErrorException error){
            throw new RuntimeException(error.getResponseBodyAsString());
        }
    }

    public Vendedor actualizarVendedor(Long id, Vendedor vendedor) {
        List<String> errores = new ArrayList<>();
        Vendedor vendedorUp = new Vendedor();
        if (vendedorRepository.existsById(id)) {
            vendedorUp.setIdVendedor(id);
            if(vendedor.getSucursal() == null || vendedor.getSucursal().equals("")){
                errores.add("Sucursal: La sucursal no debe estar vacia.");
            }

            if(vendedor.getMetaMensual() == null || vendedor.getMetaMensual().compareTo(BigDecimal.ZERO) <= 0){
                errores.add("Meta Mensual: La meta mensual no debe estar vacia o debe ser mayor a 0");
            }
            if (!errores.isEmpty()) {
                throw new IllegalArgumentException(String.join("; ", errores));
            }
            vendedorUp.setMetaMensual(vendedor.getMetaMensual());
            vendedorUp.setSucursal(vendedor.getSucursal());
            vendedorRepository.save(vendedorUp);
            return vendedorUp;
        }
        return null;
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
