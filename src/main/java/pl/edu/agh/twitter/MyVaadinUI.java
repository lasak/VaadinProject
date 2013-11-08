package pl.edu.agh.twitter;

import javax.servlet.annotation.WebServlet;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("mytheme")
@SuppressWarnings("serial")
public class MyVaadinUI extends UI {
	final VerticalLayout layout = new VerticalLayout();

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class, widgetset = "pl.edu.agh.twitter.AppWidgetSet")
	public static class Servlet extends VaadinServlet {
	}



    @Override
    protected void init(VaadinRequest request) {
    	
        Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("C:\\Users\\as\\Desktop\\studia\\TAI\\Projekt\\VaadinProject\\src\\main\\resources\\shiro.ini");
    	
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
    	SecurityUtils.setSecurityManager(securityManager);
    	
		final Navigator navigator = new Navigator(this, this);
		setNavigator(navigator);
		
		navigator.addView(LoginView.LOGIN_VIEW_NAME, LoginView.class);
		navigator.addView(ApplicationView.APPLICATION_VIEW_NAME, ApplicationView.class);
		
		Subject currentUser = SecurityUtils.getSubject();
        
		final String viewName = currentUser.isAuthenticated()
				? ApplicationView.APPLICATION_VIEW_NAME: LoginView.LOGIN_VIEW_NAME;

		navigator.navigateTo(viewName);
    }

}
