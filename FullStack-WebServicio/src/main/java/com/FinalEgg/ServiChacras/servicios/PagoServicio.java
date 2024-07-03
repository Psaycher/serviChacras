package com.FinalEgg.ServiChacras.servicios;

import com.FinalEgg.ServiChacras.entidades.*;
import com.FinalEgg.ServiChacras.enumeraciones.Estado;
import com.FinalEgg.ServiChacras.excepciones.MiExcepcion;
import com.FinalEgg.ServiChacras.repositorios.ClienteRepositorio;
import com.FinalEgg.ServiChacras.repositorios.PagoRepositorio;
import com.FinalEgg.ServiChacras.repositorios.ProveedorRepositorio;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PagoServicio {
    @Autowired
    private PagoRepositorio pagoRepositorio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private ProveedorRepositorio proveedorRepositorio;

    @Autowired
    ImagenServicio imagenServicio;

    @Transactional
    public Pago crearPago(String idCliente, String idProveedor) throws MiExcepcion {
        Pago pago = new Pago();

        Optional<Cliente> opcionalCliente = clienteRepositorio.findById(idCliente);
        Cliente cliente = new Cliente();

        if (opcionalCliente.isPresent()) { cliente = opcionalCliente.get(); }
        pago.setCliente(cliente);

        Optional<Proveedor> opcionalProveedor = proveedorRepositorio.findById(idProveedor);
        Proveedor proveedor = new Proveedor();

        if (opcionalProveedor.isPresent()) { proveedor = opcionalProveedor.get(); }
        pago.setProveedor(proveedor);
        pago.setValor(0);
        pago.setEstado(Estado.PENDIENTE);

        pagoRepositorio.save(pago);
        return pago;
    }

    @Transactional(readOnly = true)
    public List<Pago> listarPagos() { return pagoRepositorio.findAll(); }

    @Transactional
    public void actualizar(String id, String estadoString, Integer valor) throws MiExcepcion {
        Optional<Pago> optionalPago = pagoRepositorio.findById(id);

        optionalPago.ifPresent(pago -> {
            Estado estado = Estado.valueOf(estadoString.toUpperCase());
            pago.setEstado(estado);
            pago.setValor(valor);
            pagoRepositorio.save(pago);
        });
    }

    @Transactional
    public void deletearPago(String id) { pagoRepositorio.deleteById(id); }

    @Transactional(readOnly = true)
    public Pago getOne(String id) { return pagoRepositorio.getOne(id); }

    @Transactional(readOnly = true)
    public String idPorPedido(String idPedido) { return pagoRepositorio.idPorPedido(idPedido); }

    @Transactional(readOnly = true)
    public Pago getPagoPorPedido(String idPedido) { return pagoRepositorio.getPagoPorPedido(idPedido); }

    @Transactional(readOnly = true)
    public List<Pago> getPagoPorClientes(String idCliente) { return pagoRepositorio.getPagoPorClientes(idCliente); }

    @Transactional(readOnly = true)
    public List<Pago> getPagoPorProveedores(String idProveedor) { return pagoRepositorio.getPagoPorProveedores(idProveedor); }

    @Transactional(readOnly = true)
    public List<Pago> getPagosPendiente() { return pagoRepositorio.getPagosPendiente(); }

    @Transactional(readOnly = true)
    public List<Pago> getPagosPendientePorCliente(String idCliente) { return pagoRepositorio.getPagosPendientePorCliente(idCliente); }

    @Transactional(readOnly = true)
    public List<Pago> getPagosPendientePorProveedor(String idProveedor) { return pagoRepositorio.getPagosPendientePorProveedor(idProveedor); }
}