package pl.edu.agh.twitter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.annotation.WebServlet;

import twitter4j.Status;
import twitter4j.TwitterException;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("mytheme")
@SuppressWarnings("serial")
public class MyVaadinUI extends UI
{

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class, widgetset = "pl.edu.agh.twitter.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);
        
        Button button = new Button("Show tweets from last 4 hours");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                /*layout.addComponent(new Label("Thank you for clicking"));
                try {
					List<String> statusy = MyTwitterFun.doSomething();
					for(String s : statusy) {
						layout.addComponent(new Label(s));
					}
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
            	SortedSet<Status>statusesSet = TwitterUtil.getProperJoinedTimeline();
            	SortedSet<Status>headSet = statusesSet; //for use within while
            	Status last = statusesSet.last();
            	Calendar calendar = Calendar.getInstance();
            	calendar.add(Calendar.HOUR_OF_DAY, -4);
            	Date fourHoursAgo = calendar.getTime();
            	while(last != null && last.getCreatedAt().after(fourHoursAgo)) {
            		layout.addComponent(new Label(last.getUser().getName() + " (" + last.getCreatedAt().toString() + "):" + last.getText()));
            		headSet = headSet.headSet(last);
            		last = headSet.last();
            	}
            }
        });
        layout.addComponent(button);
    }

}
