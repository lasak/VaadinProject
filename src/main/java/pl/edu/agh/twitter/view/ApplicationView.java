package pl.edu.agh.twitter.view;

import java.util.Calendar;
import java.util.Date;
import java.util.SortedSet;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import pl.edu.agh.twitter.model.Model;

import twitter4j.Status;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class ApplicationView extends VerticalLayout implements View{
        
        public static final String APPLICATION_VIEW_NAME = "application";
        private VerticalLayout layout = new VerticalLayout();
        
        private TweetsPanel friendTweetsPanel;
        private NewFriendsPanel newPeoplePanel;
        private NewTweetPanel newTweetPanel;
        private FriendsPanel friendsPanel;
        private Label brakUprawnienLabel = new Label("Brak uprawnień");
        private Button wylogujButton = new Button("Wyloguj");
        private Button zalogujButton = new Button("Zaloguj");
    	private Button refreshButton = new Button("Odśwież");

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
        	
        	HorizontalLayout upperLayout = new HorizontalLayout();
        	HorizontalLayout bottomLayout = new HorizontalLayout();
        	
        	upperLayout.setSpacing(true);
        	bottomLayout.setSpacing(true);
        	upperLayout.setSizeFull();
        	bottomLayout.setSizeFull();
        	
        	
        	if (SecurityUtils.getSubject().isPermitted("display_tweets") 
        			|| SecurityUtils.getSubject().isPermitted("display_friends")
        			|| SecurityUtils.getSubject().isPermitted("add_tweet")
        			|| SecurityUtils.getSubject().isPermitted("add_new_friends")) {
        		layout.addComponent(wylogujButton);
        		layout.setComponentAlignment(wylogujButton, Alignment.TOP_RIGHT);
        		layout.addComponent(refreshButton);
        		layout.setComponentAlignment(refreshButton, Alignment.TOP_LEFT);
        		
        	}
           	else {
        		layout.addComponent(zalogujButton);
        		layout.addComponent(brakUprawnienLabel);
        		layout.setComponentAlignment(zalogujButton, Alignment.TOP_RIGHT);
        	}
        	
        	if (SecurityUtils.getSubject().isPermitted("display_tweets"))  {
	            friendTweetsPanel = new TweetsPanel();
	            upperLayout.addComponent(friendTweetsPanel);
        	}
        	if (SecurityUtils.getSubject().isPermitted("display_friends"))  {
	            friendsPanel = new FriendsPanel();
	            upperLayout.addComponent(friendsPanel);
        	}
        	if (SecurityUtils.getSubject().isPermitted("add_tweet"))  {
	            newTweetPanel = new NewTweetPanel();
	            bottomLayout.addComponent(newTweetPanel);
        	}
        	if (SecurityUtils.getSubject().isPermitted("add_new_friends")) {
        		newPeoplePanel = new NewFriendsPanel();
        		bottomLayout.addComponent(newPeoplePanel);
        	}
        	
        	layout.addComponent(upperLayout);
        	layout.addComponent(bottomLayout);
 
			
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
        	
        	refreshButton.addClickListener(new ClickListener() {
    			
    			@Override
    			public void buttonClick(ClickEvent event) {
    				friendTweetsPanel.refreshTable();
    				friendsPanel.refreshTable();
    				
    			}
    		});
        }

		private void showLast4HoursOfTweets() {
                SortedSet<Status> statusesSet = Model.getInstance().getProperJoinedTimeline();
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