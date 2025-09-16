package objects;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.game.Room;
import java.util.ArrayList;
import java.util.List;

public class Explosion extends GameObject implements Interactable {

    private Point2D position;
    private Room room;
    private int ticks = 0;
    private boolean willDie = false;

    public Explosion(Point2D position, Room room) {
        super(position);
        this.position = position;
        this.room = room;
        explode();
    }

    @Override
    public String getName() {
        return "Fire_old";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public void interact(GameObject object) {
        if (object instanceof JumpMan) {
            JumpMan manel = (JumpMan) object;
            manel.setHP(-100);
        }else if (object instanceof DonkeyKong) {
            DonkeyKong DK = (DonkeyKong) object;
            DK.markToDie();
        } else if (object instanceof Interactable && !(object instanceof Explosion)) {
            ((Interactable) object).interact(this);
        }
    }

    public void explode() {
        GameObject obj = room.getElement(position);
        if (obj != null && !(obj instanceof Wall) && !(obj instanceof Stairs) && !(obj instanceof Floor)) {
            if (obj instanceof JumpMan) {
                interact(obj);
            } else if (obj instanceof Interactable && !(obj instanceof Explosion)) {
                ((Interactable) obj).interact(this);
            } else {
                obj.markToDie();
            }
        }
    }

    public void tick() {
        ticks++;
        if (ticks >= 3) {
            willDie = true;
        }
    }

    @Override
    public boolean willDie() {
        return willDie;
    }
}
