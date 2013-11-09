package pl.edu.agh.twitter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import twitter4j.Status;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class FriendsTweetsPanel extends Panel {
	
	private Button refreshButton;
	private Table twitTable;
	
	private Set<Status> twitties;
	 
	public FriendsTweetsPanel() {
		refreshButton = new Button("Odśwież");
		
		twitTable = new Table();
		twitTable.addContainerProperty("Nazwa użytkownika", Label.class, null);
		twitTable.addContainerProperty("Tekst", String.class, null);
		twitTable.addContainerProperty("Data", String.class, null);
		twitTable.setSizeFull();
		
		completeTable();
		addListenerToButton();
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.addComponent(refreshButton);
		mainLayout.addComponent(twitTable);
		
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		mainLayout.setSizeFull();
		setContent(mainLayout);
		
		setCaption("Tweety");
		
	}
	
	private void completeTable() {
		
		twitties = TwitterUtil.getFriendTweets();
		
		twitTable.removeAllItems();
		
		for (Status status : twitties) {
			Label userLabel = new Label();
			userLabel.setContentMode(ContentMode.HTML);
			userLabel.setValue("<b>" + status.getUser().getName() + "</b>" + " @" + status.getUser().getScreenName());
			
			String message = status.getText();
			Date createdAt = status.getCreatedAt();
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy 'o godz.' H:mm");
			String date = sdf.format(createdAt);
			
			twitTable.addItem(new Object[] {userLabel, message, date}, status);
		}
		
	}
	
	private void addListenerToButton() {
		refreshButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				completeTable();
				
			}
		});
	}

}
