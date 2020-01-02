/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theapexframework.source.internal;

import java.awt.Color;

/**
 *
 * @author OwenBoseley
 */
public class Mesh {
    private Color color;
    private Shape shape;
    private String filePath;
    
    public Mesh(Color color, Shape shape){
        this.color = color;
        this.shape = shape;
        filePath = "null";
    }
    public Mesh(Color color, Shape shape, String filePath){
        this.color = color;
        this.filePath = filePath;
        this.shape = shape;
    }
    
    
    public Color getColor(){return this.color;}
    public Shape getShape(){return this.shape;}
    public String getFilePath(){return this.filePath;}
    public void setColor(Color c){this.color = c;}
    
    
}
