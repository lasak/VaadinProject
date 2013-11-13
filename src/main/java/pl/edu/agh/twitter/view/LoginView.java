package pl.edu.agh.twitter.view;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;


import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class LoginView extends VerticalLayout implements View{
        
        public static final String LOGIN_VIEW_NAME = "login";
        
        private TextField loginTextField = new TextField();
        private PasswordField passwordTextField = new PasswordField();
        private Label loginLabel = new Label("Login:");
        private Label passwordLabel = new Label("Hasło:");
        private Button button = new Button("Zaloguj");
        
        public LoginView() {
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
                
                button.addClickListener(new ClickListener() {
                        
                        @Override
                        public void buttonClick(ClickEvent event) {
                                try {
                                        String userName = loginTextField.getValue();
                                        String password = passwordTextField.getValue();
                                        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
                                        
                                        final Navigator navigator = UI.getCurrent().getNavigator();
                                        
                                        Subject currentUser = SecurityUtils.getSubject();
                                        currentUser.login(token);
                                        final String location = ApplicationView.APPLICATION_VIEW_NAME;
                                                
                                        navigator.navigateTo(location);
                                }
                                catch ( UnknownAccountException uae ) {
                                        Notification.show("Nie udało się zalogować. Spróbuj ponownie", Type.ERROR_MESSAGE);
                                } catch ( IncorrectCredentialsException ice ) {
                                        Notification.show("Nie udało się zalogować. Spróbuj ponownie", Type.ERROR_MESSAGE);
                                } catch ( LockedAccountException lae ) {
                                        Notification.show("Nie udało się zalogować. Spróbuj ponownie", Type.ERROR_MESSAGE);
                                } catch ( AuthenticationException ae ) {
                                        Notification.show("Nie udało się zalogować. Spróbuj ponownie", Type.ERROR_MESSAGE);
                                }
                                
                        }
                });
        }

        @Override
        public void enter(ViewChangeEvent event) {
                // TODO Auto-generated method stub
                
        }
        

}