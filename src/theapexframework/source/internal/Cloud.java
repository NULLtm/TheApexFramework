/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theapexframework.source.internal;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author owenboseley
 */
public class Cloud {
    
    
    
    public static void createHighScore(int score){
        File file = new File("src/highscore.txt");
        
        try{
            FileWriter write = new FileWriter(file);
            
            write.write(score+"");
            
            write.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public static int getHighScore(){
        File file = new File("src/highscore.txt");
        
        String rawText = "";
        
        try{
            FileReader reader = new FileReader(file);
            
            int state = 0;
            
            while(state != -1){
                state = reader.read();
                
                if(state != -1){
                    rawText = rawText.concat(((char) state)+"");
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        
        return Integer.parseInt(rawText);
        
    }
}
