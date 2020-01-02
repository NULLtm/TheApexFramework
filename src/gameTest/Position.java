/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameTest;

public class Position
{
  private int x1;
  private int y1;
  private double maxDistance;
  private double currentDistance = 0;
  
  Position(int x1, int y1, double maxDistance)
  {
    this.maxDistance = maxDistance;
    this.x1 = x1;
    this.y1 = y1;
  }
 
  public boolean compareDistance(int x2, int y2)
  {
    double distance = Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
    currentDistance = distance;
    if (maxDistance >= distance)
      return true;
    else
      return false;
  }
  
  public double getDistance(){
      return currentDistance;
  }
}