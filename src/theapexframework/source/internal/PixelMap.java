/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theapexframework.source.internal;

/**
 *
 * @author owenboseley
 */
public class PixelMap {
    
    int preferredSize;
    
    
    public PixelMap(int pixelSize){
        preferredSize = pixelSize;
    }
    
    public int getPreferredSize(){
        return preferredSize;
    }
    
    public int getPixelPosition(Vector2D position){
        
        int xPos = (int)position.x;
        
        boolean run = true;
        int i = 1;
        /*while(run){
            if(Math.abs(preferredSize*i-xPos) >= 0.5){
                
            }
        }*/
        
        return 2;
    }
}
