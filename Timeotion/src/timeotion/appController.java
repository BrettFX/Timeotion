/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeotion;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import customjavafxlibs.controls.FXTimer;
import customjavafxlibs.controls.ImageButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.SepiaTone;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author brett
 */
public class appController implements Initializable {
    
    @FXML 
    private TextField timerName; // FOR DEMO PURPOSE ONLY
    
    @FXML
    private Label lblTimer;    // FOR DEMO PURPOSE ONLY
    
    @FXML
    private JFXButton btnAddTimer;
    
    // Tabs
    @FXML
    private ImageButton btn_home, btn_settings, btn_profile, btn_share;
    
    // Demo
    @FXML
    private ImageButton btn_play, btn_reset;
    
    @FXML
    private AnchorPane home, settings, profile, share;
    
    @FXML
    private JFXListView timersListView;
    
    // Track currently active tabs
    private AnchorPane activePane;
    private ImageButton activeTab;
    
    private int timerCount = 0;
    
    /**
     * Set the active tab. Toggles visibility of previous active tab and turns
     * on the visibility of the new active tab.
     * 
     * @param tab The tab to set as active
     */
    private void setActiveTab(AnchorPane tab, ImageButton btn) {
        // Set tab active
        activeTab.setActive(false);
        activePane.setVisible(false);
            
        // Only want to set active tab to actual value (corner case)
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
        
//        boolean tabHovered = target.equals(btn_home) ||
//                             target.equals(btn_settings) ||
//                             target.equals(btn_profile) ||
//                             target.equals(btn_share);
        
        if (target instanceof ImageButton) {
            ImageButton tab = (ImageButton)target;
            SepiaTone tone = new SepiaTone(1.0);
            tab.setEffect(tone);
        }
    }
    
    @FXML
    public void handleMouseExit(MouseEvent event) {
        EventTarget target = event.getTarget();
//        boolean tabExited = target.equals(btn_home) ||
//                             target.equals(btn_settings) ||
//                             target.equals(btn_profile) ||
//                             target.equals(btn_share);
        
        if (target instanceof ImageButton) {
            ImageButton tab = (ImageButton)target;
            tab.setEffect(null); // Set to null to disable effect
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
    public void configureTimerState(MouseEvent event) {
        EventTarget target = event.getTarget();
        if (target.equals(btn_play)) {
            // Toggle active state
            btn_play.setActive(!btn_play.isActive());
            
            // TODO implement functionality for playing/pausing timer
            
            
        } else if (target.equals(btn_reset)) {
            // TODO implement functionality for reseting timer
            System.out.println("Reseting timer!");
            
        }
    }
    
    @FXML
    public void addTimer() {
        timersListView.setVisible(true);
//        timersListView.getItems().add(new Label("Timer " + (timerCount + 1)));
        timersListView.getItems().add(new FXTimer());
        timerCount++;
    }
    
    @FXML
    public void handleTimerNameKeyReleased(KeyEvent event) {
        KeyCode code = event.getCode();
        System.out.println("Pressed key with code: " + code);
        if (code.equals(KeyCode.ENTER)) {
            timerName.parentProperty().get().requestFocus();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Active tab should be home by default
        // NOTE: Must set here to prevent null pointers (NPE)
        activePane = home;
        activeTab = btn_home;
        
        setActiveTab(home, btn_home);
        
        // Initialize timers list view 
        // TODO remove debug/testing code
//        for (int i = 0; i < 50; i++) {
//            timersListView.getItems().add(new Label("Item " + (i+1)));
//        }
        
        timersListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FXTimer>() {

                @Override
                public void changed(ObservableValue<? extends FXTimer> list, FXTimer previous, FXTimer current) {
                    System.out.println("Changed!");
                    System.out.println("|-- Previous = " + previous);
                    System.out.println("|-- Current  = " + current);
                }	
        });
        
    }
    
    /**
     * Produce a formatted timestamp string from seconds to HH:MM:SS.
     * 
     * EX: 72 seconds -> 00:01:12
     * 
     * @param baseSeconds The base number of seconds that will be used to create
     *                    a formatted timestamp string. Seconds can be greater than 60.
     * @return String the formatted timestamp string based on the provided seconds in HH:MM:SS
     */
    private String secondsToHms(double baseSeconds) {
        int seconds =  (int)baseSeconds % 60;
        int hours = (int)baseSeconds / 60;
        int minutes = hours % 60;
        hours /= 60;

//      String.format("%02d:%02d:%04.1f", hours, minutes, seconds);      // 00:00:00.0
        return String.format("%02d:%02d:%02d", hours, minutes, seconds); // 00:00:00
    }
    
}
