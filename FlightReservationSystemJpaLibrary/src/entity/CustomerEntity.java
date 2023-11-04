/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author kahjy
 */

@Entity
public class CustomerEntity extends UserEntity implements Serializable {

    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String firstName;
    
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String lastName;
    
    @Column(nullable = false, unique = true, length = 64)
    @Size(min = 1, max = 64)
    @NotNull
    private String identificationNumber;
    
    @Column(nullable = false, unique = true, length = 32)
    @Size(min = 1, max = 32)
    @NotNull
    private String contactNumber;
    
    @Column(nullable = false, length = 64)
    @Size(min = 1, max = 64)
    @NotNull
    private String address;
    
    @Column(nullable = false, length = 64)
    @Size(min = 1, max = 64)
    @NotNull
    private String postalCode;

    public CustomerEntity() {
        super();
    }

    public CustomerEntity(String firstName, String lastName, String identificationNumber, String contactNumber, String address, String postalCode, String username, String password) {
        super(username, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.identificationNumber = identificationNumber;
        this.contactNumber = contactNumber;
        this.address = address;
        this.postalCode = postalCode;
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

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the userId fields are not set
        if (!(object instanceof CustomerEntity)) {
            return false;
        }
        CustomerEntity other = (CustomerEntity) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CustomerEntity[ id=" + userId + " ]";
    }
    
}