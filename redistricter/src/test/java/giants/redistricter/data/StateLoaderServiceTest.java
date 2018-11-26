package giants.redistricter.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import giants.redistricter.database.DatabaseService;

@RunWith(MockitoJUnitRunner.class)
public class StateLoaderServiceTest {

	StateLoaderService service = new StateLoaderService();
	@Mock
	DatabaseService db;
	
	@Test
	public void testCheckStateFile() {
		service.checkForStateFile("NY");
	}
	
//	@Test
//	public void testGetState() throws Throwable {
//		gerrymandering.model.State s = new gerrymandering.model.State();
//		List<Object> l = new ArrayList<Object>();
//		Mockito.doReturn(s).when(db).getState(Mockito.anyString());
//		l.add(s);
//		service.getState("NY");
//	}
}
