package desktop;

import org.wings.SComponent;
import org.wings.SContainer;
import org.wings.SDesktopPane;


public class DesktopPane extends SDesktopPane {
	
	public DesktopPane(){
		super();
		setCG(new DpCG());
		
	}
	
	public SComponent addNonFrameComponent(SComponent component){
		return addNonFrameComponent(component, null);
	}
	
	public SComponent addNonFrameComponent(SComponent component,
            Object constraints) {
		
		getComponentList().add(component);
        //getConstraintList().add(constraints);
        component.setParent(this);
		return component;
	}

}
