/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Record;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author foozh
 */
@Stateless
public class RecordSessionBean implements RecordSessionBeanRemote, RecordSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystemJpa-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewRecord(Record newRecock) 
    {
        em.persist(newRecock);
        em.flush();
        
        return newRecock.getRecordId();
    }
    
    @Override
    public List<Record> retrieveAllRecords() {
        Query query = em.createQuery("SELECT r FROM Record r");
        return query.getResultList();
    }
    
    
}
