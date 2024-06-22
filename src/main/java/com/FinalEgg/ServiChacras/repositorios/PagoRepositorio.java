package com.FinalEgg.ServiChacras.repositorios;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.FinalEgg.ServiChacras.entidades.Pago;

@Repository
public interface PagoRepositorio extends JpaRepository<Pago, String> {
    @Query("SELECT p.id AS pago, p.valor FROM Pago p WHERE p.id = :idPago")
    public Integer puntuacionPorPago(@Param("idPago") String idPago);

    @Query("SELECT p.id AS pago, CONCAT(p.cliente.usuario.nombre, ' ', p.cliente.usuario.apellido) AS cliente,"+
           " p.estado, p.valor FROM Pago p WHERE p.cliente.id = :idCliente")
    public List<Object> getPagoPorClientes(@Param("idCliente") String idCliente);

    @Query("SELECT p.id AS pago, CONCAT(p.proveedor.usuario.nombre, ' ', p.proveedor.usuario.apellido) AS proveedor,"+
           " p.estado, p.valor FROM Pago p WHERE p.proveedor.id = :idProveedor")
    public List<Object> getPagoPorProveedores(@Param("idProveedor") String idProveedor);

    @Query("SELECT p.id AS pago, CONCAT(p.proveedor.usuario.nombre, ' ', p.proveedor.usuario.apellido) AS proveedor,"+
           " CONCAT(p.cliente.usuario.nombre, ' ', p.cliente.usuario.apellido) AS cliente,"+
           " p.cliente.usuario.email AS correo, p.valor FROM Pago p WHERE p.estado = 'PENDIENTE'")
    public List<Object> getPagosPendiente();
}