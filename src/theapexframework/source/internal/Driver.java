package theapexframework.source.internal;

import theapexframework.source.user.BehaviorManager;

public class Driver {
    
    public static Instance engine;
    
    public static void main(String[] args){
        engine = new Instance();
        BehaviorManager.init();
        engine.b = BehaviorManager.registered;
        engine.initFrame();
        
        // Starts new thread
        engine.start();
    }
}
