/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeotion;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Brett Allen
 */
public class Timeotion extends Application {
    
    private final boolean TRANSPARENT_MODE = false;
    
    private static final String USER_AGENT_STYLESHEET = Timeotion.class.getResource("timeotion.css").toExternalForm();
    
    private double xOffset = 0;
    private double yOffset = 0;
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("timeotion.fxml"));
        Parent root = (Parent)loader.load();
        
        stage.setTitle("Timeotion - Timesheet Tracker");
        stage.setResizable(false);
        stage.centerOnScreen();
        
        // Set app icon on the stage
        // NOTE: Need to retrieve resource as stream to change icon on taskbar/dock
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/timeotion/images/timeotion.png")));
        
        // Pass primary stage to app controller for custom rendering (e.g., toast)
        appController controller = (appController)loader.getController();
        controller.setStage(stage);
        
        // Instantiate the scene and set the fill color to transparent
        Scene scene = new Scene(root);
        scene.getStylesheets().add(USER_AGENT_STYLESHEET);
        
        if (TRANSPARENT_MODE) {
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            
            // Mouse press event listner to assist mouse dragging
            // NOTE: Disable if not using transparent stage style
            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });

            // Mouse drag event listener to set the position of the window programatically
            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });
        } else {
            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    // Request focus to take focus away from child nodes within root
                    root.requestFocus();
                }
            });
        }
        
        // Set the scene for the stage and show it
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
