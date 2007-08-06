package desktop;

import org.wings.SFileChooser;
import org.wings.SOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FileOpenerItem
    extends EditorItem
{


    public FileOpenerItem() {
        super();
        pref.put(TOOL, "FileOpenerTool");
    }

    public FileOpenerItem(String name) {
        super(name);
    }

    @Override
    public void activated() {
        System.out.println("NAME:" + getValue(NAME));
        System.out.println("TEXT:" + getValue(TEXT));

        if (getValue(TEXT) != null)
            return;

        final SFileChooser chooser = new SFileChooser();
        chooser.setColumns(20);

        final SOptionPane dialog = new SOptionPane();
        dialog.setEncodingType("multipart/form-data");
        dialog.showInput(getComponent(), "Choose file", chooser, "Open file");
        dialog.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt) {
                if (evt.getActionCommand() == SOptionPane.OK_ACTION) {
                    try {
                        File file = chooser.getSelectedFile();
                        putValue(NAME, chooser.getFileName());
                        putValue(TEXT, ((EditorPanel)getComponent()).openFile(file));

                    }
                    catch (Exception e) {
                        dialog.show((EditorPanel)getComponent()); // show again ..
                        // .. but first, show error-message on top ..
                        SOptionPane.showMessageDialog((EditorPanel)getComponent(),
                                                      "Error opening file",
                                                      e.getMessage());
                    }
                }

            }
        });
    }

}
