package com.FinalEgg.ServiChacras.repositorios;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.FinalEgg.ServiChacras.entidades.Notificacion;

@Repository
public interface NotificacionRepositorio extends JpaRepository<Notificacion, String> {
    @Query("SELECT n FROM Notificacion n WHERE n.pago = :idPago")
    public List<Notificacion> notificacionPorPago( @Param("idPago") String idPago);

    @Query("SELECT n FROM Notificacion n WHERE n.pago = :idPago")
    public List<Notificacion> listarPorPago( @Param("idPago") String idPago);

    @Query("SELECT n FROM Notificacion n WHERE n.pago = :idPago AND n.visto LIKE 'EFECTUADO'")
    public List<Notificacion> getPorPagoVisto( @Param("idPago") String idPago);

    @Query("SELECT n FROM Notificacion n WHERE n.pago = :idPago AND n.visto LIKE 'PENDIENTE'")
    public List<Notificacion> getPorPagoNoVisto( @Param("idPago") String idPago);

    @Query("SELECT n FROM Notificacion n WHERE n.pedido = :idPedido")
    public List<Notificacion> listarPorPedido( @Param("idPedido") String idPedido);
    
    @Query("SELECT n FROM Notificacion n WHERE n.pedido = :idPedido AND n.visto LIKE 'EFECTUADO'")
    public List<Notificacion> getPorPedidoVisto( @Param("idPedido") String idPedido);

    @Query("SELECT n FROM Notificacion n WHERE n.pedido = :idPedido AND n.visto LIKE 'PENDIENTE'")
    public List<Notificacion> getPorPedidoNoVisto( @Param("idPedido") String idPedido);

    @Query("SELECT n FROM Notificacion n WHERE n.denuncia = :idDenuncia")
    public List<Notificacion> listarPorDenuncia( @Param("idDenuncia") String idDenuncia);

    @Query("SELECT n FROM Notificacion n WHERE n.denuncia = :idDenuncia AND n.visto LIKE 'EFECTUADO'")
    public List<Notificacion> getPorDenunciaVisto( @Param("idDenuncia") String idDenuncia);

    @Query("SELECT n FROM Notificacion n WHERE n.denuncia = :idDenuncia AND n.visto LIKE 'PENDIENTE'")
    public List<Notificacion> getPorDenunciaNoVisto( @Param("idDenuncia") String idDenuncia);  

    @Query("SELECT n FROM Notificacion n WHERE n.destinatario.id = :idUsuario AND n.visto LIKE 'PENDIENTE'")
    public List<Notificacion> getPorUsuarioNoVisto( @Param("idUsuario") String idUsuario);

    @Query("SELECT COUNT(n) FROM Notificacion n WHERE n.destinatario.id = :idUsuario AND n.visto LIKE 'PENDIENTE'")
    public Integer contarPorUsuarioNoVisto( @Param("idUsuario") String idUsuario);
}