package theapexframework.source.internal;

// Parent class of user's custom scripts in the framework
public abstract class Behavior {
    // Variables
    public Instance instance = Driver.engine;
    
    // Window
    public int width = 100;
    public int height = 100;
    
    // Main three methods
    public abstract void onStart();
    public abstract void onRun();
    public abstract void onStop();
}
