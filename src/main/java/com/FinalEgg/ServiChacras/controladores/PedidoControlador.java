/* package com.FinalEgg.ServiChacras.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinalEgg.ServiChacras.entidades.Pedido;
import com.FinalEgg.ServiChacras.servicios.PedidoServicio;

@RestController
@RequestMapping("/api/pedidos")

public class PedidoControlador {
    @Autowired
    private PedidoServicio pedidoService;

    @PostMapping("/crear")
    public ResponseEntity<Pedido> crearPedido(@RequestBody PageRequest pedidoRequest) {
        // Aquí manejas la lógica para obtener el cliente, proveedor, y otros datos del pedido
        // Llama al servicio para crear el pedido
        Pedido pedido = PedidoServicio.crearPedido(cliente, proveedor,  Otros parámetros );
        return ResponseEntity.ok(pedido);
    }
}  */

//METODO 1