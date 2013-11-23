import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.apache.shiro.util.ThreadState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;

import pl.edu.agh.twitter.controller.ApplicationController;
import pl.edu.agh.twitter.model.Model;
import pl.edu.agh.twitter.view.ApplicationView;
import pl.edu.agh.twitter.view.LoginView;
import pl.edu.agh.twitter.view.NewFriendsPanel;


public class ApplicationControllerTests {
	
	private Model model;
	private ClickEvent event;
	private Button button;
	private Subject subjecttestee;
	private ThreadState threadState;
	@Mock ApplicationView applicationView = mock(ApplicationView.class);;
	@InjectMocks private ApplicationController app = new ApplicationController(applicationView);
	private Navigator navigator;
	private UI ui;
	private NewFriendsPanel newFriendsPanel;

	@Before
	public void setUp() throws Exception {
		model = mock(Model.class);
		event = mock(ClickEvent.class);
		button = mock(Button.class);
		when(event.getButton()).thenReturn(button);
		subjecttestee = mock(Subject.class);
		when(subjecttestee.isAuthenticated()).thenReturn(true);
		threadState = new SubjectThreadState(subjecttestee);
		threadState.bind();
		navigator = mock(Navigator.class);
		ui = mock(UI.class);
		when(applicationView.getUI()).thenReturn(ui);
		when(ui.getNavigator()).thenReturn(navigator);
		newFriendsPanel = mock(NewFriendsPanel.class);
		when(applicationView.getNewPeoplePanel()).thenReturn(newFriendsPanel);
	}

	@After
	public void tearDown() throws Exception {
		threadState.clear();
	}

	//applicationView.getUI().getNavigator().navigateTo
	@Test
	public void wylogujTest() {
		when(button.getCaption()).thenReturn(ApplicationView.WYLOGUJ_BUTTON_CAPTION);
		app.buttonClick(event);
		verify(subjecttestee).logout();
	}
	
	@Test
	public void wylogujWhenUnloggedTest() {
		when(subjecttestee.isAuthenticated()).thenReturn(false);
		when(button.getCaption()).thenReturn(ApplicationView.WYLOGUJ_BUTTON_CAPTION);
		app.buttonClick(event);
		verify(subjecttestee).logout();
	}
	
	@Test
	public void zalogujTest() {
		when(button.getCaption()).thenReturn(ApplicationView.ZALOGUJ_BUTTON_CAPTION);
		app.buttonClick(event);
		verify(navigator).navigateTo(LoginView.LOGIN_VIEW_NAME);
	}
	
	@Test
	public void odzwiezButtonTest() {
		when(button.getCaption()).thenReturn(ApplicationView.ODSWIEZ_BUTTON_CAPTION);
		app.buttonClick(event);
		verify(applicationView).odswiez();
	}
	
	@Test
	public void szukajButtonTest() {
		when(button.getCaption()).thenReturn(ApplicationView.SZUKAJ_BUTTON_CAPTION);
		when(newFriendsPanel.getTextFieldValue()).thenReturn("test");
		app.buttonClick(event);
		//need to mock Model.getInstance(), verify it does searchUsers("test)
	}

}
