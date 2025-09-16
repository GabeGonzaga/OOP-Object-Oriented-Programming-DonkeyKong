package objects;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Movable;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Direction;
import java.util.Random;
import java.util.ArrayList;

public class BadGuy extends GameObject implements Movable, Interactable{


    private Room currentRoom;
    private Random random = new Random();

    private int hp = 50;

    public BadGuy(Point2D position) {
        super(position);
    }

    @Override
    public String getName() {
        return "BadGuy";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public void move() {
        Direction[] directions = {Direction.LEFT, Direction.RIGHT};
        Direction randomDirection = directions[random.nextInt(directions.length)];
        Point2D newPos = super.getPosition().plus(randomDirection.asVector());

        if (newPos.getX() < 0 || newPos.getX() >= 10) {
            return;
        }

        GameObject belowObj = currentRoom.getElement(newPos.plus(Direction.DOWN.asVector()));
        if (belowObj instanceof Floor || belowObj instanceof Stairs || belowObj instanceof Scaffolding) {
            newPos = newPos.plus(Direction.DOWN.asVector());
        }

        ArrayList<GameObject> objs = currentRoom.getElementsAt(newPos);
        for(GameObject obj : objs) {
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
            if (!manel.hasSword()) {
                manel.setHP(-30);
                damageBG(manel.getDamage());
                System.out.println("Manel was hit by Bad Guy");
            } else {
                damageBG(manel.getDamage());
            }
        } else if (object instanceof Explosion) {
            super.markToDie();
        }else if(object instanceof FireBall){
            damageBG(50);
        }
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

     public void damageBG(int hp) {
        this.hp -= hp;

        if (this.hp <= 0) {
            this.markToDie();
            GameEngine.getInstance().getCurrentRoom().removeObject(this);
            ImageGUI.getInstance().setStatusMessage("Bag Guy defeated!");
        } else {
            ImageGUI.getInstance().setStatusMessage("Bad Guy HP: " + this.hp);
        }
    }

}
