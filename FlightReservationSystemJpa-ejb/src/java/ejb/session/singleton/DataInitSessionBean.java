/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.RecordSessionBeanLocal;
import entity.Record;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author foozh
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean implements DataInitSessionBeanRemote, DataInitSessionBeanLocal {

    @EJB
    private RecordSessionBeanLocal recordSessionBeanLocal;

    @PersistenceContext(unitName = "FlightReservationSystemJpa-ejbPU")
    private EntityManager em;

    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PostConstruct
    public void postConstruct(){
        if(em.find(Record.class , 1l) == null) {
        
            recordSessionBeanLocal.createNewRecord(new Record("BIG ASSS"));
            recordSessionBeanLocal.createNewRecord(new Record("BIG BOOBS"));
            recordSessionBeanLocal.createNewRecord(new Record("GOD SAVE ME"));
        }
    
    }
}
