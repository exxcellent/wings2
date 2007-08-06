package desktop;

import org.wings.SComponent;
import org.wings.SURLIcon;
import org.wings.event.SDocumentEvent;
import org.wings.event.SDocumentListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class EditorItem
    extends AbstractDesktopItem
{

    protected EditorPanel editorPane;

    public EditorItem() {
        super();

        pref.put(TOOL, "EditorTool");
        addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals(TEXT))
                    ((EditorPanel)getComponent()).setText((String)e.getNewValue());


            }
        });

        putValue(NAME, "Editor [" + ((EditorPanel)getComponent()).getEditorNr() + "]");
        putValue(ICON, new SURLIcon("../icons/penguin.png"));
    }

    public EditorItem(String name) {
        super(name);
        addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals(TEXT))
                    ((EditorPanel)getComponent()).setText((String)e.getNewValue());


            }
        });
    }

    public SComponent getComponent() {
        if (editorPane == null) {
            editorPane = new EditorPanel();

            editorPane.getTextArea().addDocumentListener(new SDocumentListener()
            {

                public void changedUpdate(SDocumentEvent e) {
                    putValue(TEXT, editorPane.getText());

                }


                public void insertUpdate(SDocumentEvent e) {
                    putValue(TEXT, editorPane.getText());

                }


                public void removeUpdate(SDocumentEvent e) {
                    putValue(TEXT, editorPane.getText());

                }

            });

        }


        return editorPane;
    }

    public void activated() {
    }

    public void fixContent() {
        System.out.println(editorPane.getText());
        putValue(TEXT, editorPane.getText());
    }
} 
