/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package samples;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import theapexframework.source.internal.Behavior;
import theapexframework.source.internal.Input;
import theapexframework.source.internal.ProjectSettings;
import theapexframework.source.internal.Shape;
import theapexframework.source.internal.Vector2D;

/**
 *
 * @author Owner
 */
public class FTCTesting extends Behavior{
    
    // Settings for simulation
    private final boolean EDIT_MODE = false;
    
    
    private final double RUN_SPEED = 1;
    private final double TURN_RADIUS = 0.5; /// Max: 1 Min: 0
    private final double ANGLE_TOLERANCE = 0.004;
    private final double DISTANCE_TOLERANCE = 30;
    private final double STRAFE_TOLERANCE = 0.5; // Max: 1 Min: 0
    
    
    
    
    

    private theapexframework.source.internal.Object obj1, img1, obj2;
    
    private List<Vector2D> points = new ArrayList<>();
    
    private boolean getAngle = true;
    
    private double oGAngle = 0;
    
    private final double MAP_RATIO = 900 / 146.4; // UNITS: Pixels Per Inch
    private final Vector2D ROBOT_SIZE = new Vector2D(18, 18); // In INCHES
    
    private int quadrant = 4;
    private boolean hasI = false;
    
    private double pointOverallDistance, pointOverallAngle, originalRot;
    private boolean runOnce = true;
    
    private int currentPointI = 0;
    
    private PointRoster pointFile;
    
    // Initialize objects/GUI and project settings here
    public FTCTesting(){
        
        pointFile = new PointRoster("src/samples/pointPos.txt", EDIT_MODE);
        
        // Settings
        ProjectSettings parameters = new ProjectSettings();
        
        parameters.height = 900;
        parameters.width = 900;
        parameters.title = "Team 6427 Autonomous Simulation";
        parameters.resizable = false;
        
        instance.setSettings(parameters);
    }
    
    // Code to run ONCE on start, allowed to initalize objects here, but not the best practice...
    public void onStart() {
        
        img1 = instance.createObject(new theapexframework.source.internal.Object("barney", Shape.IMAGE, "field3.png"));
        img1.position = new Vector2D(instance.getSettings().width/2, instance.getSettings().height/2);
        img1.scale.x = instance.getSettings().width;
        img1.scale.y = instance.getSettings().height;
        
        obj1 = instance.createObject(new theapexframework.source.internal.Object("robot", new Vector2D(450, 450)));
        obj1.scale.x = MAP_RATIO * ROBOT_SIZE.x;
        obj1.scale.y = MAP_RATIO * ROBOT_SIZE.y;
        instance.bGColor = Color.GRAY;
        
        obj1.rotZ = Math.PI / 2;
        
        if(!EDIT_MODE){
            String currentText = "", currentX = "", currentY = "";
            double x, y;
            boolean running = true;
            while(running){
                System.out.println("Running line now");
                try{
                    currentText = pointFile.readNextLine();

                    if(currentText != null){
                        for(int i = 0; i < currentText.length(); i++){
                            if(currentText.charAt(i) == ','){
                                currentX = currentText.substring(0, i);
                                currentY = currentText.substring(i+2);
                            }
                        }
                        x = Double.parseDouble(currentX);
                        y = Double.parseDouble(currentY);

                        points.add(new Vector2D(x, y));
                    } else {
                        running = false;
                    }
                } catch(IOException e){
                    running = false;
                }
            }
        }
        
        
        
        
        
        
        
    }

    // Main loop
    public void onRun() {
        Vector2D forward = obj1.getDirection();
        Vector2D right = obj1.getRight();
        
        // Manual Movement
        if(EDIT_MODE){
            double speed = 7;
            if(Input.getKey('d')){
                obj1.rotZ += 0.075;
            }
            if(Input.getKey('a')){
                obj1.rotZ -= 0.075;
            }
            if(Input.getKey('w')){
                obj1.position.x += forward.x*speed;
                obj1.position.y += forward.y*speed;
            }
            if(Input.getKey('s')){
                obj1.position.x -= forward.x*speed;
                obj1.position.y -= forward.y*speed;
            }
        }
        
        if(!EDIT_MODE && (points.size() > 0) && ((points.size() - 1) >= currentPointI)){
            System.out.println(Vector2D.distance(points.get(currentPointI), obj1.position));
            if(Vector2D.distance(points.get(currentPointI), obj1.position) > DISTANCE_TOLERANCE){
                Vector2D v = points.get(currentPointI);
                
                if(getAngle){
                    getAngle = false;
                    oGAngle = Math.atan2(v.y-obj1.position.y, v.x-obj1.position.x) * -1;
                }

                double difference = obj1.getDifferenceRotation() - oGAngle;
                
                if(!hasI && difference >= -1.35 && difference <= 1.35){
                    quadrant = 2;
                    hasI = true;
                } else if(!hasI && difference <= 4.5 && difference >= 1.8){
                    quadrant = 3;
                    hasI = true;
                } else if(!hasI && difference < 1.8 && difference > 1.35){
                    hasI = true;
                    quadrant = 0;
                } else if(!hasI && (difference < -1.35 && difference > -1*(Math.PI/2))||(difference < (3*Math.PI / 2) && difference > 4.5)){
                    hasI = true;
                    quadrant = 1;
                }
                
                if(runOnce){
                    runOnce = false;
                    pointOverallDistance = Vector2D.distance(points.get(currentPointI), obj1.position);
                    pointOverallAngle = difference;
                    originalRot = obj1.rotZ;
                }
                
                if(quadrant == 2){
                    System.out.println("Running normal");
                    double change = Vector2D.normalizeValue(difference);
                    
                    double changeFactor = RUN_SPEED;
                    double changeMove = changeFactor;
                    
                    //System.out.println("Should turn in this direction: "+change);
                    System.out.println("Angle difference: "+Math.abs(difference));
                    //System.out.println("Rotation of robo: "+obj1.getDifferenceRotation());
                    
                    double currentDistance = Vector2D.distance(points.get(currentPointI), obj1.position);
                    System.out.println("Percent finished: "+((pointOverallDistance-currentDistance)/pointOverallDistance));
                    
                    if(Math.abs(difference) > ANGLE_TOLERANCE){
                        obj1.rotZ = originalRot + (((pointOverallDistance-currentDistance)/pointOverallDistance)*(pointOverallAngle)*2);
                        changeMove = changeFactor+(changeFactor*TURN_RADIUS);
                    }

                    obj1.position.x += forward.x*changeMove;
                    obj1.position.y += forward.y*changeMove;
                }
                
                if(quadrant == 3){
                    System.out.println("Running backwards");
                    double change = Vector2D.normalizeValue(difference-Math.PI);
                    double bChange = Math.abs(difference)-Math.PI;
                    
                    //System.out.println("Should turn in this direction: "+change);
                    //System.out.println("Angle difference: "+(Math.abs(difference)-Math.PI));
                    //System.out.println("Point angle: "+oGAngle);
                    //System.out.println("Rotation of robo: "+obj1.getDifferenceRotation());
                    
                    double changeFactor = RUN_SPEED;
                    double changeMove = changeFactor;
                    
                    if(Math.abs(bChange) > ANGLE_TOLERANCE){
                        obj1.rotZ += change*changeFactor*0.06;
                        changeMove = changeFactor+(changeFactor*TURN_RADIUS);
                    }

                    obj1.position.x -= forward.x*changeMove;
                    obj1.position.y -= forward.y*changeMove;
                }
                
                if(quadrant == 0){
                    System.out.println("Running strafe RIGHT");
                    double change, bChange;
                    change = difference-(Math.PI/2);
                    
                    bChange = Math.abs(difference)-(Math.PI/2);
                    
                    double changeFactor = RUN_SPEED;
                    double changeMove = changeFactor;
                    
                    if(Math.abs(bChange) > ANGLE_TOLERANCE){
                        obj1.rotZ += change*changeFactor*0.06;
                        changeMove = changeFactor+(changeFactor*TURN_RADIUS);
                    }

                    obj1.position.x += right.x*changeMove;
                    obj1.position.y += right.y*changeMove;
                }
                if(quadrant == 1){
                    System.out.println("Running STRAFE LEFT");
                    double change = 0;
                    double bChange = 0;
                    
                    if(difference < 0){
                        change = Vector2D.normalizeValue(difference+(Math.PI/2));
                        bChange = Math.abs(difference)+(Math.PI/2);
                    } else if(difference > 0){
                        change = Vector2D.normalizeValue(difference-(3*Math.PI/2));
                        bChange = Math.abs(difference)-(3*Math.PI/2);
                    }
                    
                    double changeFactor = RUN_SPEED;
                    double changeMove = changeFactor;
                    
                    if(Math.abs(bChange) > ANGLE_TOLERANCE){
                        obj1.rotZ += change*changeFactor*0.06;
                        changeMove = changeFactor+(changeFactor*TURN_RADIUS);
                    }

                    obj1.position.x -= right.x*changeMove;
                    obj1.position.y -= right.y*changeMove;
                }
            } else {
                hasI = false;
                quadrant = 4;
                getAngle = true;
                currentPointI++;
                runOnce = true;
            }
        } else if(!EDIT_MODE && points.size() > 0){
            hasI = false;
            runOnce = true;
            quadrant = 4;
            getAngle = true;
            currentPointI++;
        }
        if(EDIT_MODE){
            if(Input.getMouseClicked()){
                theapexframework.source.internal.Object o = instance.createObject(new theapexframework.source.internal.Object("Point", Color.GREEN, new Vector2D(Input.mousePos.x, Input.mousePos.y), new Vector2D(5, 5)));
                points.add(o.position);
                Vector2D v = points.get(points.size()-1);

                //double difference;
                double oGAngle = Math.atan2(v.y-obj1.position.y, v.x-obj1.position.x) * -1;
                
                double difference = obj1.getDifferenceRotation() - oGAngle;
                
                System.out.println("Angle of point: "+oGAngle);
                
                System.out.println("Diff Rotation: "+obj1.getDifferenceRotation());
                
                System.out.println("DIFFERENCE: "+difference);
            }
            if(Input.getKeyDown('g')){
                for(Vector2D p : points){
                    pointFile.writeLine(p);
                }
                pointFile.finishWriting();
                System.out.println("Successfully read to file!");
            }
        }
    }
    
    public double decelerate(double valueToChange, double targetValue, double amount){
        double change = Vector2D.normalizeValue(targetValue-valueToChange);
        change *= amount;
        
        return valueToChange+change;
    }

    // Code to run on stopping of this behavior
    public void onStop() {
    }
    
}
