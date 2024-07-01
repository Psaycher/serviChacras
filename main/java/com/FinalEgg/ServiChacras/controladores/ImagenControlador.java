package com.FinalEgg.ServiChacras.controladores;

import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.FinalEgg.ServiChacras.entidades.Proveedor;
import com.FinalEgg.ServiChacras.servicios.ProveedorServicio;

@Controller
@RequestMapping("/imagen")
public class ImagenControlador {

    @Autowired
    private ProveedorServicio proveedorServicio;

    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> Imagenproveedor(@PathVariable String id) {

        Proveedor proveedor = proveedorServicio.getOne(id);

        byte[] imagen = proveedor.getImagen().getContenido();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }
}