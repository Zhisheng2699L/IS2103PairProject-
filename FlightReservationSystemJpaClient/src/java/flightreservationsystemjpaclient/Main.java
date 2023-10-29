/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package flightreservationsystemjpaclient;

import ejb.session.stateless.RecordSessionBeanRemote;
import entity.Record;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author foozh
 */
public class Main {

    @EJB
    private static RecordSessionBeanRemote recordSessionBeanRemote;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        List<Record> retrieveAllRecords = recordSessionBeanRemote.retrieveAllRecords();
        
        for(Record record : retrieveAllRecords) {
            
            System.out.println(record.getRecordId() + ", " + record.getYourAss());
        }
        
        
    }
    
}
