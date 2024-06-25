package com.FinalEgg.ServiChacras.controladores;

import com.FinalEgg.ServiChacras.servicios.BarrioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistroControlador {

    @Autowired
    private BarrioServicio barrioServicio;

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("barrios", barrioServicio.obtenerListaDeBarrios());
        return "registrar";
    }
}