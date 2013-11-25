package pl.edu.agh.twitter.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import pl.edu.agh.twitter.model.Model;
import pl.edu.agh.twitter.view.ApplicationView;
import pl.edu.agh.twitter.view.LoginView;
import pl.edu.agh.twitter.view.NewFriendsPanel;
import pl.edu.agh.twitter.view.NewTweetPanel;
import twitter4j.User;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * 
 * Controller connected with application main view. Implements ClickListener. 
 * Handles all requests
 *
 */
public class ApplicationController implements ClickListener{
	
	private ApplicationView applicationView;
	
	public ApplicationController(ApplicationView applicationView) {
		this.applicationView = applicationView;
	}

	/**
	 * Handles button requests
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		Button button = event.getButton();
		if (button.getCaption().equals(ApplicationView.WYLOGUJ_BUTTON_CAPTION)) {
			wylogujButtonClicked();
		}
		else if (button.getCaption().equals(ApplicationView.ZALOGUJ_BUTTON_CAPTION)) {
			zalogujButtonClicked();
		}
		else if (button.getCaption().equals(ApplicationView.ODSWIEZ_BUTTON_CAPTION)) {
			odzwiezButtonClicked();
		}
		else if (button.getCaption().equals(ApplicationView.SZUKAJ_BUTTON_CAPTION)) {
			szukajButtonClicked();
		}
		else if (button.getCaption().equals(ApplicationView.OBSERWUJ_BUTTON_CAPTION)) {
			obserwujButtonClicked();
		}
		else if (button.getCaption().equals(ApplicationView.WYSLIJ_BUTTON_CAPTION)) {
			wyslijButtonClicked();
		}
		
	}

	private void wyslijButtonClicked() {
		NewTweetPanel panel = applicationView.getNewTweetPanel();
		if (panel.getTextFieldValue().trim().isEmpty()) {
			applicationView.nieWpisanoWartosci();
		}
		else {
			Model.getInstance().sendATweet(panel.getTextFieldValue().trim());
			panel.wyslano();
			applicationView.odswiez();
		}
	}

	private void obserwujButtonClicked() {
		NewFriendsPanel panel = applicationView.getNewPeoplePanel();
		Object object = panel.getWybranaOsoba();
		if (object == null) {
			panel.nieWybranoOsoby();
		}
		else {
			User user = (User) object;
			if (Model.getInstance().follow(user) != null) {
				panel.obserwujesz(user.getName());
				applicationView.odswiez();
			}
			else {
				panel.nieUdaloSie();
			}
		}
	}

	private void szukajButtonClicked() {
		NewFriendsPanel panel = applicationView.getNewPeoplePanel();
		if (panel.getTextFieldValue().trim().isEmpty()) {
			applicationView.nieWpisanoWartosci();
		}
		else {
			panel.znaleziono(Model.getInstance().searchUsers(panel.getTextFieldValue().trim()));
		}
	}

	private void odzwiezButtonClicked() {
		applicationView.odswiez();
	}

	private void zalogujButtonClicked() {
		applicationView.getUI().getNavigator().navigateTo(LoginView.LOGIN_VIEW_NAME);
	}

	private void wylogujButtonClicked() {
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout();
		zalogujButtonClicked();
	}

}
