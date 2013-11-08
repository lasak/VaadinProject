package pl.edu.agh.twitter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
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
	
	static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}



	/**
	 * getResource... to jedyny formalnie poprawny sposób pobierania plików leżących w WebInfie. Zwykły getResource nie chciał zadziałać
	 * getResourceStream zadziałał. Niestety IniSecurityManagerFactory chce jedynie skonfigurowany Ini albo ścieżkę do pliku której
	 *  formalnie nie mogę mu dać. Przy odrobinie szczęścia workaround poniżej nie jest herezją - podczas inicjalizacji tworzę 
	 * plik tymczasowy do którego przepisuję shiro i usuwam go zaraz po wykorzystaniu. Nie powinno tworzyć dziury w 
	 * bezpieczeństwie na moje oko, no i działa (choć obniża wydajność deploya).
	 */
    @Override
    protected void init(VaadinRequest request) {
    	InputStream shiroInputStream = VaadinServlet.getCurrent()
				    .getServletContext().getResourceAsStream("/WEB-INF/classes/shiro.ini");
    	File file = new File("tmp");
    	try {
			file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(convertStreamToString(shiroInputStream));
			bw.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory(file.getAbsolutePath());
		
		  org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
		  file.delete(); //important - delete AFTER factory.getInstance(), otherwise fun happens
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
