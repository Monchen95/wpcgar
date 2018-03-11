package edu.hawhamburg.shared.characters;

/**
 * Created by Devran on 10.03.2018.
 */

public class NPC {
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public void amplifyDamage(int amount){
        damage=damage*amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBuff() {
        return buff;
    }

    public void setBuff(int buff) {
        this.buff = buff;
    }

    public void afflictDamage(int amount){
        health=health-amount;
    }

    public boolean isAlive(){
        if(health>0){
            return true;
        }
        else{
            return false;
        }
    }

    public void healUp(int amount){
        health=health+amount;
    }

    private int health;
    private int damage;
    private String name;
    private int buff;

    public NPC(int health, int damage, String name){
        this.health=health;
        this.damage=damage;
        this.name=name;
    }
}
