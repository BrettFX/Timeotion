/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeotion;

import customjavafxlibs.libs.ImageButton;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author brett
 */
public class appController implements Initializable {
    
    @FXML
    private ImageButton btn_home, btn_settings, btn_profile;
    
    @FXML
    private ImageView btn_exit;
    
    @FXML
    private AnchorPane home, settings, profile;
    
    // Track currently active tabs
    private AnchorPane activePane;
    private ImageButton activeTab;
    
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
    public void handleButtonAction(MouseEvent event) {
        if (event.getTarget().equals(btn_home)) {
            setActiveTab(home, btn_home);
        } else if (event.getTarget().equals(btn_settings)) {
            setActiveTab(settings, btn_settings);
        } else if (event.getTarget().equals(btn_profile)) {
            setActiveTab(profile, btn_profile);
        } else if (event.getTarget().equals(btn_exit)) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit");
            alert.setHeaderText("Close Program");
            alert.setContentText("Are you sure you would like to exit and close program?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                Platform.exit();
                System.exit(0);
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Active tab should be home by default
        // NOTE: Must set here to prevent null pointers (NPE)
        activePane = home;
        activeTab = btn_home;
        
        setActiveTab(home, btn_home);
    }
    
}
