package objects;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.utils.Point2D;

public class Flag extends GameObject implements Interactable {

    public Flag(Point2D position) {
        super(position);
    }

    @Override
    public String getName() {
        return "Flag";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public void interact(GameObject obj) {
        if (obj instanceof JumpMan) {
            JumpMan manel = (JumpMan) obj;
            manel.setCheckPoint();
            ImageGUI.getInstance().setStatusMessage("Marked Checkpoint!");
            System.out.println("Manel got a Checkpoint");
        }
    }



}
