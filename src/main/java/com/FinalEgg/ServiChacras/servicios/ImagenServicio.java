package com.FinalEgg.ServiChacras.servicios;

import com.FinalEgg.ServiChacras.entidades.Imagen;
import com.FinalEgg.ServiChacras.excepciones.MiExcepcion;
import com.FinalEgg.ServiChacras.repositorios.ImagenRepositorio;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ImagenServicio {

    @Autowired
    private ImagenRepositorio imagenRepositorio;

    public Imagen guardar(MultipartFile archivo) throws MiExcepcion {

        if (archivo != null) {
            try {
                Imagen imagen = new Imagen();
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imagenRepositorio.save(imagen);

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

    @Transactional
    public Imagen actualizar(MultipartFile archivo, String idImagen) throws MiExcepcion{

        if (archivo != null) {
            try {
                Imagen imagen = new Imagen();

                if(idImagen != null) {
                    Optional<Imagen> respuesta = imagenRepositorio.findById(idImagen);

                    if(respuesta.isPresent()) { imagen = respuesta.get(); }
                }

                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imagenRepositorio.save(imagen);

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<Imagen> obtenerTodasLasTarjetas() { return imagenRepositorio.findAll(); }

    @Transactional(readOnly = true)
    public Imagen getOne(String id) { return imagenRepositorio.getOne(id); }
}