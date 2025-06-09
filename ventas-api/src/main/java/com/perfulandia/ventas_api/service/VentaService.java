package com.perfulandia.ventas_api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.perfulandia.ventas_api.client.ClienteRestClient;
import com.perfulandia.ventas_api.client.CuponRestClient;
import com.perfulandia.ventas_api.dto.ClienteDTO;
import com.perfulandia.ventas_api.dto.CuponRequestDTO;
import com.perfulandia.ventas_api.dto.CuponResponseDTO;
import com.perfulandia.ventas_api.models.Venta;
import com.perfulandia.ventas_api.repository.VentaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ClienteRestClient clienteRest;
    private final CuponRestClient cuponRest;

    public Venta procesarVenta(Venta nuevaVenta, String codigoCupon){
        ClienteDTO cliente = clienteRest.findById(nuevaVenta.getIdCliente());
        if(cliente == null){
            return null;
        }

        CuponRequestDTO requestDTO = new CuponRequestDTO(codigoCupon);
        CuponResponseDTO cuponResponse = cuponRest.validarCupon(requestDTO);

        if(cuponResponse == null || !cuponResponse.isValido()){
            return null;
        }

        BigDecimal descuento = cuponResponse.getDescuento();
        if(descuento != null){
            BigDecimal totalConDescuento = nuevaVenta.getTotal().subtract(descuento);
            nuevaVenta.setTotal(totalConDescuento.max(BigDecimal.ZERO));
        }

        return ventaRepository.save(nuevaVenta);
    }

    public Venta save(Venta venta, String codigoCupon){
        return procesarVenta(venta, codigoCupon);
    }

    public List<Venta> getVentas(){
        return ventaRepository.findAll();
    }

    public Venta findById(Long id){
        Optional<Venta> venta = ventaRepository.findById(id);
        return venta.orElse(null);
    }

    public List<Venta> getVentasByIdCliente(Long idCliente) {
        return ventaRepository.findByIdCliente(idCliente);
    }
}
