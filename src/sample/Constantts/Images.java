package sample.Constantts;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * @summary
 * Used to improve loading time and application speed
 * */
public class Images {
    // liked and unliked song icons
    public static final Image liked = new Image(Images.class.getResource("../Images/liked.png").toExternalForm());
    public static final Image unLiked = new Image(Images.class.getResource("../Images/unLiked.png").toExternalForm());

    // review star icons
    public static final Image starFilled = new Image(Images.class.getResource("../Images/starFilled.png").toExternalForm());
    public static final Image starUnfilled = new Image(Images.class.getResource("../Images/starUnfilled.png").toExternalForm());

    // play and pause icons
    public static final Image play = new Image(Images.class.getResource("../Images/play.png").toExternalForm());
    public static final Image pause = new Image(Images.class.getResource("../Images/pause.png").toExternalForm());

    // album covers icons
    public static final Map<String, Image> albumCovers = new HashMap<>();
}
