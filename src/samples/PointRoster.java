/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package samples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import theapexframework.source.internal.Vector2D;

/**
 *
 * @author Owner
 */
public class PointRoster {
    private final String path;
    private File file;
    private FileReader reader;
    private BufferedReader buffer;
    private FileWriter writer;
    private boolean canWrite;
    
    public PointRoster(String path, boolean canWrite){
        this.canWrite = canWrite;
        this.path = path;
        
        try{
            file = new File(path);
            reader = new FileReader(file);
            buffer = new BufferedReader(reader);
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
        
        if(canWrite){
            try{
                writer = new FileWriter(file);
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    
    public String readNextLine() throws IOException{
        return buffer.readLine();
    }
    
    public void writeLine(Vector2D pos){
        if(canWrite){
            String text = pos.x+", "+pos.y+"\n";
            try{
                writer.append(text);
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    
    public void finishWriting(){
        if(canWrite){
            try{
                writer.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
