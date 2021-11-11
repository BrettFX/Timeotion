/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeotion;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXToggleButton;
import customjavafxlibs.controls.FXTimer;
import customjavafxlibs.controls.ImageButton;
import customjavafxlibs.utils.Toast;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.SepiaTone;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import timeotion.animations.PulseTransition;
import timeotion.utils.Settings;

/**
 *
 * @author Brett Allen
 */
public class appController implements Initializable {
    // Misc
    @FXML
    private Label lblTotalTime;
    
    @FXML 
    private ImageButton btnAddTimer;
    
    // Tabs
    @FXML
    private ImageButton btn_home, btn_settings, btn_profile, btn_share;
    
    // Pages
    @FXML
    private AnchorPane home, settings, profile, share;
    
    @FXML
    private JFXListView timersListView;
    
    // Track currently active tabs
    private AnchorPane activePane;
    private ImageButton activeTab;
    
    private int timerCount = 0;
    private double totalTimeSecs = 0;
    
    private FXTimer activeTimer; // Keep track of currently active timer for efficient control
    
    private PulseTransition pulseAnimation;
    
    private Stage primaryStage;
    
    /**
     * Set the active tab. Toggles visibility of previous active tab and turns
     * on the visibility of the new active tab.
     * 
     * @param tab The btn to set as active
     */
    private void setActiveTab(AnchorPane tab, ImageButton btn) {
        // Set btn active
        activeTab.setActive(false);
        activePane.setVisible(false);
            
        // Only want to set active btn to actual value (corner case)
        if (tab != null && btn != null) {
            activeTab = btn;
            activeTab.setActive(true);
            
            activePane = tab;
            activePane.setVisible(true);
        } 
    }
    
    @FXML
    public void handleMouseOver(MouseEvent event) {
        EventTarget target = event.getTarget();
        if (target instanceof ImageButton) {
            ImageButton btn = (ImageButton)target;
            SepiaTone tone = new SepiaTone(1.0);
            btn.setEffect(tone);
        }
    }
    
    @FXML
    public void handleMouseExit(MouseEvent event) {
        EventTarget target = event.getTarget();
        if (target instanceof ImageButton) {
            ImageButton btn = (ImageButton)target;
            btn.setEffect(null); // Set to null to disable effect
        }
    }
    
    @FXML
    public void handleButtonAction(MouseEvent event) {
        if (event.getTarget().equals(btn_home)) {
            setActiveTab(home, btn_home);
        } else if (event.getTarget().equals(btn_settings)) {
            setActiveTab(settings, btn_settings);
        } else if (event.getTarget().equals(btn_profile)) {
            setActiveTab(profile, btn_profile);
        } else if (event.getTarget().equals(btn_share)) {
//            Alert alert = new Alert(AlertType.CONFIRMATION);
//            alert.setTitle("Exit");
//            alert.setHeaderText("Close Program");
//            alert.setContentText("Are you sure you would like to exit and close program?");
//
//            Optional<ButtonType> result = alert.showAndWait();
//            if (result.get() == ButtonType.OK){
//                Platform.exit();
//                System.exit(0);
//            }
              setActiveTab(share, btn_share);
        }
    }
    
    @FXML
    public void toggleAlwaysOnTop(ActionEvent event) {
        EventTarget target = event.getTarget();
        if (target instanceof JFXToggleButton) {
            boolean selected = ((JFXToggleButton)target).isSelected();
            primaryStage.setAlwaysOnTop(selected);
        }
    }
    
    @FXML
    public void clearPreferences() {
        // Get confirmation from user before clearing preferences
        Alert alert = new Alert(
            AlertType.CONFIRMATION,
            null,
            ButtonType.YES,
            ButtonType.CANCEL
        );
        
        alert.setTitle("Reset Preferences?");
        alert.setHeaderText("Are you sure you would like to reset preferences?");
        alert.getDialogPane().setContentText("Resetting preferences will result in"
            + " any configured dialog boxes to reappear automatically until configured"
            + " otherwise.");

        alert.showAndWait();

        // Only proceed with timer reset if the user selected yes
        Alert resultAlert;
        if (alert.getResult().equals(ButtonType.YES)) {
            try {
                Settings.getInstance().getPrefs().clear();
                resultAlert = new Alert(
                    AlertType.INFORMATION,
                    null,
                    ButtonType.CLOSE
                );
                
                resultAlert.setTitle("Preferences Cleared");
                resultAlert.setHeaderText("Preferences have been successfully cleared.");
                resultAlert.getDialogPane().setContentText("All dialog popups will now be displayed until"
                    + " configured otherwise.");
                
            } catch (BackingStoreException ex) {
                Logger.getLogger(appController.class.getName()).log(Level.SEVERE, null, ex);
                resultAlert = new Alert(
                    AlertType.ERROR,
                    null,
                    ButtonType.CLOSE
                );
                
                resultAlert.setTitle("Error When Clearing Preferences");
                resultAlert.setHeaderText("Could not clear Preferences because of a BackingStoreException.");
                resultAlert.getDialogPane().setContentText("Please contact support"
                    + " at 443-812-0896 for assistance with this error. We apologize for the inconvenience.");
            }
            
            resultAlert.showAndWait();
        }
    }
    
    @FXML
    public void addTimer() {
        if (!timersListView.isVisible()) {
            lblTotalTime.setVisible(true);
            timersListView.setVisible(true);
        }
        
        // TODO add listner for playing timers to enforce business rule of only one timer
        //      playing at a time.
        
        
        // Implement event lister to enforce business rules on timers
        timersListView.getItems().add(new FXTimer(new FXTimer.FXTimerEventListener() {
            @Override
            public void onPlay(FXTimer fxt) {
                // Business rule: only one timer is allowed to play by default (may change this rule in the future)
                if (activeTimer != null) {
                    activeTimer.pause();
                }
                
                activeTimer = fxt;
                
                // LESS EFFICIENT APPROACH: But might be needed for multi-timer playing support in the future
                // Itereate list of timers and pause all that are currently playing
//                ObservableList<FXTimer> timersList = timersListView.getItems();
//                for (FXTimer timer : timersList) {
//                    // Only set timer to paused if it's not the same as the timer that should be playing
//                    if (!timer.equals(fxt) && timer.isPlaying()) {
//                        timer.pause();
//                        break;
//                    }
//                }
            }

            @Override
            public void onTick(FXTimer timer, int secsAdded) {
                // Only add time if this timer is set to be included in total time aggregation
                if (timer.isIncludedInTotal()) {
                    // Keep track of total number of seconds added over time across timers
                    updateTotalTime(secsAdded);
                }
            }

            @Override
            public void onReset(FXTimer timer, int secsBeforeReset) {
                // Subtract the number of seconds before reset to normalize total aggregation
                updateTotalTime(-1 * secsBeforeReset);
            }
            
            @Override
            public void onDelete(FXTimer fxt) {
                final String timerName = fxt.getTimerName();
                int secsBeforeDelete = fxt.getAccumulatedTimeSecs();
                String toastMsg;
                // Configure toast times (in milliseconds)
                int toastMsgTime = 500;
                int fadeInTime = 500;
                int fadeOutTime= 500;
                
                // Delete timer (returns true if exists; otherwise false if not exists)
                if (timersListView.getItems().remove(fxt)) {
                    toastMsg = "Deleted timer \"" + timerName + "\"";
                    if (primaryStage != null && Settings.getInstance().isToastEnabled()) {
                        Toast.makeText(primaryStage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime);
                    }
                    
                    // Keep track of total timers and time aggregation
                    timerCount--;
                    updateTotalTime(-1 * secsBeforeDelete);
                    
                    // If there are no more timers left then turn off visbility of widgets and play
                    // pulse animation for the add timer button
                    if (timerCount == 0) {
                        timersListView.setVisible(false);
                        lblTotalTime.setVisible(false);
                        pulseAnimation.play();
                    }
                } else {
                    toastMsg = "Could not delete timer, \"" + timerName + "\"";
                    if (primaryStage != null  && Settings.getInstance().isToastEnabled()) {
                        Toast.makeText(primaryStage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime);
                    } else {
                        System.out.println(toastMsg);
                    }
                }
            }

            @Override
            public void onIncludeInTotalChanged(boolean include, int currentSecs) {
                // Control whether to include this timer in the total time aggregation
                // Add time (back) to total if include; otherwise subtract from total
                int sign = include ? 1 : -1;
                updateTotalTime(sign * currentSecs);
            }
        }));
        
        timerCount++;
        pulseAnimation.jumpTo(Duration.INDEFINITE); // Jump to beginning by passing negative duration
        pulseAnimation.stop();
    }
    
    /**
     * Update the total time aggregated across all existing timers. The value
     * provided as a delta time in seconds and be positive or negative and 
     * add or subtract from the current running total respectively.
     * 
     * @param deltaSeconds Positive or negative seconds to use to adjust the
     *                     current total time aggregation.
     */
    private void updateTotalTime(int deltaSeconds) {
        totalTimeSecs += deltaSeconds;
        
        // Update the total time label based on the new total time in seconds
        String totalHms = FXTimer.secondsToHms(totalTimeSecs);
        String totalHours = FXTimer.secondsToHours(totalTimeSecs);
        lblTotalTime.setText(totalHms + " | " + totalHours);
    }
    
    /**
     * Set primary stage for this controller instance.
     * 
     * @param stage 
     */
    public void setStage(Stage stage) {
        primaryStage = stage;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Active btn should be home by default
        // NOTE: Must set here to prevent null pointers (NPE)
        activePane = home;
        activeTab = btn_home;
        
        setActiveTab(home, btn_home);
        
        // Create tooltips 
        Tooltip.install(btnAddTimer, new Tooltip("Add a new timer"));
        Tooltip.install(lblTotalTime, new Tooltip("Total time across timers in HH:MM:SS format | Total hours"));
        
        // Create, configure, and play animations
        pulseAnimation = new PulseTransition(btnAddTimer);
        pulseAnimation.setCycleCount(Transition.INDEFINITE);
        pulseAnimation.play(); // TODO if saved timers exist then this should not be played on init
        
        // TODO Add saved timers from prefs
        
        
        // Initialize timers list view 
        timersListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FXTimer>() {

            @Override
            public void changed(ObservableValue<? extends FXTimer> list, FXTimer previous, FXTimer current) {
//                    System.out.println("Changed!");
//                    System.out.println("|-- Previous = " + previous);
//                    System.out.println("|-- Current  = " + current);

                // TODO handle selection changed listener as needed


            }	
        });
        
        boolean showStartupDialog = Settings.getInstance().getPrefs().getBoolean(Settings.STARTUP_DIALOG_PREF, true);
        
        // Only show startup dialog if the user hasn't configured for it not to show
        if (showStartupDialog) {
            // Run later so the stage renders before the dialog pops up
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    // Show dialog on startup to notify user of auto-save feature
                    // Provide an option to not show again and safe in prefs
                    Alert alert = Settings.createAlertWithOptOut(
                        AlertType.INFORMATION, 
                        "Welcome to Timeotion!", 
                        null, 
                        "This app uses an autosave feature that consistently tracks"
                            + " each timer you create. If you close this app at any"
                            + " point or if your computer crashes, nothing will be"
                            + " lost. Be at ease and enjoy tracking your time, in motion,"
                            + " efficiently.",
                        "Do not show again", 
                        new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean checked) {
                                Settings.getInstance().getPrefs().putBoolean(Settings.STARTUP_DIALOG_PREF, !checked);
                            }
                        },
                        ButtonType.OK
                    );

                    alert.showAndWait();
                    if (alert.getResult().equals(ButtonType.OK)) {
                       System.out.println("User clicked OK");
                    }
                }
            });
        }
        
    }
}
