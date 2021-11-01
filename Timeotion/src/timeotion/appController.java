/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeotion;

import com.jfoenix.controls.JFXListView;
import customjavafxlibs.controls.FXTimer;
import customjavafxlibs.controls.ImageButton;
import customjavafxlibs.utils.Toast;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.SepiaTone;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import timeotion.utils.Settings;

/**
 *
 * @author brett
 */
public class appController implements Initializable {
    
    // Tabs
    @FXML
    private ImageButton btn_home, btn_settings, btn_profile, btn_share;
    
    @FXML
    private AnchorPane home, settings, profile, share;
    
    @FXML
    private JFXListView timersListView;
    
    // Track currently active tabs
    private AnchorPane activePane;
    private ImageButton activeTab;
    
    private int timerCount = 0;
    
    private FXTimer activeTimer; // Keep track of currently active timer for efficient control
    
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
    public void addTimer() {
        timersListView.setVisible(true);
        
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
            public void onDelete(FXTimer fxt) {
                final String timerName = fxt.getTimerName();
                String toastMsg;
                // Configure toast times (in milliseconds)
                int toastMsgTime = 500;
                int fadeInTime = 500;
                int fadeOutTime= 500;
                
                // Delete timer (returns true if exists; otherwise false if not exists)
                if (timersListView.getItems().remove(fxt)) {
                    toastMsg = "Deleted timer \"" + timerName + "\"";
                    if (primaryStage != null && Settings.isToastEnabled()) {
                        Toast.makeText(primaryStage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime);
                    } else {
                        System.out.println(toastMsg);
                    }
                    
                    timerCount--;
                } else {
                    toastMsg = "Could not delete timer, \"" + timerName + "\"";
                    if (primaryStage != null  && Settings.isToastEnabled()) {
                        Toast.makeText(primaryStage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime);
                    } else {
                        System.out.println(toastMsg);
                    }
                }
            }
        }));
        
        timerCount++;
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
    }
}
