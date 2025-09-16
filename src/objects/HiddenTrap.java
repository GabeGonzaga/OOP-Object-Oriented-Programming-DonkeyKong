package objects;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

public class HiddenTrap extends GameObject implements Interactable {

    private String name = "Wall";

    public HiddenTrap(Point2D position) {
        super(position);
    }

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public int getLayer() {
        return 0;
    }

    @Override
    public void interact(GameObject object) {
        if (object instanceof JumpMan) {
            name = "HiddenTrap";
            ImageGUI.getInstance().removeImage(this);
            ImageGUI.getInstance().addImage(this);
            ((JumpMan) object).setHP(-25);
            System.out.println("Manel stepped on a Hidden Trap");
        }
    }
}