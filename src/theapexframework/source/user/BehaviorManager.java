/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theapexframework.source.user;

import java.util.ArrayList;
import samples.FTCTesting;
import theapexframework.source.internal.Behavior;
import theapexframework.source.internal.Behavior;

/**
 *
 * @author owenboseley
 */
public class BehaviorManager {
    public static ArrayList<Behavior> registered = new ArrayList<>();
    
    public static void init(){
        // Add Your Behaviors
        // REMINDER: Behaviors at the top will go first
        registered.add(new FTCTesting());
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
}
