/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfigurationEntity;
import entity.AircraftTypeEntity;
import entity.CabinClassEntity;
import exceptions.AircraftConfigNotFoundException;
import exceptions.AircraftTypeDoNotExistException;
import exceptions.CreateNewAircraftConfigErrorException;
import exceptions.ExistingAircraftConfigException;
import exceptions.UnknownPersistenceException;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 *
 * @author foozh
 */
@Stateless
public class AircraftConfigurationSessionBean implements AircraftConfigurationSessionBeanRemote, AircraftConfigurationSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystemJpa-ejbPU")
    private EntityManager em;
    
    @Resource
    private EJBContext eJBContext;

    @EJB
    private CabinClassSessionBeanLocal cabinClassSessionBean;

    @EJB
    private AircraftTypeSessionBeanLocal aircraftTypeSessionBean;
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public AircraftConfigurationEntity createNewAircraftConfig(AircraftConfigurationEntity aircraftConfig, long aircraftTypeID, List<CabinClassEntity> cabinClasses) throws 
            CreateNewAircraftConfigErrorException, AircraftTypeDoNotExistException, UnknownPersistenceException, ExistingAircraftConfigException {
        try {
            em.persist(aircraftConfig);

            
            AircraftTypeEntity aircraftType = aircraftTypeSessionBean.retrieveAircraftTypeById(aircraftTypeID);
            aircraftType.getAircraftConfig().add(aircraftConfig);
            aircraftConfig.setAircraftType(aircraftType);

            for (CabinClassEntity c : cabinClasses) {
                cabinClassSessionBean.createNewCabinClass(c, aircraftConfig);
            }

            int maxCapacity = calculateMaxCapacity(aircraftConfig);
            if (aircraftConfig.getAircraftType().getMaxCapacity() >= maxCapacity) {
                em.flush();
                return aircraftConfig;
            } else {
                throw new CreateNewAircraftConfigErrorException("Configuration exceeds maximum capacity of aircraft type");
            }

        } catch (CreateNewAircraftConfigErrorException | AircraftTypeDoNotExistException ex) {
            eJBContext.setRollbackOnly();
            throw new CreateNewAircraftConfigErrorException(ex.getMessage());
        } catch (PersistenceException ex) { 
            eJBContext.setRollbackOnly();
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new ExistingAircraftConfigException("Aircraft Config name already exist");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    @Override
    public List<AircraftConfigurationEntity> retrieveAllConfiguration() throws AircraftConfigNotFoundException {
        Query query = em.createQuery("SELECT a FROM AircraftConfigurationEntity a ORDER BY a.aircraftType ASC, a.name ASC ");

        List<AircraftConfigurationEntity> result = query.getResultList();
        if (result.isEmpty()) {
            throw new AircraftConfigNotFoundException("No aircraft configurations in system");
        } else {
            return result;
        }

    }
    
    @Override
    public AircraftConfigurationEntity retriveAircraftConfigByID(Long aircraftConfigID) throws AircraftConfigNotFoundException {
        AircraftConfigurationEntity config = em.find(AircraftConfigurationEntity.class, aircraftConfigID);
        if (config != null) {
            return config;
        } else {
            throw new AircraftConfigNotFoundException("Aircraft Configuration with " + aircraftConfigID + " does not exist!");
        }
    }
    
    @Override
    public AircraftConfigurationEntity retrieveAircraftConfigByName(String name) throws AircraftConfigNotFoundException {
        Query query = em.createQuery("SELECT a FROM AircraftConfigurationEntity a WHERE a.name = :name");
        query.setParameter("name", name);

        try {
            return (AircraftConfigurationEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new AircraftConfigNotFoundException("Flight does not exist in sytem!");
        }
    }
    
    private int calculateMaxCapacity(AircraftConfigurationEntity aircraftConfig) {
        return aircraftConfig.getCabin()
                .stream()
                .mapToInt(CabinClassEntity::getMaxSeatCapacity)
                .sum();
    }
}
