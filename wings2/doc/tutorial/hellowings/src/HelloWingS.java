import org.wings.SForm;
import org.wings.SFrame;
import org.wings.SGridLayout;
import org.wings.SList;
import org.wings.SButton;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class HelloWingS {
    public HelloWingS() {
        SGridLayout gridLayout = new SGridLayout(1);
        SForm panel = new SForm(gridLayout);
        gridLayout.setVgap(10);

        final DefaultListModel model = new DefaultListModel();
        for ( int i = 1; i <= 10; i++ ) {
            model.addElement( new Integer(i) );
        }

        final SList list = new SList( model );
        list.setSelectionMode( SList.SINGLE_SELECTION );
        list.addListSelectionListener( new ListSelectionListener() {
            public void valueChanged( ListSelectionEvent event ) {
                System.out.println( "event value is adjusting = " + event.getValueIsAdjusting() );
                int firstIndex = event.getFirstIndex();
                System.out.println( "first index = " + firstIndex );
                int lastIndex = event.getLastIndex();
                System.out.println( "last index = " + lastIndex );
                Object selected = model.get( lastIndex );
                System.out.println( selected );
            }
        });

        panel.add( list );
        panel.add(new SButton("submit"));

        SFrame rootFrame = new SFrame();
        rootFrame.getContentPane().add(panel);
        rootFrame.setVisible(true);
    }
}


/*import org.wings.*;
import java.util.*;
import java.awt.event.*;

public class HelloWingS {
    public HelloWingS() {
        SGridLayout gridLayout = new SGridLayout(1);
        SForm panel = new SForm(gridLayout);
        SLabel titel = new SLabel("Hello World - this is wingS!");
        SButton okButton = new SButton("Guess!");
        titel.setFont(new SFont(null, SFont.BOLD, 18));
        gridLayout.setVgap(10);

        final SLabel message = new SLabel();
        final STextField textField = new STextField();
        final int randomNr = new Random().nextInt(10) + 1;

        // check our guesses and respond with according message
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Integer.toString(randomNr).equals(textField.getText()))
                    message.setText("Congratulations! You guessed my number!");
                else
                    message.setText("No - '"+textField.getText()+
                            "' is not the right number. Try again!");
            }
        });

        // arrange components using a grid layout
        panel.add(titel);
        panel.add(new SLabel("We want fun, so let's play a game!\n" +
                "Try to guess a number between 1 and 10."));
        panel.add(textField);
        panel.add(okButton);
        panel.add(message);

        SFrame rootFrame = new SFrame();
        rootFrame.getContentPane().add(panel);
        rootFrame.setVisible(true);
    }
}*/