package ar.edu.utn.frbb.tup.model;

public class NoAlcanzaException extends Exception {
    public NoAlcanzaException() {
        super("No hay suficiente balance en la cuenta.");
    }
}