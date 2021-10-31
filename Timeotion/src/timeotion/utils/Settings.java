/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package timeotion.utils;

/**
 *
 * @author brett
 */
public abstract class Settings {
    private static boolean showToast = false;
    
    /**
     * Set the configured state for showing toast messages.
     * 
     * @param enabled boolean Whether to show toast or not.
     */
    public static final void setToastEnabled(boolean enabled) {
        showToast = false;
    }
    
    /**
     * Whether toast messages are configured to be displayed.
     * 
     * @return boolean toast message enabled state.
     */
    public static final boolean isToastEnabled() {
        return showToast;
    }
}
