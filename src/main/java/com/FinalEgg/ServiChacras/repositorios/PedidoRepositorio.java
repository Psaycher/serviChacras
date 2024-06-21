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
       @Query("SELECT p.id FROM Pedido p WHERE p.cliente.id = :idCliente")
       public String getIdPedidoPorClientes(@Param("idCliente") String idCliente);

       @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :idCliente")
       public List<Pedido> getPedidoPorClientes(@Param("idCliente") String idCliente);

       @Query("SELECT p FROM Pedido p WHERE p.proveedor.id = :idProveedor")
       public List<Pedido> getPedidoPorProveedores(@Param("idProveedor") String idProveedor);

       @Query("SELECT p.id FROM Pedido p WHERE p.proveedor.id = :idProveedor")
       public String getIdPedidoPorProveedores(@Param("idProveedor") String idProveedor);

       @Query("SELECT p FROM Pedido p WHERE p.estado = 'PENDIENTE'")
       public List<Pedido> getPedidosPendiente();

       @Query("SELECT p FROM Pedido p WHERE p.fechaPedido = :fecha")
       public List<Pedido> getPedidosPorFecha(@Param("fecha") Date fecha);

       @Query("SELECT p FROM Pedido p WHERE p.fechaPedido BETWEEN :inicio AND :fin")
       public List<Pedido> getPedidosEntreFechas(@Param("inicio") Date fechaInicio, @Param("fin") Date fechaFin);
}
