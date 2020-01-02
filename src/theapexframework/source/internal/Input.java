/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theapexframework.source.internal;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author OwenBoseley
 */
public class Input implements KeyListener, MouseListener {
    
    public static List<KeyEvent> key = new ArrayList<>(), keyDown = new ArrayList<>();
    public static Vector2D mousePos = new Vector2D(0, 0);
    
    private static boolean mouseIsClicked = false, mouseIsDown = false;
    
    public static boolean getKey(char c){
        for(KeyEvent i : key){
            if(i != null && i.getKeyChar() == c){
                return true;
            }
        }
        return false;
    }
    
    public static boolean getMouseClicked(){
        boolean x = mouseIsClicked;
        mouseIsClicked = false;
        return x;
    }
    
    public static boolean getMouseDown(){
        return mouseIsDown;
    }
    
    public static boolean getKeyDown(char c){
        for(KeyEvent i : keyDown){
            if(i != null && i.getKeyChar() == c){
                keyDown.remove(i);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        boolean isSame = false;
        for(KeyEvent i : key){
            if(i.getKeyChar() == e.getKeyChar()){
                isSame = true;
            }
        }
        if(!isSame)
            key.add(e);
        isSame = false;
        
        for(KeyEvent i : keyDown){
            if(i.getKeyChar() == e.getKeyChar()){
                isSame = true;
            }
        }
        if(!isSame)
            keyDown.add(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        for(KeyEvent i : key){
            if(i.getKeyChar() == e.getKeyChar()){
                key.remove(i);
                break;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mousePos.x = e.getX();
        mousePos.y = e.getY();
        mouseIsClicked = true;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseIsDown = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseIsDown = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
}
