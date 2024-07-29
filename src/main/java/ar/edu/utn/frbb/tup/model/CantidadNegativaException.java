package ar.edu.utn.frbb.tup.model;

public class CantidadNegativaException extends Exception {
    public CantidadNegativaException() {
        super("La cantidad no puede ser negativa.");
    }
}