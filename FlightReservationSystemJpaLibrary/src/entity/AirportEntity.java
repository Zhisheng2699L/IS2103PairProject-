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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author kahjy
 */
@Entity
public class AirportEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long airportID;
    
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String airportName;
    
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String IATACode;
        
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String city;
    
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String state;
    
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String country;
    
    @Column(nullable = false)
    @Min(0)
    @Max(24)
    @NotNull
    private int gmt;

    public AirportEntity() {
    }

    public AirportEntity(Long airportID, String airportName, String IATACode, String city, String state, String country, int gmt) {
        this.airportID = airportID;
        this.airportName = airportName;
        this.IATACode = IATACode;
        this.city = city;
        this.state = state;
        this.country = country;
        this.gmt = gmt;
    }
    
    public AirportEntity(String airport_does_not_exist) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Long getAirportID() {
        return airportID;
    }

    public void setAirportID(Long airportID) {
        this.airportID = airportID;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getIATACode() {
        return IATACode;
    }

    public void setIATACode(String IATACode) {
        this.IATACode = IATACode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    
    public Long getId() {
        return airportID;
    }

    public void setId(Long id) {
        this.airportID = id;
    }
    
    public int getGmt() {
        return gmt;
    }

    public void setGmt(int gmt) {
        this.gmt = gmt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (airportID != null ? airportID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AirportEntity)) {
            return false;
        }
        AirportEntity other = (AirportEntity) object;
        if ((this.airportID == null && other.airportID != null) || (this.airportID != null && !this.airportID.equals(other.airportID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AirportEntity[ id=" + airportID + " ]";
    }
    
}
