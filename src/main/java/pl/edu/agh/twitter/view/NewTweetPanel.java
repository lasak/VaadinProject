package pl.edu.agh.twitter.view;

import pl.edu.agh.twitter.controller.ApplicationController;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;


public class NewTweetPanel extends Panel{
	
	private TextField textField = new TextField();
	private Button sendButton = new Button(ApplicationView.WYSLIJ_BUTTON_CAPTION);
	
	private ApplicationController controller;
	
	public NewTweetPanel(ApplicationController controller) {
		this.controller = controller;
		textField.setMaxLength(140);
		textField.setSizeFull();
		initLayouts();
		addListeners();
	}

	private void initLayouts() {
		HorizontalLayout mainLayout = new HorizontalLayout();
		mainLayout.setSizeFull();
		mainLayout.addComponent(textField);
		mainLayout.addComponent(sendButton);
		mainLayout.setComponentAlignment(sendButton, Alignment.MIDDLE_CENTER);
		
		setContent(mainLayout);
		setCaption("Tweetnij coś");
		
	}
	
	/**
	 * Return the text entered to the text field
	 * @return value of the text field
	 */
	public String getTextFieldValue() {
		return textField.getValue();
	}
	
	/**
	 * Informs user about successful sending of tweet
	 */
	public void wyslano() {
		Notification.show("Wysłano");
		textField.setValue("");
	}

	private void addListeners() {
		sendButton.addClickListener(controller);
		
	}

}
