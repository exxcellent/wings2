package desktop;

import java.util.ArrayList;
import java.util.List;

import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SDesktopPane;
import org.wings.SLabel;
import org.wings.dnd.DropTarget;
import org.wings.event.SComponentDropListener;
import org.wings.session.SessionManager;
import org.wings.style.CSSProperty;

public class DropLabel extends SLabel implements DropTarget {

	private ArrayList<SComponentDropListener> componentDropListeners = new ArrayList<SComponentDropListener>();
	
	public DropLabel() {
		super();
		this.setVerticalAlignment(SConstants.TOP_ALIGN);
		this.setAttribute(CSSProperty.HEIGHT, "200px");
		this.setAttribute(CSSProperty.WIDTH, "100%");
				
		addComponentDropListener(new SComponentDropListener() {
            public boolean handleDrop(SComponent dragSource) {
            	SDesktopPane sourcePane = (SDesktopPane)dragSource.getParent();
            	SDesktopPane targetPane = (SDesktopPane)DropLabel.this.getParent();
            	
            	
            	
            	if(sourcePane == targetPane && sourcePane.getIndexOf(dragSource)== sourcePane.getComponentCount()-2)
            		return false;
            	
            	sourcePane.remove(dragSource);
            	targetPane.add(dragSource, targetPane.getComponentCount()-1);
    			return true;
            }});
		
	}

	public void addComponentDropListener(SComponentDropListener listener) {
		componentDropListeners.add(listener);
        SessionManager.getSession().getDragAndDropManager().registerDropTarget(this);
		
	}

	public List<SComponentDropListener> getComponentDropListeners() {
		return componentDropListeners;
	}

}
