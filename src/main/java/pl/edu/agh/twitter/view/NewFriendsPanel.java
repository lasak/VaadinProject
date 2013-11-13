package pl.edu.agh.twitter.view;

import java.util.List;

import pl.edu.agh.twitter.model.Model;

import twitter4j.User;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class NewFriendsPanel extends Panel {
	
	private TextField textField = new TextField();
	private Button searchButton = new Button("Szukaj");
	private Button followButton = new Button("Obserwuj");
	private Table userTable = new Table();
	private List<User> users;
	
	public NewFriendsPanel() {
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
	
	private void addListeners() {
		searchButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				String text = textField.getValue();
				if (text == null || text.trim().isEmpty()) {
					Notification.show("Nie wpisano wartości", Type.ERROR_MESSAGE);
				}
				else {
					users = Model.getInstance().searchUsers(text.trim());
					completeTable();
					textField.setValue("");
				}
				
			}
		});
		
		followButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Object object = userTable.getValue();
				if (object == null) {
					Notification.show("Nie wybrano osoby", Type.ERROR_MESSAGE);
				}
				else {
					User user = (User) object;
					if (Model.getInstance().follow(user) != null) {
						Notification.show("Obserwujesz: " + user.getName());
					}
					else {
						Notification.show("Nie udało się", Type.ERROR_MESSAGE);
					}
					textField.setValue("");
				}
				
			}
		});
		
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
