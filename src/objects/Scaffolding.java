package objects;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.utils.Point2D;


public class Scaffolding extends GameObject implements Interactable {

    public Scaffolding (Point2D position){
        super(position);
    }

    @Override
    public String getName(){
        return "Stairs";
    }

    @Override
    public int getLayer(){
        return 0;
    }

    @Override
public void interact(GameObject obj) {
    if (obj instanceof JumpMan) {
        JumpMan JP = (JumpMan) obj;

        // Randomly decide if JumpMan can move
        boolean canMove = Math.random() >= 0.5; // 50% chance

        // Set the canMove flag without changing the position
        JP.setCanMove(canMove);
    }
}

}
