/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author kahjy
 */
@Entity
public class PassengerEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long PassengerId;
    
    @Column(nullable = false, length = 64)
    private String firstName;
    
    @Column(nullable = false, length = 64)
    private String lastName;
    
    @Column(nullable = false, length = 64)
    private String passportNumber;    
    
    @Column(nullable = false, length = 64)
    private String seatNumber;  

    public PassengerEntity() {
    }

    public PassengerEntity(Long PassengerId, String firstName, String lastName, String passportNumber, String seatNumber) {
        this.PassengerId = PassengerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportNumber = passportNumber;
        this.seatNumber = seatNumber;
    }
    
    public PassengerEntity(String firstName, String lastName, String passportNumber, String seatNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportNumber = passportNumber;
        this.seatNumber = seatNumber;
    }

    public Long getPassengerId() {
        return PassengerId;
    }

    public void setPassengerId(Long PassengerId) {
        this.PassengerId = PassengerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    
    
    public Long getId() {
        return PassengerId;
    }

    public void setId(Long PassengerId) {
        this.PassengerId = PassengerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (PassengerId != null ? PassengerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PassengerEntity)) {
            return false;
        }
        PassengerEntity other = (PassengerEntity) object;
        if ((this.PassengerId == null && other.PassengerId != null) || (this.PassengerId != null && !this.PassengerId.equals(other.PassengerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PassengerEntity[ id=" + PassengerId + " ]";
    }
    
}
