package objects;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.utils.Point2D;

public class Key extends GameObject implements Interactable{

    public Key(Point2D position){
        super(position);
    }

    @Override
    public String getName(){
        return "Key";
    }

    @Override
    public int getLayer(){
        return 1;
    }

    @Override
    public void interact(GameObject object) {
        if (object instanceof JumpMan) {
            JumpMan manel = (JumpMan) object;
            manel.setKey();
            System.out.println("Manel has Key");
            GameEngine.getInstance().getCurrentRoom().removeObject(this);
        }
    }
}
