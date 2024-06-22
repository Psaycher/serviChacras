package com.FinalEgg.ServiChacras.entidades;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import com.FinalEgg.ServiChacras.enumeraciones.Estado;
import com.FinalEgg.ServiChacras.enumeraciones.TipoDeNota;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Notificacion {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String asunto;
    private String remitente;
    
   @Enumerated(EnumType.STRING)
    private TipoDeNota nota;

    @Enumerated(EnumType.STRING)
    private Estado visto;

    @ManyToOne
    @JoinColumn(name = "id_destinatario")
    private Usuario destinatario;

    @Column(name = "id_pedido")
    private String pedido;

    @Column(name = "id_pago")
    private String pago;

    @Column(name = "id_denuncia")
    private String denuncia;
}