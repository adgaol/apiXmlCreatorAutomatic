/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistdsapixmlcreator;

/**
 *
 * @author adgao
 */
public class Node {
    private Integer id;
    private String element;
    private Boolean terminal;
    private Integer nivel;
    private Node fatherNode;
    private Boolean haveBrother;
    public Node(Integer id, String element, Boolean terminal, Integer nivel,Boolean haveBrother) {
        this.id = id;
        this.element = element;
        this.terminal = terminal;
        this.nivel = nivel;
        this.haveBrother=haveBrother;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public Boolean getTerminal() {
        return terminal;
    }

    public void setTerminal(Boolean terminal) {
        this.terminal = terminal;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public Node getFatherNode() {
        return fatherNode;
    }

    public void setFatherNode(Node fahterNode) {
        this.fatherNode = fahterNode;
    }

    public Boolean getHaveBrother() {
        return haveBrother;
    }
    
    
    
}
