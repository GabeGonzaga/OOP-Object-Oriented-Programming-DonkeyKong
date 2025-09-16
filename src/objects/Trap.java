package objects;

import pt.iscte.poo.utils.Point2D;

public class Trap extends GameObject implements Interactable {

    public Trap(Point2D position) {
        super(position);
    }

    @Override
    public String getName() {
        return "Trap";
    }
    
    @Override
    public int getLayer() {
        return 0;
    }

    @Override
    public void interact(GameObject object) {
        if (object instanceof JumpMan) {
            JumpMan manel = (JumpMan) object;
            manel.setHP(-25);
            System.out.println("Manel stepped on a trap");
        }
    }
}
