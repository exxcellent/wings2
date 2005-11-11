import org.wings.*;
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

        final SLabel resultLabel = new SLabel();
        final STextField numberField = new STextField();
        final int aRandomNumber = new Random().nextInt(11);

        // check our guesses and respond with according message
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Integer.toString(aRandomNumber).equals(numberField.getText()))
                    resultLabel.setText("Congratulations! You guessed my number");
                else
                    resultLabel.setText("No - '"+numberField.getText()+
                            "' is not the right number. Try again!");
            }
        });

        // arrange components using a grid layout
        panel.add(titel);
        panel.add(new SLabel("We want fun, so let's play us a game!\n" +
                "Try to guess a number between 1 and 10."));
        panel.add(numberField);
        panel.add(okButton);
        panel.add(resultLabel);

        SFrame rootFrame = new SFrame();
        rootFrame.getContentPane().add(panel);
        rootFrame.setVisible(true);
    }
}