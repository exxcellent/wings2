package org.wings.plaf.css;

import org.wings.plaf.ComponentCG;

/**
 * @author hengels
 */
public interface CGDecorator
    extends ComponentCG
{
    ComponentCG getDelegate();

    void setDelegate(ComponentCG delegate);
}
