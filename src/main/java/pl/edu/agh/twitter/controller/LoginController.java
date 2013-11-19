package pl.edu.agh.twitter.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import pl.edu.agh.twitter.view.ApplicationView;
import pl.edu.agh.twitter.view.LoginView;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;

/**
 * 
 * Controller connected with login view. Implements ClickListener. 
 * Handles log in requests.
 *
 */
public class LoginController implements ClickListener{
	
	private LoginView loginView;
	
	public LoginController(LoginView loginView) {
		this.loginView = loginView;
	}

	/**
	 * Handles button requests
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		try {
			String password = loginView.getPassword();
			String login = loginView.getLogin();
			UsernamePasswordToken token = new UsernamePasswordToken(
					login, password);
	
			final Navigator navigator = UI.getCurrent().getNavigator();
	
			Subject currentUser = SecurityUtils.getSubject();
			currentUser.login(token);
			final String location = ApplicationView.APPLICATION_VIEW_NAME;
	
			navigator.navigateTo(location);
		} catch (UnknownAccountException uae) {
			loginView.error();
		} catch (IncorrectCredentialsException ice) {
			loginView.error();
		} catch (LockedAccountException lae) {
			loginView.error();
		} catch (AuthenticationException ae) {
			loginView.error();
		}
	}

}
