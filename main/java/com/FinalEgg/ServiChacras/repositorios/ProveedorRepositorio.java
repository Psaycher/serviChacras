package com.FinalEgg.ServiChacras.repositorios;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.FinalEgg.ServiChacras.entidades.Proveedor;

@Repository
public interface ProveedorRepositorio extends JpaRepository<Proveedor, String> {
       @Query("SELECT p.id FROM Proveedor p WHERE p.usuario.id = :idUsuario")
       public String idUsuario(@Param("idUsuario") String idUsuario);

       @Query("SELECT p FROM Proveedor p WHERE p.usuario.email = :email")
       public Proveedor getPorEmail(@Param("email") String email);

       @Query("SELECT p FROM Proveedor p WHERE p.usuario.nombre LIKE %:nombreUsuario% OR p.usuario.apellido LIKE %:nombreUsuario%")
       public List<Proveedor> getPorNombreCompleto(@Param("nombreUsuario") String nombreUsuario);

       @Query("SELECT p.servicio.id AS id_servicio FROM Proveedor p WHERE p.id = :idProveedor")
       public String getIdServicio(@Param("idProveedor") String idProveedor);

       @Query("SELECT p FROM Proveedor p WHERE p.usuario.barrio = :barrio")
       public List<Proveedor> getPorBarrio(@Param("barrio") String barrio);

       @Query("SELECT p FROM Proveedor p WHERE p.usuario.barrio = :barrio AND p.usuario.direccion = :direccion")
       public List<Proveedor> getPorDireccion(@Param("barrio") String barrio, @Param("direccion") String direccion);

       @Query("SELECT p FROM Pedido p WHERE p.id = :idPedido")
       public Proveedor getPorPedido(@Param("idPedido") String idPedido);

       @Query("SELECT p FROM Proveedor p WHERE p.servicio.id = :idServicio")
       public List<Proveedor> getPorServicio(@Param("idServicio") String idServicio);

       @Query("SELECT p.id AS pedido, CONCAT(p.cliente.usuario.nombre, ' ', p.cliente.usuario.apellido) AS cliente,"+
              " p.comentario FROM Pedido p WHERE p.cliente.id = :idCliente")
       public List<Object> getComentarios(@Param("idCliente") String idCliente);

       @Query("SELECT CONCAT(p.usuario.nombre, ' ', p.usuario.apellido) AS proveedor,"+
              " p.promPuntuacion FROM Proveedor p WHERE p.id = :idProveedor")
       public Integer getPuntuaciones(@Param("idProveedor") String idProveedor);

       @Query("SELECT p FROM Proveedor p WHERE p.servicio.id = :idServicio AND "+
              "(p.usuario.nombre LIKE %:nombreUsuario% OR p.usuario.apellido LIKE %:nombreUsuario%)")
       public List<Proveedor> getPorServicioYNombre(@Param("nombreUsuario") String nombreUsuario, @Param("idServicio") String idServicio);

       @Query("SELECT p FROM Proveedor p WHERE p.servicio.id = :idServicio AND p.usuario.barrio = :barrio")
       public List<Proveedor> getPorServicioYBarrio(@Param("idServicio") String idServicio, @Param("barrio") String barrio);

       @Query("SELECT p FROM Proveedor p WHERE p.usuario.barrio = :barrio AND "+
              "(p.usuario.nombre LIKE %:nombreUsuario% OR p.usuario.apellido LIKE %:nombreUsuario%)")
       public List<Proveedor> getPorBarrioYNombre(@Param("nombreUsuario") String nombreUsuario, @Param("barrio") String barrio);

       @Query("SELECT p FROM Proveedor p WHERE p.servicio.id = :idServicio AND p.usuario.barrio = :barrio AND "+
              "(p.usuario.nombre LIKE %:nombreUsuario% OR p.usuario.apellido LIKE %:nombreUsuario%)")
       public List<Proveedor> getPorServicioBarrioYNombre(@Param("idServicio") String idServicio, @Param("nombreUsuario") String nombreUsuario, @Param("barrio") String barrio);
}