package pl.edu.agh.twitter;

import java.util.Calendar;
import java.util.Date;
import java.util.SortedSet;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import twitter4j.Status;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class ApplicationView extends VerticalLayout implements View{
        
        public static final String APPLICATION_VIEW_NAME = "application";
        private VerticalLayout layout = new VerticalLayout();
        
        private FriendsTweetsPanel friendTweetsPanel;
        private NewPeoplePanel newPeoplePanel;
        private Label brakUprawnienLabel = new Label("Brak uprawnień");
        private Button wylogujButton = new Button("Wyloguj");
        private Button zalogujButton = new Button("Zaloguj");

        @Override
        public void enter(ViewChangeEvent event) {
                // TODO Auto-generated method stub
                
        }
        
        public ApplicationView() {
                layout.setMargin(true);
                layout.setSpacing(true);
                addComponent(layout);
                
                completeLayout();
                addListenerToButtons();

        }
        
        private void completeLayout() {
        	layout.removeAllComponents();
        	
        	if (SecurityUtils.getSubject().isPermitted("display_twits") 
        			|| SecurityUtils.getSubject().isPermitted("add_new_friends")) {
        		layout.addComponent(wylogujButton);
        		layout.setComponentAlignment(wylogujButton, Alignment.TOP_RIGHT);
        		
        	}
           	else {
        		layout.addComponent(zalogujButton);
        		layout.addComponent(brakUprawnienLabel);
        		layout.setComponentAlignment(zalogujButton, Alignment.TOP_RIGHT);
        	}
        	
        	if (SecurityUtils.getSubject().isPermitted("display_twits"))  {
	            friendTweetsPanel = new FriendsTweetsPanel();
	            layout.addComponent(friendTweetsPanel);
        	}
        	if (SecurityUtils.getSubject().isPermitted("add_new_friends")) {
        		newPeoplePanel = new NewPeoplePanel();
        		layout.addComponent(newPeoplePanel);
        	}
 
			
		}
        
        private void addListenerToButtons() {
        	wylogujButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					Subject currentUser = SecurityUtils.getSubject();
					currentUser.logout();
					getUI().getNavigator().navigateTo(LoginView.LOGIN_VIEW_NAME);
				}
			});
        	
        	zalogujButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					getUI().getNavigator().navigateTo(LoginView.LOGIN_VIEW_NAME);
					
				}
			});
        }

		private void showLast4HoursOfTweets() {
                SortedSet<Status> statusesSet = TwitterUtil.getProperJoinedTimeline();
                SortedSet<Status> headSet = statusesSet; // for use within while
                Status last = statusesSet.last();

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.HOUR_OF_DAY, -4);
                Date fourHoursAgo = calendar.getTime();

                while (last != null && last.getCreatedAt().after(fourHoursAgo)) {
                        String tweet = last.getUser().getName() + " ("
                                        + last.getCreatedAt().toString() + "):" + last.getText();
                        layout.addComponent(new Label(tweet));
                        headSet = headSet.headSet(last);
                        last = headSet.last();
                }
        }

}