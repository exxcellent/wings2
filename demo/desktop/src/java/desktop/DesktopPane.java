package desktop;

import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SContainer;
import org.wings.SDesktopPane;
import org.wings.SFileChooser;
import org.wings.SGridLayout;
import org.wings.SInternalFrame;
import org.wings.SLabel;
import org.wings.SOptionPane;
import org.wings.SPanel;
import org.wings.SPopupMenu;
import org.wings.SMenuItem;
import org.wings.STextField;
import org.wings.SURLIcon;
import org.wings.style.CSSProperty;
import org.wings.util.ComponentVisitor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringWriter;

import java.util.prefs.*;



public class DesktopPane extends SDesktopPane {
	
	public static String FIRST_FREE_INDEX = "firstFreeIndex";
	public static String WEIGHTX = "weightx";
	
	private Preferences pref;
	
	
	public static org.wings.util.SessionLocal<Integer> paneNo = new org.wings.util.SessionLocal<Integer>(){
		@Override
		protected Integer initialValue() {
			return 0;
		}};
		
	public DesktopPane(String name){
		super();
		setName(name);
		
		
		pref = PreferenceHandler.getPreferenceHandler().getPanePreferences().node(this.getName());
				
		setCG(new DpCG());
		setVerticalAlignment(SConstants.TOP_ALIGN);
		addNonFrameComponent(new DropLabel());
				
		setupContextMenu();
	}
	
	public DesktopPane(){
		super();
		setName("desktoppane" + paneNo.get().toString());
		paneNo.set(paneNo.get()+1);
		
		pref = PreferenceHandler.getPreferenceHandler().getPanePreferences().node(this.getName());
		pref.putInt(FIRST_FREE_INDEX, paneNo.get());
		
				
		setCG(new DpCG());
		setVerticalAlignment(SConstants.TOP_ALIGN);
		addNonFrameComponent(new DropLabel());
				
		setupContextMenu();
	}
	
	private void setupContextMenu(){
		
		SPopupMenu popupMenu = new SPopupMenu();
		SMenuItem menuItem;
		
		for(final DesktopTool tool: ToolRegistry.getToolRegistry().getRegisteredTools().values()){
			menuItem = new SMenuItem(tool.getText(), tool.getIcon());
			menuItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
				addDesktopItem(tool.getItem());
				}
			});
			popupMenu.add(menuItem);
		}
				
		this.setComponentPopupMenu(popupMenu);
	}
	
	public void addDesktopItem(DesktopTool tool){
		addDesktopItem(tool.getItem());
	}
	
	public void addDesktopItem(DesktopItem contentItem){
		final InternalDesktopFrame newFrame = new InternalDesktopFrame();
		contentItem.setContainer(newFrame);
		Preferences p = PreferenceHandler.getPreferenceHandler().getItemPreferences();
		p.node((String)contentItem.getValue(DesktopItem.KEY)).put(DesktopItem.DESKTOPPANE, this.getName());
		p.node((String)contentItem.getValue(DesktopItem.KEY)).putInt(DesktopItem.POSITION_ON_PANE, getComponentList().size() -1);
		
		this.add(newFrame, getComponentList().size() -1);
		
		try {
        	this.invite(new ComponentVisitor() {
                    public void visit(SComponent c) { /* ign */ }
                    public void visit(SContainer c) {
                        if (! (c instanceof SInternalFrame))
                            return;
                        SInternalFrame ff = (SInternalFrame) c;
                        if (ff != newFrame && ff.isMaximized()) {
                            ff.setMaximized(false);
                            // set _our_ frame maximized, then.
                            newFrame.setMaximized(true);
                        }
                    }
                });
        	
        	pref.flush();
        }
        catch (Exception e) {
            System.err.println(e);
        }
        
	}
	
	public void addExistingDesktopItem(DesktopItem contentItem){
		final InternalDesktopFrame newFrame = new InternalDesktopFrame();
		contentItem.setContainer(newFrame);
		this.add(newFrame, getComponentList().size() -1);
		
		try {
        	this.invite(new ComponentVisitor() {
                    public void visit(SComponent c) { /* ign */ }
                    public void visit(SContainer c) {
                        if (! (c instanceof SInternalFrame))
                            return;
                        SInternalFrame ff = (SInternalFrame) c;
                        if (ff != newFrame && ff.isMaximized()) {
                            ff.setMaximized(false);
                            // set _our_ frame maximized, then.
                            newFrame.setMaximized(true);
                        }
                    }
                });
        	
        	pref.flush();
        }
        catch (Exception e) {
            System.err.println(e);
        }
	}
	
	public SComponent addNonFrameComponent(SComponent component){
		return addNonFrameComponent(component, null);
	}
	
	public SComponent addNonFrameComponent(SComponent component,
            Object constraints) {
		if (component != null) {
            // Sanity check: Component is only allowed to be inside one container
            // equal to Swing (1:n tree, not n:m tree)
            if (component.getParent() != null) {
                if (component.getParent() == this) {
                    // Forstall confusing ArrayOutOfBoundsException which would occur in the remove.
                    throw new IllegalArgumentException("Component must only added exactly " +
                            "once to exactly one container!");
                } else {
                    // Try to silently fix the double-add by removing it from the old parent.
                    component.getParent().remove(component);
                }
            }
		
		if (constraints == null)
            constraints = component.getName();
		
		getComponentList().add(component);
        getConstraintList().add(constraints);
        component.setParent(this);
        
        if(super.getLayout() != null)
        	super.getLayout().addComponent(component, constraints, getComponentList().indexOf(component));
        
        
        component.addNotify();
        
        this.reload();
		}
		return component;
	}

	public void removeAll(){
		for(SComponent comp: this.getComponents()){
			if(comp instanceof SInternalFrame)
			{
				((SInternalFrame)comp).dispose();
				remove(comp);
			}
			
			getComponentList().remove(comp);
		}
	}
}
