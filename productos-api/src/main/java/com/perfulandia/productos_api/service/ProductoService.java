package com.perfulandia.productos_api.service;

import com.perfulandia.productos_api.models.Categoria;
import com.perfulandia.productos_api.models.Producto;
import com.perfulandia.productos_api.repository.CategoriaRepository;
import com.perfulandia.productos_api.repository.ProductoRepository;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    CategoriaRepository categoriaRepository;

    public List<Producto> verProductos(){
        return productoRepository.findAll();
    }

    public Producto productoPorId(Long idProducto){
        Optional<Producto> productoOptional = productoRepository.findById(idProducto);
        if(productoOptional.isPresent()){
            Producto producto = productoOptional.get();
            return producto;
        }
        return null;
    }

    public Producto nuevoProducto(Producto nuevoProducto){
        List<String> errores = new ArrayList<>();
        if(nuevoProducto.getNombreProducto() == null || nuevoProducto.getNombreProducto().equals("")){
            errores.add("Nombre del producto no puede estar vacio");
        }
        if(nuevoProducto.getDescripcionProducto() == null || nuevoProducto.getDescripcionProducto().equals("")){
            errores.add("Descripcion del producto no puede estar vacio");
        }
        if (nuevoProducto.getPrecio() == null || nuevoProducto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            errores.add("El precio no puede ser vacio, negativo o cero");
        }
        if(nuevoProducto.getCategoria() == null){
            errores.add("La categoria no puede ser vacia");
        }else{
            if(!categoriaRepository.existsById(nuevoProducto.getCategoria().getId())){
                errores.add("La categoria no existe.");
            }
        }

        if(nuevoProducto.getMarca() == null || nuevoProducto.getMarca().equals("")){
            errores.add("La marca del producto no puede estar vacia");
        }
        if (!errores.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", errores));
        }

        productoRepository.save(nuevoProducto);
        return nuevoProducto;
    }

    public List<Producto> listaProductosByCategoria(String nombreCategoria){
        return productoRepository.findByCategoriaNombreCategoria(nombreCategoria);
    }
}
