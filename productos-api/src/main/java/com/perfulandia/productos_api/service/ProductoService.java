package com.perfulandia.productos_api.service;

import com.perfulandia.productos_api.dto.ProductoInventarioDTO;
import com.perfulandia.productos_api.models.Categoria;
import com.perfulandia.productos_api.models.Inventario;
import com.perfulandia.productos_api.models.Producto;
import com.perfulandia.productos_api.repository.CategoriaRepository;
import com.perfulandia.productos_api.repository.InventarioRepository;
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

    @Autowired
    InventarioRepository inventarioRepository;

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

    public Producto nuevoProducto(ProductoInventarioDTO nuevoProducto){
        List<String> errores = new ArrayList<>();
        Producto producto = new Producto();
        Inventario inventario = new Inventario();
        if(nuevoProducto.getNombreProducto() == null || nuevoProducto.getNombreProducto().equals("")){
            errores.add("Nombre del producto no puede estar vacio");
        }else{
            producto.setNombreProducto(nuevoProducto.getNombreProducto());
        }
        if(nuevoProducto.getDescripcionProducto() == null || nuevoProducto.getDescripcionProducto().equals("")){
            errores.add("Descripcion del producto no puede estar vacio");
        }else{
            producto.setDescripcionProducto(nuevoProducto.getDescripcionProducto());
        }
        if (nuevoProducto.getPrecio() == null || nuevoProducto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            errores.add("El precio no puede ser vacio, negativo o cero");
        }else{
            producto.setPrecio(nuevoProducto.getPrecio());
        }

        if(nuevoProducto.getCategoria() == null){
            errores.add("La categoria no puede ser vacia");
        }else{
            if(!categoriaRepository.existsById(nuevoProducto.getCategoria().getId())){
                errores.add("La categoria no existe.");
            }else{
                producto.setCategoria(nuevoProducto.getCategoria());
            }
        }

        if(nuevoProducto.getCantidad() <=0){
            errores.add("La cantidad no puede ser vacio, negativo o cero");
        }else{
            inventario.setCantidad(nuevoProducto.getCantidad());
        }

        if(nuevoProducto.getMarca() == null || nuevoProducto.getMarca().equals("")){
            errores.add("La marca del producto no puede estar vacia");
        }else{
            producto.setMarca(nuevoProducto.getMarca());
        }
        if(nuevoProducto.getUbicacion() == null || nuevoProducto.getUbicacion().equals("")){
            errores.add("La ubicación del producto no puede estar vacia");
        }else{
            inventario.setUbicacion(nuevoProducto.getUbicacion());
        }

        if (!errores.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", errores));
        }

        productoRepository.save(producto);
        inventario.setProducto(producto);
        inventarioRepository.save(inventario);
        return producto;
    }

    public List<Producto> listaProductosByCategoria(Long idCategoria){
        return productoRepository.findByCategoriaId(idCategoria);
    }
}
