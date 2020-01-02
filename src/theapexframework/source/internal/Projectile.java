/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theapexframework.source.internal;

/**
 *
 * @author owenboseley
 */
public class Projectile extends Object {
    
    public Projectile(String name, Vector2D startPos, Vector2D vel) {
        super(name, startPos);
        velocity = vel;
    }
    
    
    
}
