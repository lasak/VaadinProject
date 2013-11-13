package pl.edu.agh.twitter.view;

import org.apache.shiro.SecurityUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;

import controller.ApplicationController;

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
        
        private TweetsPanel friendTweetsPanel;
        private NewFriendsPanel newPeoplePanel;
        private NewTweetPanel newTweetPanel;
        private FriendsPanel friendsPanel;
        private Label brakUprawnienLabel = new Label("Brak uprawnień");
        private Button wylogujButton = new Button(WYLOGUJ_BUTTON_CAPTION);
        private Button zalogujButton = new Button(ZALOGUJ_BUTTON_CAPTION);
    	private Button refreshButton = new Button(ODSWIEZ_BUTTON_CAPTION);

        @Override
        public void enter(ViewChangeEvent event) {
                // TODO Auto-generated method stub
                
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
	            friendTweetsPanel = new TweetsPanel();
	            upperLayout.addComponent(friendTweetsPanel);
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
        
        
        public void odswiez() {
        	friendTweetsPanel.refreshTable();
			friendsPanel.refreshTable();
        }
        
        public void nieWpisanoWartosci() {
        	Notification.show("Nie wpisano wartości", Type.ERROR_MESSAGE);
        }


		public TweetsPanel getFriendTweetsPanel() {
			return friendTweetsPanel;
		}

		public NewFriendsPanel getNewPeoplePanel() {
			return newPeoplePanel;
		}

		public NewTweetPanel getNewTweetPanel() {
			return newTweetPanel;
		}

		public FriendsPanel getFriendsPanel() {
			return friendsPanel;
		}

		/*private void showLast4HoursOfTweets() {
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
        }*/

}