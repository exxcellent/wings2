package org.wings.event;

import java.util.EventObject;
import org.wings.SComponent;


/**
 * SRenderEvent.java
 *
 *
 * Created: Wed Nov  6 10:06:57 2002
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SRenderEvent extends EventObject {
    
    /**
     * 
     */
    public SRenderEvent(SComponent source) {
        super(source);
    }
    
}// SRenderEvent

/*
   $Log$
   Revision 1.1  2004/10/04 16:13:16  hengels
   Initial revision

   Revision 1.1  2002/11/06 17:00:29  ahaaf
   o add support for render events

*/
