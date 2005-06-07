/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package dwr;

import org.wings.text.SAbstractFormatter;
import org.wings.SFormattedTextField;
import org.wings.SComponent;
import org.wings.script.JavaScriptListener;
import org.wings.resource.DefaultURLResource;
import org.wings.header.Script;

import java.text.ParseException;

/**
 * @author hengels
 * @version $Revision$
 */
public abstract class DWRFormatter extends SAbstractFormatter {
    private Script header;

    public void install(SFormattedTextField field) {
        super.install(field);
        header = new Script("text/javascript",
                            new DefaultURLResource("../dwr/interface/formatter_" + field.getName() + ".js"));
        field.getParentFrame().addHeader(header);
        CallableManager.getInstance().registerCallable("formatter_" + field.getName(), this);
    }

    public void uninstall(SFormattedTextField field) {
        super.uninstall(field);
        field.getParentFrame().removeHeader(header);
        CallableManager.getInstance().unregisterCallable("formatter_" + field.getName());
    }

    public JavaScriptListener generateJavaScript(SFormattedTextField field, boolean actionListener) {
        return new JavaScriptListener("onblur",
                "document.getElementById('{0}').getElementsByTagName('input')[0].style.color = 'inherit';" +
                "formatter_{0}.validate(callback_{0}, document.getElementById('{0}').getElementsByTagName('input')[0].value)",
                "function callback_{0}(data) {\n" +
                "   if (!data && data != '') {\n" +
                "       document.getElementById('{0}').getElementsByTagName('input')[0].focus();\n" +
                "       document.getElementById('{0}').getElementsByTagName('input')[0].style.color = 'red';\n" +
                "   }\n" +
                "   else\n" +
                "       document.getElementById('{0}').getElementsByTagName('input')[0].value = data;\n" +
                "}\n", new SComponent[] { field });
    }

    public abstract String validate(String text);
}
