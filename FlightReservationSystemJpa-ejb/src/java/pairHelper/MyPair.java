/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pairHelper;

import entity.FlightScheduleEntity;

/**
 *
 * @author Mitsuki
 */
public class MyPair {
    
    private FlightScheduleEntity fs1;
    private FlightScheduleEntity fs2;

    public MyPair() {
    }

    public MyPair(FlightScheduleEntity fs1, FlightScheduleEntity fs2) {
        this.fs1 = fs1;
        this.fs2 = fs2;
    }

    public FlightScheduleEntity getFs1() {
        return fs1;
    }

    public void setFs1(FlightScheduleEntity fs1) {
        this.fs1 = fs1;
    }

    public FlightScheduleEntity getFs2() {
        return fs2;
    }

    public void setFs2(FlightScheduleEntity fs2) {
        this.fs2 = fs2;
    } 
}
