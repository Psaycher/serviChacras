package com.FinalEgg.ServiChacras.repositorios;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.FinalEgg.ServiChacras.entidades.Pago;

@Repository
public interface PagoRepositorio extends JpaRepository<Pago, String> {
       @Query("SELECT p.id FROM Pago p WHERE p.pedido.id = :idPedido")
       public String idPorPedido(@Param("idPedido") String idPedido);

       @Query("SELECT n.pedido FROM Notificacion n WHERE n.id = :idNotificacion")
       public String IdPedidoPorNotificacion(@Param("idNotificacion") String idNotificacion);

       @Query("SELECT p FROM Pago p WHERE p.pedido.id = :idPedido")
       public Pago getPagoPorPedido(@Param("idPedido") String idPedido);

       @Query("SELECT p FROM Pago p WHERE p.cliente.id = :idCliente")
       public List<Pago> getPagoPorClientes(@Param("idCliente") String idCliente);

       @Query("SELECT p FROM Pago p WHERE p.proveedor.id = :idProveedor")
       public List<Pago> getPagoPorProveedores(@Param("idProveedor") String idProveedor);

       @Query("SELECT p FROM Pago p WHERE p.estado = 'PENDIENTE'")
       public List<Pago> getPagosPendiente();

       @Query("SELECT p FROM Pago p WHERE p.estado = 'PENDIENTE' AND p.cliente.id = :idCliente")
       public List<Pago> getPagosPendientePorCliente(@Param("idCliente") String idCliente);

       @Query("SELECT p FROM Pago p WHERE p.estado = 'PENDIENTE' AND p.proveedor.id = :idProveedor")
       public List<Pago> getPagosPendientePorProveedor(@Param("idProveedor") String idProveedor);
}