package com.FinalEgg.ServiChacras.repositorios;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.FinalEgg.ServiChacras.entidades.Servicio;

@Repository
public interface ServicioRepositorio extends JpaRepository<Servicio, String> {
    @Query("SELECT p.servicio.nombre AS servicio, CONCAT(p.usuario.nombre, ' ', p.usuario.apellido) AS proveedor,"+
           " p.servicio.detalle FROM Proveedor p WHERE p.id = :idProveedor")
    public List<Object> getServicioPorProveedores(@Param("idProveedor") String idProveedor);

    @Query("SELECT p.id AS pedido, p.servicio.nombre AS servicio, CONCAT(p.proveedor.usuario.nombre, ' ', p.proveedor.usuario.apellido) AS proveedor,"+
           " p.servicio.detalle, p.estado FROM Pedido p WHERE p.id = :idPedido")
    public List<Object> getServicioPorPedido(@Param("idPedido") String idPedido);

    @Query("SELECT s FROM Servicio s WHERE s.categoria = :categoria")
    public List<Servicio> listarPorCategoria(@Param("categoria") String categoria);
}