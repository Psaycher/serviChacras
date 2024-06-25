package com.FinalEgg.ServiChacras.repositorios;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.FinalEgg.ServiChacras.entidades.Cliente;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, String> {
       @Query("SELECT id FROM Cliente c WHERE c.usuario.id = :idUsuario")
       public String idUsuario(@Param("idUsuario") String idUsuario);

       @Query("SELECT c FROM Cliente c WHERE c.usuario.email = :email")
       public Cliente getPorEmail(@Param("email") String email);

       @Query("SELECT c FROM Cliente c WHERE c.usuario.nombre LIKE %:nombreUsuario% OR c.usuario.apellido LIKE %:nombreUsuario%")
       public List<Cliente> getPorNombreCompleto(@Param("nombreUsuario") String nombreUsuario);

       @Query("SELECT p.cliente FROM Pedido p WHERE p.proveedor.id = :idProveedor AND "+
              "(p.cliente.usuario.nombre LIKE %:nombreUsuario% OR p.cliente.usuario.apellido LIKE %:nombreUsuario%)")
       public List<Cliente> getPorNombreCompartido(@Param("nombreUsuario") String nombreUsuario, @Param("idProveedor") String idProveedor);

       @Query("SELECT c FROM Cliente c WHERE c.usuario.barrio = :barrio")
       public List<Cliente> getPorBarrio(@Param("barrio") String barrio);

       @Query("SELECT p.cliente FROM Pedido p WHERE p.cliente.usuario.barrio = :barrio AND p.id = "+
              "(SELECT ped.id FROM Pedido ped WHERE ped.proveedor.id = :idProveedor)")
       public List<Cliente> getPorBarrioCompartido(@Param("barrio") String barrio, @Param("idProveedor") String idProveedor);

       @Query("SELECT c FROM Cliente c WHERE c.usuario.barrio = :barrio AND c.usuario.direccion = :direccion")
       public List<Cliente> getPorDireccion(@Param("barrio") String barrio, @Param("direccion") String direccion);

       @Query("SELECT p.cliente FROM Pedido p WHERE p.id = :idPedido")
       public Cliente getPorPedido(@Param("idPedido") String idPedido);

       @Query("SELECT p.cliente FROM Pedido p WHERE p.id = :idPedido AND "+
              "(p.cliente.usuario.nombre LIKE %:nombreUsuario% OR p.cliente.usuario.apellido LIKE %:nombreUsuario%)")
       public List<Cliente> getPorPedidoYNombre(@Param("nombreUsuario") String nombreUsuario, @Param("idPedido") String idPedido);

       @Query("SELECT p.cliente FROM Pedido p WHERE p.id = "+
              "(SELECT ped.id FROM Pedido ped WHERE ped.proveedor.id = :idProveedor)")
       public List<Cliente> getPorPedidoCompartido(@Param("idProveedor") String idProveedor);

       @Query("SELECT CONCAT(p.cliente.usuario.nombre, ' ', p.cliente.usuario.apellido) AS cliente,"+
              " p.cliente.usuario.email AS correo, p.comentario FROM Pedido p WHERE p.cliente.id = :idUsuario")
       public List<Object> getComentarios(@Param("idUsuario") String idUsuario);

       @Query("SELECT c FROM Cliente c WHERE c.usuario.barrio = :barrio AND "+
              "(c.usuario.nombre LIKE %:nombreUsuario% OR c.usuario.apellido LIKE %:nombreUsuario%)")
       public List<Cliente> getPorBarrioYNombre(@Param("nombreUsuario") String nombreUsuario, @Param("barrio") String barrio);

       @Query("SELECT p.cliente FROM Pedido p WHERE p.cliente.usuario.barrio = :barrio AND p.id = :idPedido")
       public List<Cliente> getPorPedidoYBarrio(@Param("barrio") String barrio, @Param("idPedido") String idPedido);

       @Query("SELECT p.cliente FROM Pedido p WHERE p.cliente.usuario.barrio = :barrio AND "+
              "(p.cliente.usuario.nombre LIKE %:nombreUsuario% OR p.cliente.usuario.apellido LIKE %:nombreUsuario%) AND p.id = "+
              "(SELECT ped.id FROM Pedido ped WHERE ped.proveedor.id = :idProveedor)")
       public List<Cliente> getPorBarrioYNombreCompartido(@Param("nombreUsuario") String nombreUsuario, 
                                                          @Param("barrio") String barrio, @Param("idProveedor") String idProveedor);

       @Query("SELECT p.cliente FROM Pedido p WHERE p.id = :idPedido AND "+
              "(p.cliente.usuario.nombre LIKE %:nombreUsuario% OR p.cliente.usuario.apellido LIKE %:nombreUsuario%) AND "+
              "(p.cliente.usuario.barrio =:barrio)")
       public List<Cliente> getPorPedidoBarrioYNombre(@Param("nombreUsuario") String nombreUsuario, 
                                                      @Param("idPedido") String idPedido, @Param("barrio") String barrio);
}