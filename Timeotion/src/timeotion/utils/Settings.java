/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package timeotion.utils;

import java.util.function.Consumer;
import java.util.prefs.Preferences;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;

/**
 *
 * @author brett
 */
public final class Settings {
    public static final String STARTUP_DIALOG_PREF = "StartDialog";
    
    private final Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
    private boolean showToast = false;
    
    private static volatile Settings instance;
    
    private Settings() {}
    
    // Thread-safe singleton class implementation
    public static synchronized Settings getInstance() {
        Settings self = Settings.instance;
        if (self == null) {
            // Implement double checked locking principle (see https://www.journaldev.com/1061/thread-safety-in-java)
            synchronized (Settings.class) {
                self = Settings.instance;
                if (self == null) {
                    Settings.instance = self = new Settings();
                }
            }
        }
        
        return self;
    }
    
    /**
     * Get preferences for storing key-value paired application configuration data.
     * NOT INTENDED FOR LARGE STRUCTURED DATA.
     * 
     * @return Preferences object for storing application configuration data.
     */
    public Preferences getPrefs() {
        return prefs;
    }
    
    /**
     * Set the configured state for showing toast messages.
     * 
     * @param enabled boolean Whether to show toast or not.
     */
    public void setToastEnabled(boolean enabled) {
        showToast = false;
    }
    
    /**
     * Whether toast messages are configured to be displayed.
     * 
     * @return boolean toast message enabled state.
     */
    public boolean isToastEnabled() {
        return showToast;
    }
    
    /**
     * 
     * 
     * @param type
     * @param title
     * @param headerText
     * @param message
     * @param optOutMessage
     * @param optOutAction
     * @param buttonTypes
     * @return 
     */
    public static Alert createAlertWithOptOut(AlertType type, String title, String headerText, 
        String message, String optOutMessage, Consumer<Boolean> optOutAction, 
        ButtonType... buttonTypes) {
        Alert alert = new Alert(type);
        // Need to force the alert to layout in order to grab the graphic,
         // as we are replacing the dialog pane with a custom pane
         alert.getDialogPane().applyCss();
         Node graphic = alert.getDialogPane().getGraphic();
         // Create a new dialog pane that has a checkbox instead of the hide/show details button
         // Use the supplied callback for the action of the checkbox
         alert.setDialogPane(new DialogPane() {
           @Override
           protected Node createDetailsButton() {
             CheckBox optOut = new CheckBox();
             optOut.setText(optOutMessage);
             optOut.setOnAction(e -> optOutAction.accept(optOut.isSelected()));
             return optOut;
           }
         });
         alert.getDialogPane().getButtonTypes().addAll(buttonTypes);
         alert.getDialogPane().setContentText(message);
         // Fool the dialog into thinking there is some expandable content
         // a Group won't take up any space if it has no children
         alert.getDialogPane().setExpandableContent(new Group());
         alert.getDialogPane().setExpanded(true);
         // Reset the dialog graphic using the default style
         alert.getDialogPane().setGraphic(graphic);
         alert.setTitle(title);
         alert.setHeaderText(headerText);
         return alert;
     }
}
