package pl.edu.agh.twitter.view;

import java.util.List;

import pl.edu.agh.twitter.model.Model;
import twitter4j.User;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;

public class FriendsPanel extends Panel{
	
	private Table userTable = new Table();
	private List<User> users;
	
	public FriendsPanel() {
		initTable();
		initLayouts();
	}

	private void initTable() {
		userTable = new Table();
		userTable.addContainerProperty("Nazwa", Label.class, null);
		userTable.addContainerProperty("Opis", Label.class, null);
		userTable.setSizeFull();
		userTable.setPageLength(0);
		userTable.setVisible(false);
		refreshTable();
	}
	
	/**
	 * Refreshes content of friends table.
	 */
	public void refreshTable() {
		users = Model.getInstance().getFriends();
		
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
		
		int pageLength = users.size() < 10 ? 0 : 10;
		userTable.setPageLength(pageLength);
	}

	private void initLayouts() {
		setContent(userTable);
		setCaption("Obserwowane osoby");
		
	}

}
