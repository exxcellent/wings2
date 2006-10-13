/*
 * StatesOfGermany.java
 *
 * Created on 29. September 2006, 15:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.wingx.XSuggestDataSource;

/**
 *
 * @author Christian_2
 */
public class StatesOfGermany implements XSuggestDataSource {
    
    List states = new ArrayList();
    
    private void init() {
        states.add("Berlin");
        states.add("Brandenburg");
        states.add("Baden-Württemberg");
        states.add("Bayern");
        states.add("Bremen");
        states.add("Hessen");
        states.add("Hamburg");
        states.add("Mecklenburg-Vorpommern");
        states.add("Niedersachsen");
        states.add("Nordrhein-Westfalen");
        states.add("Rheinland-Pfalz");
        states.add("Schleswig-Holstein");
        states.add("Saarland");
        states.add("Sachsen");
        states.add("Sachsen-Anhalt");
        states.add("Thüringen");
    }
    
    public List getData(String part) {
        
        List returning = new ArrayList();
        
        for (Iterator iter = states.iterator(); iter.hasNext();) {
            
            Object o = iter.next();
            
            if (o instanceof String) {
                if (((String) o).toLowerCase().startsWith(part.toLowerCase())) {
                    returning.add(o);
                }
            }
        }
        
        return returning;
    }
    
    
    /** Creates a new instance of StatesOfGermany */
    public StatesOfGermany() {
        
        init();
    }
}
