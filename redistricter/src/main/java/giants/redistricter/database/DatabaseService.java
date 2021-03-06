package giants.redistricter.database;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.stereotype.Service;

import giants.redistricter.data.Demographics;
import giants.redistricter.data.District;
import giants.redistricter.data.Precinct;
import giants.redistricter.data.State;
import giants.redistricter.data.Vote;


@Service
public class DatabaseService {
    
    private EntityManagerFactory factory = EntityManagerSingleton.getFactory();
    private EntityManager em = factory.createEntityManager();
    
    public State getStateById(Integer id){
        return em.find(State.class, id);   
    }
    
    public State getStateByShortName(String shortName) {
        return em.createQuery("SELECT state FROM State state where state.shortName = :value1", State.class).setParameter("value1", shortName).getSingleResult();
    }
    
    public State getStateByFullName(String fullName) {
        return em.createQuery("SELECT state FROM State state where state.name = :value1", State.class).setParameter("value1", fullName).getSingleResult();
    }
    
    public List<State> getAllStates(){
        return em.createQuery("SELECT state FROM State", State.class).getResultList();      
    } 
    
    public List<District> getDistrictsByStateId(Integer stateId){
        return em.createQuery("SELECT district FROM District district where district.stateId = :value1", District.class).setParameter("value1", stateId).getResultList();
    }
    
    public List<Precinct> getPrecinctsByDistrictsId(Integer precinctId){
        return em.createQuery("SELECT precinct FROM Precinct precinct where precinct.districtId = :value1", Precinct.class).setParameter("value1", precinctId).getResultList();
    }
    
    public List<Demographics> getDemographicsByPrecinctId(Integer precinctId){
        return em.createQuery("SELECT demographic FROM Demographics demographic where demographic.precinctId = :value1", Demographics.class).setParameter("value1", precinctId).getResultList();
    }
    
    public List<Vote> getVotesByPrecinctId(Integer precinctId){
        return em.createQuery("SELECT votes FROM Vote votes where votes.precinctId = :value1", Vote.class).setParameter("value1", precinctId).getResultList();
    }
		
}