package org.wings;

/**
 * 
 * @author armin
 * created at 24.02.2004 13:05:00
 */
public class SDownloadButton extends SAbstractClickable {

    Resource resource;

    public SDownloadButton(Resource pResource) {
        resource = pResource;
    }

    public SDownloadButton(String text, Resource pResource) {
        super(text);
        resource = pResource;
    }

    public SDownloadButton(SIcon icon, Resource pResource) {
        super(icon);
        resource = pResource;
    }


    public boolean checkEpoch() {
        return false;
    }

    public SimpleURL getURL() {
        return resource.getURL();
    }


}
