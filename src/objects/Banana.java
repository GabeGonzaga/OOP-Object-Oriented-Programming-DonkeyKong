package objects;

import pt.iscte.poo.game.Movable;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.game.Room;

public class Banana extends GameObject implements Movable, Interactable{

    private Room currentRoom;

    public Banana(Point2D position) {
        super(position);
    }

    @Override
    public String getName() {
        return "Banana";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }
    

    @Override
    public void move() {
        Point2D newPos = super.getPosition().plus(Direction.DOWN.asVector());
        if (newPos.getY() >= 10) {
            super.markToDie();
        } else {
            if (currentRoom.getManel().getPosition().equals(newPos)) {
                interact(currentRoom.getManel());
            }
            super.setPosition(newPos);
        }
    }

    @Override
    public void interact(GameObject object) {
        if (object instanceof JumpMan) {
            JumpMan manel = (JumpMan) object;
            manel.setHP(-10);
            super.markToDie();
            System.out.println("Manel slipped on a banana");
        }
    }
}