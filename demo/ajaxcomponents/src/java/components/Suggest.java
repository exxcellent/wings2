/*
 * Autocompletion.java
 *
 * Created on 28. August 2006, 14:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package components;

import components.CountriesOfTheWorld;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.wings.SBorderLayout;
import org.wings.SButton;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SDimension;
import org.wings.SFont;
import org.wings.SFrame;
import org.wings.SGridBagLayout;
import org.wings.SGridLayout;
import org.wings.SLabel;
import org.wings.SPanel;
import org.wings.STextComponent;
import org.wings.STextField;
import org.wings.border.SEmptyBorder;
import org.wings.header.Link;
import org.wings.resource.DefaultURLResource;
import org.wings.template.propertymanagers.STextFieldPropertyManager;
import org.wingx.XSuggest;

/**
 * suggest example
 * @author Christian Schyma
 */
public class Suggest {
    
    private XSuggest birthCountryField = null;
    private XSuggest currentCountryField = null;
    private SFrame rootFrame = null;
    
    public Suggest() {
        
        rootFrame = new SFrame("demo");
                                
        rootFrame.getContentPane().add(createHeader(), SBorderLayout.NORTH);
        rootFrame.getContentPane().add(createXSuggestPanel(), SBorderLayout.CENTER);        
        
        rootFrame.addHeader(new Link("stylesheet", null, "text/css", null, new DefaultURLResource("../css/custom.css")));
        
        rootFrame.show();
    }
    
    private SPanel createXSuggestPanel() {
        SGridLayout gridLayout = new SGridLayout(2);
        gridLayout.setHgap(10);
        gridLayout.setVgap(4);
        SPanel panel = new SPanel(gridLayout);
        
        panel.add(new SLabel("Surname:"));
        panel.add(new STextField());
        
        panel.add(new SLabel("Name:"));
        panel.add(new STextField());
        
        panel.add(new SLabel("Birth Country:"));
        birthCountryField = new XSuggest(new CountriesOfTheWorld());
        panel.add(birthCountryField);
        
        panel.add(new SLabel("Current Country:"));
        currentCountryField = new XSuggest(new CountriesOfTheWorld());
        panel.add(currentCountryField);
        
        panel.add(new SLabel("State:"));
        XSuggest stateSuggestionField = new XSuggest(new StatesOfGermany());
        panel.add(stateSuggestionField);
        
        panel.add(new SLabel("City:"));
        panel.add(new STextField());
        
        // is there a dapper way to set a default width?
        for (int i = 0; i < panel.getComponentCount(); i++) {
            if (panel.getComponent(i).getClass() == STextField.class || panel.getComponent(i).getClass() == XSuggest.class) {
                ((STextField)panel.getComponent(i)).setColumns(20);
            }
        }
        
        SButton button = new SButton("set birth country to 'Germany'");
        panel.add(button);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                birthCountryField.setText("Germany");
            }
        });
        
        SButton remButton = new SButton("remove text field current country");
        panel.add(remButton);
        remButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rootFrame.remove(currentCountryField);
            }
        });
        
        return panel;
    }
    
    private SPanel createHeader() {
        SPanel header = new SPanel(new SGridLayout());
        header.setBackground(new Color(60, 80, 100));
        header.setBorder(new SEmptyBorder(10, 15, 10, 0));
        header.setPreferredSize(SDimension.FULLWIDTH);
        header.setHorizontalAlignment(SConstants.LEFT);
        SLabel title = new SLabel("demonstration of the component XSuggest");
        title.setStyle(title.getStyle() + " headerTitle");
        title.setFont(new SFont(SFont.BOLD));        
        title.setVerticalAlignment(SConstants.CENTER);
        title.setHorizontalAlignment(SConstants.LEFT);
        header.add(title);
        
        return header;
    }
    
}
