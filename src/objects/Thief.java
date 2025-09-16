package objects;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Movable;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Direction;
import java.util.Random;
import java.util.ArrayList;

public class Thief extends GameObject implements Movable, Interactable {

    private Room currentRoom;
    private Random random = new Random();

    public Thief(Point2D position){
        super(position);
    }

    public String getName(){
        return "Thief";
    }

    public int getLayer(){
        return 1;
    }

    @Override
    public void move() {
        Point2D manelPos = currentRoom.getManel().getPosition();
        Point2D newPos;
        int xDistance = Math.abs(super.getPosition().getX() - manelPos.getX());
        int level = GameEngine.getInstance().getCurrentLevel();
        int range = (int) Math.max(1, 4 / Math.pow(2, level - 1));

        if (xDistance > range) {
            if (super.getPosition().getX() != manelPos.getX()) {
                int dx = (manelPos.getX() > super.getPosition().getX()) ? 1 : -1;
                newPos = new Point2D(super.getPosition().getX() + dx, super.getPosition().getY());
            } else {
                newPos = super.getPosition();
            }
        } else {
            int dx = (Math.random() < 0.5) ? -1 : 1;
            newPos = new Point2D(super.getPosition().getX() + dx, super.getPosition().getY());
            if (newPos.getX() < 0 || newPos.getX() >= 10) {
                newPos = super.getPosition();
            }
        }

        GameObject belowObj = currentRoom.getElement(newPos.plus(Direction.DOWN.asVector()));
        if (belowObj instanceof Floor || belowObj instanceof Stairs || belowObj instanceof Scaffolding) {
            newPos = newPos.plus(Direction.DOWN.asVector());
        }

        GameObject upObj = currentRoom.getElement(newPos.plus(Direction.UP.asVector()));
        if (upObj instanceof Stairs) {
            newPos = newPos.plus(Direction.UP.asVector());
        }

        ArrayList<GameObject> objs = currentRoom.getElementsAt(newPos);
        for (GameObject obj : objs) {
            if (obj != null && obj.getLayer() == 1) {
                interact(obj);
                return;
            }
        }
        super.setPosition(newPos);
    }

    @Override
    public void interact(GameObject object) {
        if (object instanceof JumpMan) {
            JumpMan manel = (JumpMan) object;
            manel.setHP(-15);
            manel.setHasSword(false);
            System.out.println("Manel has sword: " + manel.hasSword());
            super.markToDie();
            System.out.println("Manel was bitten by a Thief");
        } else if (object instanceof Explosion) {
            super.markToDie();
        }else if(object instanceof FireBall){
            super.markToDie();
        }
    }


    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

}
