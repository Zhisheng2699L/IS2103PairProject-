/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Record;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author foozh
 */
@Remote
public interface RecordSessionBeanRemote {
    
    public Long createNewRecord(Record newRecock);
    
    public List<Record> retrieveAllRecords();
    
}
