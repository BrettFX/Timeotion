/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeotion;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author brett
 */
public class appController implements Initializable {
    
    @FXML
    private ImageView btn_home, btn_settings, btn_profile, btn_exit;
    
    @FXML
    private AnchorPane home, settings, profile;
    
    // Track currently active tabs
    private AnchorPane activeTab;
    
    /**
     * Set the active tab. Toggles visibility of previous active tab and turns
     * on the visibility of the new active tab.
     * 
     * @param tab The tab to set as active
     */
    private void setActiveTab(AnchorPane tab) {
        activeTab.setVisible(false);
            
        // Only want to set active tab to actual value (corner case)
        if (tab != null) {
            activeTab = tab;
            activeTab.setVisible(true);
        }
    }
    
    @FXML
    private void handleButtonAction(MouseEvent event) {
        if (event.getTarget().equals(btn_home)) {
            setActiveTab(home);
        } else if (event.getTarget().equals(btn_settings)) {
            setActiveTab(settings);
        } else if (event.getTarget().equals(btn_profile)) {
            setActiveTab(profile);
        } else if (event.getTarget().equals(btn_exit)) {
            setActiveTab(null); // Setting null hides all tabs
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Active tab should be home by default
        // NOTE: Must set here to prevent null pointers (NPE)
        activeTab = home;
    }
    
}
