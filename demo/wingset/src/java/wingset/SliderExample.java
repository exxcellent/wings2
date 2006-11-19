package wingset;

import org.wings.SComponent;
import org.wings.SForm;
import org.wings.SGridLayout;
import org.wings.SSlider;
import org.wings.SLabel;

/**
 *
 * @author Christian Schyma
 */
public class SliderExample
        extends WingSetPane {
    
    public SliderExample() {
    }
    
    public SComponent createExample() {
        
        SGridLayout layout = new SGridLayout( 2 );
        SForm form = new SForm(layout);
        
        SSlider horizSlider1 = new SSlider(0, 200, 150);
        
        SSlider horizSlider2 = new SSlider(-100, 100, 50);
        
        SSlider horizSlider3 = new SSlider(-400, -200, -300);
        horizSlider3.setMajorTickSpacing(25);
        horizSlider3.setSnapToTicks(true);
        
        SSlider vertSlider = new SSlider(SSlider.VERTICAL, -50, 50, 20);
        vertSlider.setMajorTickSpacing(5);
        vertSlider.setSnapToTicks(true);
        
        form.add(new SLabel("slider 1 [0,200], initial value = 150")); 
        form.add(horizSlider1);
        
        form.add(new SLabel("slider 2 [-100, 100, initial value = 50")); 
        form.add(horizSlider2);
        
        form.add(new SLabel("slider 3 [-400, -200, initial value = -300, snaps to every 25 ticks")); 
        form.add(horizSlider3);
        
        form.add(new SLabel("slider 4 [-50, 50] initial value = 20, snaps to every 5 ticks")); 
        form.add(vertSlider);
        
        return form;
    }
    
}
