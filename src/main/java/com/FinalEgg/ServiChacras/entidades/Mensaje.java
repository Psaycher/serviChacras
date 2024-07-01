package com.FinalEgg.ServiChacras.entidades;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import com.FinalEgg.ServiChacras.enumeraciones.Rol;
import com.FinalEgg.ServiChacras.enumeraciones.Estado;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Mensaje {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name ="uuid", strategy = "uuid2")
    private String id;
    private String asunto;
    private String remitente;

    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Enumerated(EnumType.STRING)
    private Estado visto;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_mensaje")
    private Date fechaMensaje;

    private String contenido;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name ="id_imagen")
    private Imagen imagen;
}