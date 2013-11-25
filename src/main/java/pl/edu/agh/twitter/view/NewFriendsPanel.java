package pl.edu.agh.twitter.view;

import java.util.List;

import pl.edu.agh.twitter.controller.ApplicationController;

import twitter4j.User;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;


public class NewFriendsPanel extends Panel {
	
	private ApplicationController controller;
	
	private TextField textField = new TextField();
	private Button searchButton = new Button(ApplicationView.SZUKAJ_BUTTON_CAPTION);
	private Button followButton = new Button(ApplicationView.OBSERWUJ_BUTTON_CAPTION);
	private Table userTable = new Table();
	private List<User> users;
	
	public NewFriendsPanel(ApplicationController controller) {
		this.controller = controller;
		initTable();
		initLayouts();
		addListeners();
	}

	private void initTable() {
		userTable = new Table();
		userTable.addContainerProperty("Nazwa", Label.class, null);
		userTable.addContainerProperty("Opis", Label.class, null);
		userTable.setSizeFull();
		userTable.setPageLength(0);
		userTable.setVisible(false);
		
		userTable.setSelectable(true);
		userTable.setMultiSelect(false);
	}

	private void initLayouts() {
		setCaption("Nowe osoby");
		
		VerticalLayout mainLayout = new VerticalLayout();
		HorizontalLayout upperLayout = new HorizontalLayout();
		
		upperLayout.addComponent(textField);
		upperLayout.addComponent(searchButton);
		upperLayout.addComponent(followButton);
		
		upperLayout.setComponentAlignment(textField, Alignment.MIDDLE_LEFT);
		upperLayout.setComponentAlignment(searchButton, Alignment.MIDDLE_LEFT);
		upperLayout.setComponentAlignment(followButton, Alignment.MIDDLE_RIGHT);
		
		mainLayout.addComponent(upperLayout);
		mainLayout.addComponent(userTable);
		
		upperLayout.setSizeFull();
		upperLayout.setSpacing(true);
		
		mainLayout.setSizeFull();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		
		setContent(mainLayout);
	}
	
	/**
	 * Return the text entered to the text field
	 * @return value of the text field
	 */
	public String getTextFieldValue() {
		return textField.getValue();
	}
	
	/**
	 * Shows found users
	 * @param foundUsers 
	 */
	public void znaleziono(List<User> foundUsers) {
		users = foundUsers;
		completeTable();
		textField.setValue("");
	}
	
	/**
	 * Shows error not chosen user notification
	 */
	public void nieWybranoOsoby() {
		Notification.show("Nie wybrano osoby", Type.ERROR_MESSAGE);
	}
	
	/**
	 * Returns chosen user
	 * @return chosen user
	 */
	public Object getWybranaOsoba() {
		return userTable.getValue();
	}
	
	/**
	 * Shows adding new friend success notification
	 * @param nazwa
	 */
	public void obserwujesz(String nazwa) {
		Notification.show("Obserwujesz: " + nazwa);
		textField.setValue("");
		userTable.setVisible(false);
		userTable.removeAllItems();
	}
	
	/**
	 * Shows adding new friend failure notification
	 */
	public void nieUdaloSie() {
		Notification.show("Nie udało się", Type.ERROR_MESSAGE);
		textField.setValue("");
	}
			
	
	private void addListeners() {
		searchButton.addClickListener(controller);
		
		followButton.addClickListener(controller);
		
	}
	
	private void completeTable() {
		userTable.setVisible(true);
		userTable.removeAllItems();
		
		for (User user : users) {
			Label label = new Label();
			label.setContentMode(ContentMode.HTML);
			label.setValue("<b>" + user.getName() + "</b>" + " @" + user.getScreenName());
			
			Label opisLabel = new Label(user.getDescription());
			opisLabel.setContentMode(ContentMode.HTML);
			
			userTable.addItem(new Object[] {label, opisLabel}, user);
		}
		
	}

}
