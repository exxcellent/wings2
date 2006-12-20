package org.wings.session;

import org.wings.*;
import org.wings.io.ServletDevice;
import org.wings.io.Device;
import org.wings.util.SStringBuilder;

import java.io.*;

/**
 * This implementation queries a property <code>wings.error.template</code> for a resource name relative to the
 * webapp of a wingS template. In this template, placeholders must be defined for wingS components named
 * <code>EXCEPTION_STACK_TRACE</code>, <code>EXCEPTION_MESSAGE</code> and <code>WINGS_VERSION</code>.
 */
public class DefaultExceptionHandler
    implements ExceptionHandler
{
    public void handle(Device device, Throwable thrown) {
        try {
            String errorTemplateFile = (String)SessionManager.getSession().getProperty("wings.error.template");
            if (errorTemplateFile == null)
                throw new RuntimeException("Unable to display error page. " +
                    "In web.xml define wings.error.template to see the stacktrace online " +
                    "or wings.error.handler to replace the default exception handler");
            
            String resourcePath = SessionManager.getSession().getServletContext().getRealPath(errorTemplateFile);
            STemplateLayout layout = new STemplateLayout(resourcePath);
            final SFrame errorFrame = new SFrame();
            errorFrame.getContentPane().setLayout(layout);

            final SLabel errorStackTraceLabel = new SLabel();
            errorFrame.getContentPane().add(errorStackTraceLabel, "EXCEPTION_STACK_TRACE");

            final SLabel errorMessageLabel = new SLabel();
            errorFrame.getContentPane().add(errorMessageLabel, "EXCEPTION_MESSAGE");

            final SLabel versionLabel = new SLabel();
            errorFrame.getContentPane().add(versionLabel, "WINGS_VERSION");

            versionLabel.setText("wingS " + Version.getVersion() + " / " + Version.getCompileTime());

            // build the stacktrace wrapped by pre'device so line breaks are preserved
            SStringBuilder stackTrace = new SStringBuilder("<html><pre>");
            stackTrace.append(getStackTraceString(thrown));
            stackTrace.append("</pre>");
            errorStackTraceLabel.setText(stackTrace.toString());

            // if there is a message, print it, otherwise print "none".
            errorMessageLabel.setText(thrown.getMessage() !=null ? thrown.getMessage() : "none");
            errorFrame.setVisible(true);
            errorFrame.write(device);
            errorFrame.setVisible(false);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getStackTraceString(Throwable e) {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
