package edu.hawhamburg.shared.datastructures.particles;

import edu.hawhamburg.shared.math.Vector;

/**
 * Created by abm510 on 10.01.2018.
 */

public class fireParticle {

    private Vector position;
    private double mass;
    private Vector velocity;
    private Vector acceleration;
    private Vector force;

    public fireParticle(Vector position, double mass, Vector velocity, Vector force) {
        this.position = position;
        this.mass = mass;
        this.velocity = velocity;
        this.force = force;
    }



    public void calcNewPosition(double delta){
        position = position.add(velocity.multiply(delta));
    }

    public void calcNewVelocity(double delta){
        velocity = velocity.add(acceleration.multiply(delta));
    }

    public void calcNewAcceleration(){
        acceleration = force.multiply((1/mass));
    }

    public void reset(){
        position = new Vector(0,0,0);
        velocity = new Vector(0,0,0);
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public Vector getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector acceleration) {
        this.acceleration = acceleration;
    }

    public Vector getForce() {
        return force;
    }

    public void setForce(Vector force) {
        this.force = force;
    }





}
