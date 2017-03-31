package ravtrix.foodybuddy.model;

import java.io.File;

/**
 * Created by Ravinder on 3/20/17.
 */

public class Image {

    public File image;
    public String title;

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
