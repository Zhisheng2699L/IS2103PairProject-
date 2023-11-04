/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author foozh
 */
@Entity
public class ItineraryEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iternaryId;
    
    @Column(nullable = false, length = 20)
    @NotNull
    @Size(min = 1, max = 20)
    private String creditCardNumber;

    @Column(nullable = false, length = 3)
    @NotNull
    @Size(min = 3, max = 3)
    private String cvv;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private UserEntity user;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "itinerary", cascade = CascadeType.PERSIST)
    private List<ReservationEntity> reservations;

    public ItineraryEntity() {
         this.reservations = new ArrayList<>();
    }

    public ItineraryEntity(String creditCardNumber, String cvv) {
        this.creditCardNumber = creditCardNumber;
        this.cvv = cvv;
    }

    public ItineraryEntity(Long iternaryId, String creditCardNumber, String cvv, UserEntity user, List<ReservationEntity> reservations) {
        this.iternaryId = iternaryId;
        this.creditCardNumber = creditCardNumber;
        this.cvv = cvv;
        this.user = user;
        this.reservations = reservations;
    }
    
    public Long getIternaryId() {
        return iternaryId;
    }

    public void setIternaryId(Long iternaryId) {
        this.iternaryId = iternaryId;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public List<ReservationEntity> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationEntity> reservations) {
        this.reservations = reservations;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iternaryId != null ? iternaryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the iternaryId fields are not set
        if (!(object instanceof ItineraryEntity)) {
            return false;
        }
        ItineraryEntity other = (ItineraryEntity) object;
        if ((this.iternaryId == null && other.iternaryId != null) || (this.iternaryId != null && !this.iternaryId.equals(other.iternaryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Iternary[ id=" + iternaryId + " ]";
    }
    
}
