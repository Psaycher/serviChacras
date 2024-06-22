package com.FinalEgg.ServiChacras.repositorios;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.FinalEgg.ServiChacras.entidades.Mensaje;

@Repository
public interface MensajeRepositorio extends JpaRepository<Mensaje, String> {
       @Query("SELECT m.contenido FROM Mensaje m WHERE m.usuario.id = :idUsuario")
       public List<Mensaje> bandejaDeSalida(@Param("idUsuario") String idUsuario);

       @Query("SELECT CONCAT(u.nombre, ' ', u.apellido) AS remitente FROM Usuario u WHERE u.id = :idRemitente")
       public String getRemitente(@Param("idRemitente") String idRemitente);

       @Query("SELECT m FROM Mensaje m WHERE m.pedido.id IN (SELECT m1.pedido.id FROM Mensaje m1 WHERE m1.usuario.id = :idUsuario) "+
              "AND m.rol <> (SELECT u.rol FROM Usuario u WHERE u.id = :idUsuario)")
       public List<Mensaje> bandejaDeEntrada(@Param("idUsuario") String idUsuario);

       @Query("SELECT m FROM Mensaje m WHERE m.usuario.id = :idUsuario AND m.visto LIKE 'PENDIENTE'")
       public List<Mensaje> getPorUsuarioNoVisto( @Param("idUsuario") String idUsuario);

       @Query("SELECT COUNT(m) FROM Mensaje m WHERE m.usuario.id = :idUsuario AND m.visto LIKE 'PENDIENTE'")
       public Integer contarPorUsuarioNoVisto( @Param("idUsuario") String idUsuario);

       @Query("SELECT m FROM Mensaje m WHERE m.pedido.id = :idPedido AND (m.rol = 'CLIENTE' OR m.rol = 'PROVEEDOR')")
       public List<Mensaje> conversacionPorPedido(@Param("idPedido") String idPedido);

       @Query("SELECT m FROM Mensaje m WHERE m.pedido.id = :pedidoId AND m.rol <> (SELECT u.rol FROM Usuario u WHERE u.id = :idUsuario)")
       public List<Mensaje> msjsRecibidosPorPedido( @Param("pedidoId") String pedidoId);

       @Query("SELECT m FROM Mensaje m WHERE m.rol = 'CLIENTE' AND m.pedido.id = :idUsuario")
       public List<Mensaje> msjsCliente(@Param("idUsuario") String idUsuario);

       @Query("SELECT m FROM Mensaje m WHERE m.rol = 'PROVEEDOR' AND m.pedido.id = :idUsuario")
       public List<Mensaje> msjsProveedor(@Param("idUsuario") String idUsuario);

       @Query("SELECT m FROM Mensaje m WHERE m.pedido.id = :idPedido AND m.fechaMensaje = :fecha")
       public List<Mensaje> porPedidoYFecha(@Param("idPedido") String idPedido, @Param("fecha") Date fecha);

       @Query("SELECT m FROM Mensaje m WHERE m.pedido.id = :idPedido AND m.fechaMensaje BETWEEN :inicio AND :fin")
       public List<Mensaje> porPedidoYEntreFechas(@Param("idPedido") String idPedido, @Param("inicio") Date fechaInicio, @Param("fin") Date fechaFin);
}