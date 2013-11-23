import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.apache.shiro.util.ThreadState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import pl.edu.agh.twitter.controller.ApplicationController;
import pl.edu.agh.twitter.model.Model;
import pl.edu.agh.twitter.view.ApplicationView;
import pl.edu.agh.twitter.view.LoginView;
import pl.edu.agh.twitter.view.NewFriendsPanel;
import pl.edu.agh.twitter.view.NewTweetPanel;
import twitter4j.User;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Model.class)
@PowerMockIgnore("org.mockito.*")
public class ApplicationControllerTests {

	private Model model;
	private ClickEvent event;
	private Button button;
	private Subject subjecttestee;
	private ThreadState threadState;
	@Mock
	ApplicationView applicationView = mock(ApplicationView.class);;
	@InjectMocks
	private ApplicationController app = new ApplicationController(
			applicationView);
	private Navigator navigator;
	private UI ui;
	private User u;
	private NewFriendsPanel newFriendsPanel;
	private NewTweetPanel newTweetPanel;

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
		u = mock(User.class);
		when(applicationView.getUI()).thenReturn(ui);
		when(ui.getNavigator()).thenReturn(navigator);
		newFriendsPanel = mock(NewFriendsPanel.class);
		when(applicationView.getNewPeoplePanel()).thenReturn(newFriendsPanel);
		newTweetPanel = mock(NewTweetPanel.class);
		when(applicationView.getNewTweetPanel()).thenReturn(newTweetPanel);
		PowerMockito.mockStatic(Model.class);
	}

	@After
	public void tearDown() throws Exception {
		threadState.clear();
	}

	// applicationView.getUI().getNavigator().navigateTo
	@Test
	public void wylogujTest() {
		when(button.getCaption()).thenReturn(
				ApplicationView.WYLOGUJ_BUTTON_CAPTION);
		app.buttonClick(event);
		verify(subjecttestee).logout();
	}

	@Test
	public void wylogujWhenUnloggedTest() {
		when(subjecttestee.isAuthenticated()).thenReturn(false);
		when(button.getCaption()).thenReturn(
				ApplicationView.WYLOGUJ_BUTTON_CAPTION);
		app.buttonClick(event);
		verify(subjecttestee).logout();
	}

	@Test
	public void zalogujTest() {
		when(button.getCaption()).thenReturn(
				ApplicationView.ZALOGUJ_BUTTON_CAPTION);
		app.buttonClick(event);
		verify(navigator).navigateTo(LoginView.LOGIN_VIEW_NAME);
	}

	@Test
	public void odzwiezButtonTest() {
		when(button.getCaption()).thenReturn(
				ApplicationView.ODSWIEZ_BUTTON_CAPTION);
		app.buttonClick(event);
		verify(applicationView).odswiez();
	}

	@Test
	public void szukajButtonTest() {
		when(button.getCaption()).thenReturn(
				ApplicationView.SZUKAJ_BUTTON_CAPTION);
		when(newFriendsPanel.getTextFieldValue()).thenReturn("test");
		when(Model.getInstance()).thenReturn(model);
		app.buttonClick(event);
		verify(model).searchUsers("test");
	}

	@Test
	public void szukajButtonEmptyTest() {
		when(button.getCaption()).thenReturn(
				ApplicationView.SZUKAJ_BUTTON_CAPTION);
		when(newFriendsPanel.getTextFieldValue()).thenReturn("");
		app.buttonClick(event);
		verify(applicationView).nieWpisanoWartosci();
	}

	@Test
	public void obserwujButtonTest() {
		when(button.getCaption()).thenReturn(
				ApplicationView.OBSERWUJ_BUTTON_CAPTION);
		User u = mock(User.class);
		when(u.getName()).thenReturn("test");
		when(newFriendsPanel.getWybranaOsoba()).thenReturn(u);
		when(Model.getInstance()).thenReturn(model);
		when(model.follow(u)).thenReturn(u);
		app.buttonClick(event);
		verify(newFriendsPanel).obserwujesz(u.getName());
	}

	@Test
	public void obserwujButtonFollowFailTest() {
		when(button.getCaption()).thenReturn(
				ApplicationView.OBSERWUJ_BUTTON_CAPTION);
		
		when(u.getName()).thenReturn("test");
		when(newFriendsPanel.getWybranaOsoba()).thenReturn(u);
		when(Model.getInstance()).thenReturn(model);
		when(model.follow(u)).thenReturn(null);
		app.buttonClick(event);
		verify(newFriendsPanel).nieUdaloSie();
	}

	@Test
	public void obserwujButtonPersonUnchosenTest() {
		when(button.getCaption()).thenReturn(
				ApplicationView.OBSERWUJ_BUTTON_CAPTION);
		when(newFriendsPanel.getWybranaOsoba()).thenReturn(null);
		app.buttonClick(event);
		verify(newFriendsPanel).nieWybranoOsoby();
	}

	@Test
	public void wyslijButtonTest() {
		when(button.getCaption()).thenReturn(
				ApplicationView.WYSLIJ_BUTTON_CAPTION);
		when(newTweetPanel.getTextFieldValue()).thenReturn("test");
		when(Model.getInstance()).thenReturn(model);
		app.buttonClick(event);
		verify(newTweetPanel).wyslano();
		verify(model).sendATweet("test");
	}

	@Test
	public void wyslijButtonEmptyTest() {
		when(button.getCaption()).thenReturn(
				ApplicationView.WYSLIJ_BUTTON_CAPTION);
		when(newTweetPanel.getTextFieldValue()).thenReturn("");
		app.buttonClick(event);
		verify(applicationView).nieWpisanoWartosci();
	}

}
