/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.utcluj.ssatr.examen;

/**
 * 
 * @author lenovo
 */
public class Airplane {
    private int number;
    private int LandingTime;

    public Airplane(int number) {
        this.number = number;
    }
    
    public Airplane(int number, int LandingTime) {
    
        this.number = number;
        this.LandingTime = LandingTime;
    }
    
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int geLandingTime() {
        return LandingTime;
    }

    public void setLandingTime(int LandingTime) {
        this.LandingTime = LandingTime;
    }

}

