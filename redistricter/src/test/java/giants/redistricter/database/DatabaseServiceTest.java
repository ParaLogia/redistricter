package giants.redistricter.database;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Configurable;

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
	public void getAllStates() {
		Set<State> s;
		s = service.getAllStates();
		assertTrue(s.size() != 0);
		
	}
	
}
