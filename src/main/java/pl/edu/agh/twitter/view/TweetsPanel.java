package pl.edu.agh.twitter.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import pl.edu.agh.twitter.model.Model;
import twitter4j.Status;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;

public class TweetsPanel extends Panel {
	
	private Table twitTable;
	private Set<Status> twitties;
	
	 
	public TweetsPanel() {
		
		twitTable = new Table();
		twitTable.addContainerProperty("Nazwa użytkownika", Label.class, null);
		twitTable.addContainerProperty("Tekst", Label.class, null);
		twitTable.addContainerProperty("Data", String.class, null);
		twitTable.setSizeFull();
		twitTable.setPageLength(10);
		
		refreshTable();

		setContent(twitTable);
		
		setCaption("Tweety");
		
	}
	
	/**
	 * Refreshes content of tweets table.
	 */
	public void refreshTable() {
		
		twitties = Model.getInstance().getTweets();
		
		twitTable.removeAllItems();
		
		for (Status status : twitties) {
			Label userLabel = new Label();
			userLabel.setContentMode(ContentMode.HTML);
			userLabel.setValue("<b>" + status.getUser().getName() + "</b>" + " @" + status.getUser().getScreenName());
			
			Label textLabel = new Label();
			textLabel.setContentMode(ContentMode.HTML);
			textLabel.setValue(status.getText());
			
			Date createdAt = status.getCreatedAt();
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy 'o godz.' H:mm");
			String date = sdf.format(createdAt);
			
			twitTable.addItem(new Object[] {userLabel, textLabel, date}, status);
		}
		
	}
	

}
