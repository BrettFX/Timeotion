/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package timeotion.animations;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.TimelineBuilder;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a pulse effect on the given node
 * 
 * Port of Pulse from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}@keyframes pulse {
 *     0% { transform: scale(1); }	
 * 	50% { transform: scale(1.1); }
 *     100% { transform: scale(1); }
 * }
 *
 * @author Jasper Potts
 */
public class PulseTransition extends CachedTimelineTransition {
    private static final float MIN_SCALE = 0.8f;
    private static final float MAX_SCALE = 1.0f;
    
    /**
     * Create new PulseTransition
     * 
     * @param node The node to affect
     */
    public PulseTransition(final Node node) {
        super(
            node,
            TimelineBuilder.create()
                .keyFrames(
                    new KeyFrame(Duration.millis(0), 
                        new KeyValue(node.scaleXProperty(), MAX_SCALE, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), MAX_SCALE, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(500), 
                        new KeyValue(node.scaleXProperty(), MIN_SCALE, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), MIN_SCALE, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(1000), 
                        new KeyValue(node.scaleXProperty(), MAX_SCALE, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), MAX_SCALE, WEB_EASE)
                    )
                )
                .build()
            );
        setCycleDuration(Duration.seconds(2));
        setDelay(Duration.seconds(0.2));
    }
}
