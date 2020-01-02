package theapexframework.source.internal;

import java.awt.Color;

public class Object implements ICollider, IPhysics {
    public boolean hasGravity, collided = false;
    public String name;
    public Mesh mesh;
    public Object collider;
            
    public double gravityScale = 0;
    public Vector2D velocity = new Vector2D(0, 0);
    public Vector2D acceleration = new Vector2D(0, 0);
    
    public Vector2D position = new Vector2D(0, 0);
    public Vector2D scale = new Vector2D(50, 50);
    public double rotZ = 0;
    
    public Object(String name, double rot, Vector2D pos){
        this.name = name;
        rotZ = rot;
        mesh = new Mesh(Color.RED, Shape.RECT);
        position = pos;
    }
    
    public Object(String name, Vector2D pos){
        this.name = name;
        rotZ = 0;
        mesh = new Mesh(Color.RED, Shape.RECT);
        position = pos;
    }
    
    public Object(String name, Color color){
        this.name = name;
        mesh = new Mesh(color, Shape.RECT);
    }
    
    public Object(String name, Color color, Shape shape){
        this.name = name;
        mesh = new Mesh(color, shape);
    }
    
    public Object(String name, Shape shape, String filePath){
        this.name = name;
        mesh = new Mesh(Color.BLACK, shape, filePath);
    }
    
    public Object(String name, Color color, Vector2D pos, Vector2D scale){
        this.name = name;
        position = pos;
        this.scale = scale;
        mesh = new Mesh(color, Shape.RECT);
    }

    @Override
    public boolean isCollided() {
        return collided;
    }

    @Override
    public Object getCollider() {
        return collider;
    }

    @Override
    public void updatePhysics() {
        acceleration.y = gravityScale/1000;
        velocity.x += acceleration.x;
        velocity.y += acceleration.y;
        position.x += velocity.x;
        position.y += velocity.y;
    }
    
    public Vector2D getDirection(){
        
        double trueRot = getTrueRotation();
        
        double ratio = Math.tan(trueRot);
        
        double xP = Math.sqrt(1+Math.pow(ratio, 2));
        xP = 1 / xP;
        
        double yP = Math.sqrt(1+Math.pow(ratio, 2));
        yP = ratio / yP;
        
        if(trueRot >= Math.PI/2 && trueRot <= Math.PI){
            yP = Math.abs(yP);
        }
        if(trueRot >= Math.PI && trueRot <= (Math.PI * 3) / 2){
            yP *= -1;
        }
        if(trueRot >= (Math.PI * 3) / 2 && trueRot <= Math.PI * 2){
            xP *= -1;
        }
        if(trueRot >= 0 && trueRot <= Math.PI / 2){
            xP *= -1;
        }
        
        Vector2D forward = new Vector2D(xP, yP);
        
        return forward;
    }
    
    public void rotate(double amount){
        rotZ += amount;
    }
    
    
    public double getDifferenceRotation(){
        double newRot = Math.PI - rotZ;
        
        return newRot;
    }
    
    public double getTrueRotation(){
        if(rotZ > 2*Math.PI){
            rotZ -= 2*Math.PI;
        }
        if(rotZ < 0){
            rotZ += 2*Math.PI;
        }
        
        double n = 2*Math.PI - rotZ;
        
        return n;
    }
    
    public Vector2D getRight(){
        Vector2D forward = getDirection();
        
        double x = 1 / (1+(Math.pow(forward.x, 2) / Math.pow(forward.y, 2)));
        double y = x;
        x = Math.sqrt(x);
        y = 1-y;
        y = Math.sqrt(y);
        
        if(forward.y > 0){
            x *= -1;
        }
        if(forward.x < 0){
            y *= -1;
        }
        
        Vector2D d = (new Vector2D(x, y)).normalized();
        
        return d;
    }
    
    
}
