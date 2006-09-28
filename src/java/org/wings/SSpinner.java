/*
 * SSpinner.java
 *
 * Created on 31. August 2006, 09:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.wings;

import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.SpinnerModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpinnerListModel;

import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SFormattedTextField;
import org.wings.SFrame;
import org.wings.SPanel;

import org.wings.text.SDefaultFormatter;
import org.wings.text.SNumberFormatter;
import org.wings.text.SDateFormatter;
import org.wings.text.SDefaultFormatterFactory;

/**
 *
 * @author erik
 */
public class SSpinner extends SComponent {
    
    private SpinnerModel    model;
    private DefaultEditor   editor;

    /** Creates a new instance of SSpinner */
    public SSpinner() {
    }
    
    /**
     * Creates a new instance of SSpinner
     * @param model the model for this Component
     */
    public SSpinner(SpinnerModel model) {
        this.model  = model;
        this.editor = createEditor( model );
    }
    
    protected DefaultEditor createEditor(SpinnerModel model) {
        DefaultEditor defaultEditor = null;
        
        if (model instanceof SpinnerNumberModel) {
	    defaultEditor = new NumberEditor(this);
        } else if (model instanceof SpinnerDateModel) {
	    defaultEditor = new DateEditor(this);
        } else if (model instanceof SpinnerListModel) {
	    defaultEditor = new ListEditor(this);
	} else {
	    defaultEditor = new DefaultEditor(this);
	}
        
        return defaultEditor;
    }
   
    /**
     * Returns the model of this Component.
     * @return the model of this Component
     */
    public SpinnerModel getModel() {
	return model;
    }
    
    /**
     * Sets the model for this Component.
     * @param model the model for this Component
     */
    public void setModel( SpinnerModel model ) {
        this.model = model;
    }
    
    /**
     * Returns the currrent value of this Component.
     * @return the current value of this Component
     */
    public Object getValue() {
	return getModel().getValue();
    }

    /**
     * Sets the current value for this Component.
     * @param value the new current value for this Component
     */
    public void setValue(Object value) {
	getModel().setValue(value);
    }
    
    /**
     * Returns the next value in the sequence.
     * @return the next value in the sequence
     */
    public Object getNextValue() {
	return getModel().getNextValue();
    }
    
    /**
     * Returns the current editor.
     * As long as wings donesn't support incremental updates for all components 
     * we have to return a DefaultEditor instead of SComponent.
     * @return DefaultEditor
     */
    public DefaultEditor getEditor() {
	return editor;
    }
    
    /**
     * Sets the editor for this Component.
     * @param editor the editor for this Component
     */
    public void setEditor(DefaultEditor editor) {
	if (editor != null && !editor.equals(this.editor) ) {
	    this.editor = editor;
	}
    }
    
    public static class DefaultEditor extends SPanel {
        
        SFormattedTextField ftf = null;
        
	public DefaultEditor(SSpinner spinner) {
	    super(null);

	    ftf = new SFormattedTextField( spinner.getValue() );
            ftf.setHorizontalAlignment(SConstants.RIGHT);

	    String toolTipText = spinner.getToolTipText();
	    if (toolTipText != null) {
		ftf.setToolTipText(toolTipText);
	    }

	    add(ftf);

	}
        
        /**
         * Returns the SFormattedTextField of this editor
         * @return the SFormattedTextField of this editor
         */
        public SFormattedTextField getTextField() {
	    return ftf;
	}
        
        /**
         * Returns the Spinner of this editor.
         * @return the Spinner of this editor
         */
        public SSpinner getSpinner() {
            
            SSpinner    spinner     = null;
            SComponent  component   = this;
            
            while ( component != null ) {
                if ( component instanceof SSpinner ) {
                    spinner = (SSpinner)component;
                    break;
                }
                component = component.getParent();
            }
            
            return spinner;
        }
        
    }
      
    public static class NumberEditor extends DefaultEditor 
    {

        public NumberEditor(SSpinner spinner) {
            this( spinner, getPattern() );
        }
        
	public NumberEditor(SSpinner spinner, String decimalFormatPattern) {
	    this(spinner, new DecimalFormat(decimalFormatPattern));            
	}
        
        private NumberEditor(SSpinner spinner, DecimalFormat format) {
	    super(spinner);
	    if (!(spinner.getModel() instanceof SpinnerNumberModel)) {
		throw new IllegalArgumentException(
                          "model not a SpinnerNumberModel");
	    }
 
	    SDefaultFormatterFactory factory = new SDefaultFormatterFactory( new SNumberFormatter( format ) );
            
	    SFormattedTextField ftf = getTextField();
                ftf.setFormatterFactory(factory);
	}
        
        private static String getPattern() {
            DecimalFormat decimalFormat = new DecimalFormat();
            String pattern = decimalFormat.toPattern() + ";-" + decimalFormat.toPattern();
            return pattern;
        }
    }
    
    public static class DateEditor extends DefaultEditor 
    {
        
	public DateEditor(SSpinner spinner) {
            this(spinner, getPattern());
	}
        
	public DateEditor(SSpinner spinner, String dateFormatPattern) {
	    this(spinner, new SimpleDateFormat(dateFormatPattern,
                                               spinner.getSession().getLocale()));
	}

	private DateEditor(SSpinner spinner, DateFormat format) {
	    super(spinner);
	    if (!(spinner.getModel() instanceof SpinnerDateModel)) {
		throw new IllegalArgumentException(
                                 "model not a SpinnerDateModel");
	    }

	    SDefaultFormatterFactory factory = new SDefaultFormatterFactory( new SDateFormatter( format ) );

	    SFormattedTextField ftf = getTextField();
                ftf.setFormatterFactory(factory);

        }
        
        private static String getPattern() {
            SimpleDateFormat sdf = new SimpleDateFormat();
            return sdf.toPattern();
        }

	public SimpleDateFormat getFormat() {
	    return (SimpleDateFormat)((SDateFormatter)(getTextField().getFormatter())).getFormat();
	}
        
        /**
         * Returns the SpinnerDateModel of this editor.
         * @return the SpinnerDateModel of this editor
         */
	public SpinnerDateModel getModel() {
	    return (SpinnerDateModel)(getSpinner().getModel());
	}
    }
    
    public static class ListEditor extends DefaultEditor 
    {

	public ListEditor(SSpinner spinner) {
	    super(spinner);
	    if (!(spinner.getModel() instanceof SpinnerListModel)) {
		throw new IllegalArgumentException("model not a SpinnerListModel");
	    }
            
            SDefaultFormatterFactory factory = new SDefaultFormatterFactory( new SDefaultFormatter() );
            
            SFormattedTextField ftf = getTextField();
                ftf.setFormatterFactory(factory);
	}

        /**
         * Returns the SpinnerListModel of this editor.
         * @return the SpinnerListModel of this editor
         */
	public SpinnerListModel getModel() {
	    return (SpinnerListModel)(getSpinner().getModel());
	}

    }
    
    protected void setParentFrame(SFrame f) {
        super.setParentFrame(f);
        getEditor().setParentFrame(f);
    }
   
}
