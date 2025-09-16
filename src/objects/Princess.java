package objects;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.gui.ImageGUI;

public class Princess extends GameObject implements Interactable {

    public Princess(Point2D position) {
        super(position);
    }

    @Override
    public String getName() {
        return "Princess";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public void interact(GameObject object) {
        if (object instanceof JumpMan) {
            ImageGUI.getInstance().setStatusMessage("You won!");
            ImageGUI.getInstance().showMessage("Congratulations!", "You won the game!");
            ImageGUI.getInstance().dispose();
        }
    }
}
