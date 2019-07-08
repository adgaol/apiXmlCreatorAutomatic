/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistdsapixmlcreator;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author adgao
 */
public class Paso {
    private Integer id;
    private String tipo;
    private String leido;
    private String pendiente;
    private String elemento;
    private String valor;    
    private String regla;
    private HashSet<Integer> relNodo;
    private ArrayList<Integer> changedNodes;
    private ArrayList<String> changes;
   

    public Paso(Integer id, String tipo, String leido, String pendiente, String elemento, String valor,HashSet<Integer> relNodo, String regla) {
        this.id = id;
        this.tipo = tipo;
        this.leido = leido;
        this.pendiente = pendiente;
        this.elemento = elemento;
        this.valor = valor;
        this.regla = regla;
        this.relNodo = relNodo;
        this.changedNodes = new ArrayList<>();
        this.changes = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLeido() {
        return leido;
    }

    public void setLeido(String leido) {
        this.leido = leido;
    }

    public String getPendiente() {
        return pendiente;
    }

    public void setPendiente(String pendiente) {
        this.pendiente = pendiente;
    }

    public String getElemento() {
        return elemento;
    }

    public void setElemento(String elemento) {
        this.elemento = elemento;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getRegla() {
        return regla;
    }

    public void setRegla(String regla) {
        this.regla = regla;
    }

    

    public HashSet<Integer> getRelNodo() {
        return relNodo;
    }

    public void setRelNodo(HashSet<Integer> relNodo) {
        this.relNodo = relNodo;
    }

    public ArrayList<Integer> getChangedNodes() {
        return changedNodes;
    }

    public void setChangedNodes(ArrayList<Integer> changedNodes) {
        this.changedNodes = changedNodes;
    }

    public ArrayList<String> getChanges() {
        return changes;
    }

    public void setChanges(ArrayList<String> changes) {
        this.changes = changes;
    }
    
}
