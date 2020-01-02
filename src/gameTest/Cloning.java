package gameTest;

public class Cloning {
    
    
    private long start;

    public int randomPosition(int width) {
        return (int) (Math.random() * width);
    }

    public void startTime() {
        start = System.currentTimeMillis();
    }

    public boolean timer(int timeToFind) {

        if (System.currentTimeMillis() - start >= timeToFind) {
            return true;
        } else {
            return false;
        }

    }

}