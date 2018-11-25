package giants.redistricter.database;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Configurable;

import gerrymandering.model.State;

@Configurable
public class DatabaseServiceTest {
	DatabaseService service = new DatabaseService();
	
	@Test
	public void testGetState() {
		State s = new State();
		s.setStateId(1);
		
		State ret = service.getState(s);
		assertNotNull(ret.getStateId());
		assertNotNull(ret.getShortName());
	}
	
}
