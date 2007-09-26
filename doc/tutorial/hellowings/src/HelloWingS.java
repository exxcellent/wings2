import java.awt.*;
import java.awt.event.*;
import java.util.*;

import org.wings.*;

public class HelloWingS {
    public HelloWingS() {
        SFrame rootFrame = new SFrame();
        SPanel panel = new SPanel(new SGridLayout(5, 1, 10, 5));
        SLabel title = new SLabel("Hello wingS!\n");
        SButton doGuess = new SButton("Guess!");
        final SLabel message = new SLabel();
        final STextField textField = new STextField();

        // arrange components using a grid layout
        panel.add(title);
        panel.add(new SLabel("We want fun, so let's play a game!\n" +
                "Try to guess a number between 1 and 10."));
        panel.add(message);
        panel.add(textField);
        panel.add(doGuess);
        rootFrame.getContentPane().add(panel);

        // Some formatting
        title.setFont(new SFont("sans-serif", SFont.BOLD, 18));
        message.setForeground(Color.RED);

        // The game logic as simple Swing-Actionlistener.
        final int randomNr = new Random().nextInt(10) + 1;
        doGuess.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Integer.toString(randomNr).equals(textField.getText()))
                    message.setText("Congratulations! You guessed my number!");
                else
                    message.setText("No - '"+textField.getText()+
                            "' is not the right number. Try again!");
            }
        });

        rootFrame.setVisible(true);
    }
}