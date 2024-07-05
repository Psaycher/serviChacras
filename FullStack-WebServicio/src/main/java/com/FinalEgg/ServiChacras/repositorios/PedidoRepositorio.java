package com.FinalEgg.ServiChacras.repositorios;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.FinalEgg.ServiChacras.entidades.Pedido;

@Repository
public interface PedidoRepositorio extends JpaRepository<Pedido, String> {
       @Query("SELECT p FROM Pedido p WHERE p.detalle = :detalle")
       public Pedido getPorDetalle(@Param("detalle") String detalle);

       @Query("SELECT p FROM Pedido p WHERE p.pago.id = :idPago")
       public Pedido getPorPago(@Param("idPago") String idPago);
       
       @Query("SELECT p.id FROM Pedido p WHERE p.cliente.id = :idCliente")
       public String getIdPedidoPorCliente(@Param("idCliente") String idCliente);

       @Query("SELECT COUNT(p) FROM Pedido p WHERE p.cliente.id = :idCliente")
       public Integer contarPorCliente(@Param("idCliente") String idCliente);

       @Query("SELECT COUNT(p) FROM Pedido p WHERE p.proveedor.id = :idProveedor")
       public Integer contarPorProveedor(@Param("idProveedor") String idProveedor);

       @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :idCliente")
       public List<Pedido> getPedidosPorClientes(@Param("idCliente") String idCliente);       

       @Query("SELECT COUNT(p) FROM Pedido p WHERE p.cliente.id = :idCliente")
       public Integer contarPedidosPorCliente(@Param("idCliente") String idCliente);

       @Query("SELECT p FROM Pedido p WHERE p.proveedor.id = :idProveedor")
       public List<Pedido> getPedidosPorProveedores(@Param("idProveedor") String idProveedor);

       @Query("SELECT p.id FROM Pedido p WHERE p.proveedor.id = :idProveedor")
       public String getIdPedidoPorProveedor(@Param("idProveedor") String idProveedor);

       @Query("SELECT COUNT(p) FROM Pedido p WHERE p.proveedor.id = :idProveedor")
       public Integer contarPedidosPorProveedor(@Param("idProveedor") String idProveedor);

       @Query("SELECT p FROM Pedido p WHERE p.estado = 'PENDIENTE'")
       public List<Pedido> getPedidosPendiente();

       @Query("SELECT p FROM Pedido p WHERE p.fechaPedido = :fecha")
       public List<Pedido> getPedidosPorFecha(@Param("fecha") Date fecha);

       @Query("SELECT p FROM Pedido p WHERE p.fechaPedido BETWEEN :inicio AND :fin")
       public List<Pedido> getPedidosEntreFechas(@Param("inicio") Date fechaInicio, @Param("fin") Date fechaFin);

       @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :idCliente AND p.proveedor.id = :idProveedor")
       public List<Pedido> getPedidosCompartidos(@Param("idCliente") String idCliente, @Param("idProveedor") String idProveedor);
}
