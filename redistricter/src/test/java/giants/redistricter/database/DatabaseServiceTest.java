package giants.redistricter.database;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Configurable;

import gerrymandering.model.District;
import gerrymandering.model.Precinct;
import gerrymandering.model.State;

@Configurable
public class DatabaseServiceTest {
	DatabaseService service = new DatabaseService();
	
	@Test
	public void testGetState() {
		State ret = service.getState(1);
		
		assertNotNull(ret.getStateId());
		assertNotNull(ret.getShortName());
	}
	
	@Test
	public void testGetAllStates() {
		Collection<State> s;
		s = service.getAllStates();
		assertTrue(s.size() != 0);
	}
	
	@Test
	public void testGetAllDistrictsForState() {
		Collection<District> s;
		s = service.getAllDistrictsForState(1);
		assertTrue(s.size() != 0);
	}
	
	@Test
	public void testGetAllPrecinctsForState() {
		Set<Precinct> s;
		s = service.getAllPrecinctsForState(1);
		assertTrue(s.size() != 0);
	}
	
	@Test
	public void testGetAllPrecinctsForDistrict() {
		Set<Precinct> s;
		s = service.getAllPrecinctsForState(1);
		assertTrue(s.size() != 0);
	}
	
	
	
}
