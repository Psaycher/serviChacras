package com.FinalEgg.ServiChacras.entidades;

import java.util.Date;
import java.util.List;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import com.FinalEgg.ServiChacras.enumeraciones.Estado;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Pedido {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name ="uuid", strategy = "uuid2")
    private String id;
    private String asunto;
    private String detalle;

    @ManyToOne
    @JoinColumn(name ="id_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name ="id_servicio")
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name ="id_proveedor")
    private Proveedor proveedor;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name ="id_pago")
    private Pago pago;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_pedido")
    private Date fechaPedido;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    private String comentario;
    private boolean alta;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mensaje> mensajes;
}
