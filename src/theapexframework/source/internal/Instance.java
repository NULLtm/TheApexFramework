package theapexframework.source.internal;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author OwenBoseley
 */
public class Instance implements Runnable {

    private Thread thread = new Thread(this);
    
    private JFrame window = new JFrame();
    private JLabel label;
    private JButton button;
    private Canvas canvas = new Canvas();
    private JPanel panel;
    
    private int currentBehavior = 0;
    
    public int FPS_LOCK = 60;
    
    private ProjectSettings settings;
    
    public ArrayList<Behavior> b = new ArrayList<>();
    
    public Color bGColor = Color.WHITE;
    
    public ArrayList<theapexframework.source.internal.Object> registeredObj = new ArrayList<>();
    public ArrayList<java.lang.Object> registeredUI = new ArrayList<>();
    
    public boolean isRunning = false;
    
    public Instance() {
    }
    
    public void initFrame(){
        this.registeredObj = new ArrayList<>();
        
        canvas = new Canvas();
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        window.add(canvas);
        
        window.setSize(settings.width, settings.height);
        canvas.setSize(settings.width, settings.height);
        
        window.setTitle(settings.title);
        
        window.setResizable(settings.resizable);
        
        Input i = new Input();
        
        canvas.addKeyListener(i);
        canvas.addMouseListener(i);
        
        canvas.setFocusable(true);
        
        window.pack();
    }
    
    public synchronized void start(){
        isRunning = true;
        
        if(!thread.isAlive()){
            thread.run();
        }
    }
    
    public synchronized void stop() {
        b.get(currentBehavior).onStop();
        
        try{
            thread.join();
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        canvas.createBufferStrategy(2);
        
        b.get(0).instance = this;
        b.get(0).onStart();
        setUI();
        window.setVisible(true);
        
        while(isRunning){
           long sTime = System.currentTimeMillis();
           tick();
           render();
           
           // Cap Fps
           double calcTime = (System.currentTimeMillis() - sTime)/1000.0;
           double timeToSleep = (1.0/FPS_LOCK) - calcTime;
           long startTime = System.currentTimeMillis();
           while(timeToSleep > 0 && (System.currentTimeMillis()-startTime < timeToSleep)){
           }
        }
    }
    
    private void setUI(){
        Iterator i = registeredUI.iterator();
        while(i.hasNext()){
            java.lang.Object obj = i.next();
            if(obj != null && obj instanceof JLabel){
                JLabel l = (JLabel)obj;
                panel.add(l);
            } else if(obj != null && obj instanceof JButton){
                JButton l = (JButton)obj;
                panel.add(l);
            }
        }
    }
    
    private void tick(){
        //collision();
        updatePhysics();
        b.get(currentBehavior).onRun();
    }
    
    private void updatePhysics(){
        Iterator i = registeredObj.iterator();
        while(i.hasNext()){
            java.lang.Object o = i.next();
            if(o instanceof Object){
                Object obj = (Object) o;
                obj.updatePhysics();
            }
        }
    }
    
    private void collision(){
        for(int i = 0; i < registeredObj.size(); i++){
            for(int j = 0; j < registeredObj.size(); j++){
                if(!registeredObj.get(i).equals(registeredObj.get(j))){
                    if(registeredObj.get(i).mesh.getShape().equals(Shape.RECT)){
                        if(registeredObj.get(i).position.x - (registeredObj.get(i).scale.x/2) < registeredObj.get(j).position.x + (registeredObj.get(j).scale.x/2) && registeredObj.get(i).position.x + (registeredObj.get(i).scale.x/2) > registeredObj.get(j).position.x - (registeredObj.get(j).scale.x/2) && registeredObj.get(i).position.y - (registeredObj.get(i).scale.y/2) < registeredObj.get(j).position.y + (registeredObj.get(j).scale.y/2) && registeredObj.get(i).position.y + (registeredObj.get(i).scale.y/2) > registeredObj.get(j).position.y - (registeredObj.get(j).scale.y/2)){
                            registeredObj.get(i).collided = true;
                            registeredObj.get(i).collider = registeredObj.get(j);
                        } else {
                            registeredObj.get(i).collided = false;
                            registeredObj.get(i).collider = null;
                        }
                    } else {
                        double dX = registeredObj.get(i).position.x-registeredObj.get(j).position.x;
                        double dY = registeredObj.get(i).position.y-registeredObj.get(j).position.y;
                        double distance = Math.sqrt((dY*dY)+(dX*dX));
                        if(distance < (registeredObj.get(i).scale.x/2)+(registeredObj.get(j).scale.x/2)){
                            registeredObj.get(i).collided = true;
                            registeredObj.get(i).collider = registeredObj.get(j);
                        } else {
                            registeredObj.get(i).collided = false;
                            registeredObj.get(i).collider = null;
                        }
                    }
                }
            }
        }
    }
    
    private void render(){
        // Accessing graphics
        Graphics2D drawGraphics = (Graphics2D) canvas.getBufferStrategy().getDrawGraphics();
        
        // BACKGROUND
        //drawGraphics.setBackground(bGColor);
        
        drawGraphics.setColor(bGColor);
        drawGraphics.fillRect(0, 0, settings.width, settings.height);
        
        // DRAWING OBJECTS
        Iterator i = registeredObj.iterator();
        
        while(i.hasNext()){
            theapexframework.source.internal.Object o = (Object)i.next();
            
            drawGraphics.rotate(o.rotZ, o.position.x, o.position.y);
            drawGraphics.setColor(o.mesh.getColor());
            if(o.mesh.getShape() == Shape.RECT){
                drawGraphics.fillRect((int)(o.position.x-(o.scale.x/2)), (int)(o.position.y-(o.scale.y/2)), (int)o.scale.x, (int)o.scale.y);
            } else if(o.mesh.getShape() == Shape.ELLIPSE){
                drawGraphics.fillOval((int)(o.position.x-(o.scale.x/2)), (int)(o.position.y-(o.scale.y/2)), (int)o.scale.x, (int)o.scale.y);
            } else if(o.mesh.getShape() == Shape.IMAGE){
                
                File file = new File("src/images/"+o.mesh.getFilePath());
                
                Image img = null;
                
                try{
                    img = ImageIO.read(file);
                } catch(IOException e){
                    e.printStackTrace();
                }
                
                if(img != null){
                    drawGraphics.drawImage(img, (int)(o.position.x-(o.scale.x/2)), (int)(o.position.y-(o.scale.y/2)), (int)o.scale.x, (int)o.scale.y, null);
                }
            }
            
            drawGraphics.rotate(-o.rotZ, o.position.x, o.position.y);
        }
        
        // Releases system for resources used in rendering
        drawGraphics.dispose();
        
        // Shows next frame
        canvas.getBufferStrategy().show();
    }
    
    public theapexframework.source.internal.Object createObject(theapexframework.source.internal.Object source){
        registeredObj.add(source);
        return source;
    }
    
    public void destroyObject(theapexframework.source.internal.Object source){
        registeredObj.remove(source);
    }
    
    public void setSettings(ProjectSettings layout){
        this.settings = layout;
    }
    
    public JLabel createLabel(String text, Vector2D position){
        JLabel label = new JLabel(text);
        label.setLocation((int) position.x, (int) position.y);
        registeredUI.add(label);
        label.setBackground(Color.BLUE);
        return label;
        
    }
    
    public JButton createButton(String text, Vector2D position){
        JButton b = new JButton(text);
        b.setLocation((int) position.x, (int) position.y);
        registeredUI.add(b);
        return b;
    }
    
    public void changeBehavior(int behaviorIndex){
        b.get(currentBehavior).onStop();
        registeredObj.clear();
        currentBehavior = behaviorIndex;
        b.get(currentBehavior).onStart();
    }
    
    public ProjectSettings getSettings(){
        return settings;
    }
    
}
