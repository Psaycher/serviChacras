package com.FinalEgg.ServiChacras.repositorios;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.FinalEgg.ServiChacras.entidades.Usuario;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {
    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Usuario buscarPorEmail(@Param("email") String email);

    @Query("SELECT u.id FROM Usuario u WHERE u.email = :email")
    public String getIdPorEmail(@Param("email") String email);

    @Query("SELECT u FROM Usuario u WHERE u.barrio = :barrio AND u.direccion = :direccion")
    public List<Usuario> getPorDireccion(@Param("barrio") String barrio, @Param("direccion") String direccion);

    @Query("SELECT CONCAT(c.usuario.nombre, ' ', c.usuario.apellido) AS cliente,"+
           " CONCAT(c.usuario.barrio, ' ', c.usuario.direccion) AS direccion, c.usuario.email FROM Cliente c")
    public List<Object> getClientes();

    @Query("SELECT CONCAT(u.nombre, ' ', u.apellido) AS proveedor, CONCAT(u.barrio, ' ', u.direccion) AS direccion,"+
           " u.email, s.nombre AS servicio, s.detalle AS detalleServicio FROM Proveedor p JOIN p.usuario u JOIN p.servicio s")
    public List<Object> getProveedores();
}