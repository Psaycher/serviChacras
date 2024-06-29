package com.FinalEgg.ServiChacras.enumeraciones;

public enum Barrio {
    BARRIO_1("Los Robles"),
    BARRIO_2("Las Acacias"),
    BARRIO_3("Los Tilos"),
    FORANEO("Foraneo");
    
    private String nombre;

    Barrio(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() { return nombre; }

    public static Barrio obtenerPorNombre(String nombre) {

        for (Barrio barrio : Barrio.values()) {
            if (barrio.getNombre().equalsIgnoreCase(nombre)) { return barrio; }
        }
        throw new IllegalArgumentException("No se encontró el barrio con nombre: " + nombre);
    }
}
