package pl.edu.agh.twitter.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import controller.LoginController;

public class LoginView extends VerticalLayout implements View {

	public static final String LOGIN_VIEW_NAME = "login";

	private TextField loginTextField = new TextField();
	private PasswordField passwordTextField = new PasswordField();
	private Label loginLabel = new Label("Login:");
	private Label passwordLabel = new Label("Hasło:");
	private Button button = new Button("Zaloguj");
	
	private LoginController controller;

	public LoginView() {
		
		controller = new LoginController(this);
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.addComponent(loginLabel);
		mainLayout.addComponent(loginTextField);
		mainLayout.addComponent(passwordLabel);
		mainLayout.addComponent(passwordTextField);
		mainLayout.addComponent(button);

		loginTextField.focus();

		mainLayout.setSizeFull();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		addComponent(mainLayout);
		

		button.addClickListener(controller);
	}
	
	public String getLogin() {
		return loginTextField.getValue();
	}
	
	public String getPassword () {
		return passwordTextField.getValue();
	}
	
	public void error() {
		Notification.show(
				"Nie udało się zalogować. Spróbuj ponownie",
				Type.ERROR_MESSAGE);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}