package pl.edu.agh.twitter.view;

import org.apache.shiro.SecurityUtils;

import pl.edu.agh.twitter.controller.ApplicationController;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * Main application view
 *
 */
public class ApplicationView extends VerticalLayout implements View{
	
	public static final String ODSWIEZ_BUTTON_CAPTION = "Odśwież";
	public static final String ZALOGUJ_BUTTON_CAPTION = "Zaloguj";
	public static final String WYLOGUJ_BUTTON_CAPTION = "Wyloguj";
	public static final String SZUKAJ_BUTTON_CAPTION = "Szukaj";
	public static final String OBSERWUJ_BUTTON_CAPTION = "Obserwuj";
	public static final String WYSLIJ_BUTTON_CAPTION = "Wyślij";
	
	private ApplicationController controller;
        
        public static final String APPLICATION_VIEW_NAME = "application";
        private VerticalLayout layout = new VerticalLayout();
        
        private TweetsPanel tweetsPanel;
        private NewFriendsPanel newPeoplePanel;
        private NewTweetPanel newTweetPanel;
        private FriendsPanel friendsPanel;
        private Label brakUprawnienLabel = new Label("Brak uprawnień");
        private Button wylogujButton = new Button(WYLOGUJ_BUTTON_CAPTION);
        private Button zalogujButton = new Button(ZALOGUJ_BUTTON_CAPTION);
    	private Button refreshButton = new Button(ODSWIEZ_BUTTON_CAPTION);

        @Override
        public void enter(ViewChangeEvent event) {
                
        }
        
        public ApplicationView() {
        	controller = new ApplicationController(this);
                layout.setMargin(true);
                layout.setSpacing(true);
                addComponent(layout);
                
                completeLayout();
                addListenerToButtons();

        }
        
        private void addListenerToButtons() {
			wylogujButton.addClickListener(controller);
			zalogujButton.addClickListener(controller);
			refreshButton.addClickListener(controller);
			
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
	            tweetsPanel = new TweetsPanel();
	            upperLayout.addComponent(tweetsPanel);
        	}
        	if (SecurityUtils.getSubject().isPermitted("display_friends"))  {
	            friendsPanel = new FriendsPanel();
	            upperLayout.addComponent(friendsPanel);
        	}
        	if (SecurityUtils.getSubject().isPermitted("add_tweet"))  {
	            newTweetPanel = new NewTweetPanel(controller);
	            bottomLayout.addComponent(newTweetPanel);
        	}
        	if (SecurityUtils.getSubject().isPermitted("add_new_friends")) {
        		newPeoplePanel = new NewFriendsPanel(controller);
        		bottomLayout.addComponent(newPeoplePanel);
        	}
        	
        	layout.addComponent(upperLayout);
        	layout.addComponent(bottomLayout);
 
			
		}
        
        /**
         * Refreshes all components.
         */
        public void odswiez() {
        	tweetsPanel.refreshTable();
			friendsPanel.refreshTable();
        }
        
        /**
         * Shows empty value error notification
         */
        public void nieWpisanoWartosci() {
        	Notification.show("Nie wpisano wartości", Type.ERROR_MESSAGE);
        }

        /**
         * Returns panel with time line
         * @return tweets panel
         */
		public TweetsPanel getTweetsPanel() {
			return tweetsPanel;
		}

        /**
         * Returns panel to add new friends
         * @return new friends panel
         */
		public NewFriendsPanel getNewPeoplePanel() {
			return newPeoplePanel;
		}

        /**
         * Returns panel to add new tweet
         * @return new tweet panel
         */
		public NewTweetPanel getNewTweetPanel() {
			return newTweetPanel;
		}

        /**
         * Returns panel with friends
         * @return friends panel
         */
		public FriendsPanel getFriendsPanel() {
			return friendsPanel;
		}


}