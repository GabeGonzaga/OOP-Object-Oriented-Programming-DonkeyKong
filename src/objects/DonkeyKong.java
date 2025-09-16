package objects;

import java.util.ArrayList;
import java.util.Random;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Movable;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;

public class DonkeyKong extends GameObject implements Movable, Interactable {

    private static final int BANANA_SPAWN_INTERVAL = 5;
    private int lastBananaSpawnTick = 0; // Ultimo Tick desde que deu spawn.
    private int bananasLeftToSpawn = 0; //Quantas faltam spawnar.
    private int lastBananaTick = 0; //tick da última banana spawnada.

    private Random random = new Random();
    
    private Room currentRoom;
    private boolean canMove = true;
    private int hp = 100;
    
    public DonkeyKong(Point2D position){
        super(position);
    }
    
    @Override
    public String getName() {
        return "DonkeyKong";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    // @Override
    // public void move() {
    //     Point2D manelPos = currentRoom.getManel().getPosition();
    //     Point2D newPos;
    //     int xDistance = Math.abs(super.getPosition().getX() - manelPos.getX());
    //     int level = GameEngine.getInstance().getCurrentLevel();
    //     int range = (int) Math.max(1, 4 / Math.pow(2, level - 1));

    //     if (xDistance > range) {
    //         if (super.getPosition().getX() != manelPos.getX()) {
    //             int dx = (manelPos.getX() > super.getPosition().getX()) ? 1 : -1;
    //             newPos = new Point2D(super.getPosition().getX() + dx, super.getPosition().getY());
    //         } else {
    //             newPos = super.getPosition();
    //         } 
    //     } else {
    //         int dx = (Math.random() < 0.5) ? -1 : 1;
    //         newPos = new Point2D(super.getPosition().getX() + dx, super.getPosition().getY());
    //         if (newPos.getX() < 0 || newPos.getX() >= 10) {
    //             newPos = super.getPosition();
    //         }
    //     }

    //     GameObject obj = currentRoom.getElement(newPos);

    //     if (obj != null && obj.getLayer() == 1) colision(obj);

    //     if (newPos.equals(manelPos)) {
    //         canMove = false;
    //         if (!currentRoom.getManel().hasSword()) currentRoom.getManel().setHP(-30);
    //     }

    //     if (canMove && !newPos.equals(manelPos) && !(obj instanceof DonkeyKong)) {
    //         super.setPosition(newPos);
    //     } else {
    //         canMove = true;
    //     }
    // }

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

        GameObject upObj = currentRoom.getElement(newPos.plus(Direction.UP.asVector()));
        if(upObj instanceof Stairs){
            newPos = newPos.plus(Direction.UP.asVector());
        }

        ArrayList<GameObject> objs = currentRoom.getElementsAt(newPos);
        for(GameObject obj : objs) {
            if (obj != null && obj.getLayer() == 1) {
                interact(obj);
                return;
            }
        }
        super.setPosition(newPos);


        GameObject obj = currentRoom.getElement(newPos);

        Point2D manelPos = currentRoom.getManel().getPosition();

        if (obj != null && obj.getLayer() == 1) colision(obj);

        if (newPos.equals(manelPos)) {
            canMove = false;
            if (!currentRoom.getManel().hasSword()) currentRoom.getManel().setHP(-30);
        }

        if (canMove && !newPos.equals(manelPos) && !(obj instanceof DonkeyKong)) {
            super.setPosition(newPos);
        } else {
            canMove = true;
        }
    }

    public void spawnBanana() {
        int currentTick = ImageGUI.getInstance().getTicks();

        if (currentRoom != null && currentTick - lastBananaSpawnTick >= BANANA_SPAWN_INTERVAL) {
            bananasLeftToSpawn = random.nextInt(3) + 1;
            lastBananaSpawnTick = currentTick; 
        }

        if (bananasLeftToSpawn > 0 && currentTick - lastBananaTick >= BANANA_SPAWN_INTERVAL / 3) {
            Banana banana = new Banana(super.getPosition());
            banana.setCurrentRoom(currentRoom);
            currentRoom.addElement(banana);
            ImageGUI.getInstance().update();

            bananasLeftToSpawn--;
            lastBananaTick = currentTick;
        }
    }

    @Override
    public void interact(GameObject object) {
        if (object instanceof JumpMan) {
            JumpMan manel = (JumpMan) object;
            if (!manel.hasSword()) {
                manel.setHP(-30);
                damageDK(manel.getDamage());
                System.out.println("Manel was hit by Donkey Kong");
            } else {
                damageDK(manel.getDamage());
            }
        } else if (object instanceof Explosion) {
            Explosion explosion = (Explosion) object;
            explosion.interact(this);
        }else if(object instanceof FireBall){
           // FireBall fire = (FireBall) object;
            // fire.interact(this);
            // super.markToDie();
            damageDK(50);
        }
        if(hp <= 0){
            if(currentRoom.getElement(new Point2D(0,0)) instanceof Door){
                Door door = (Door) currentRoom.getElement(new Point2D(0,0));
                door.open();
            }
        }
    } 

    public int getHP(){
        return hp;
    }

    public void damageDK(int hp) {
        this.hp -= hp;
        System.out.println("Donkey Kong HP: " + this.hp);
    
        if (this.hp <= 0) {
            this.markToDie();
            Room currentRoom = GameEngine.getInstance().getCurrentRoom();
            if (currentRoom != null) {
                currentRoom.removeObject(this); // Explicitly remove from room
                System.out.println("Donkey Kong removed from room");
            } else {
                System.out.println("Current room is null");
            }
            ImageGUI.getInstance().setStatusMessage("Donkey Kong defeated!");
        } else {
            ImageGUI.getInstance().setStatusMessage("Donkey Kong HP: " + this.hp);
        }
    }
    
    
    

    public void colision(GameObject object) {
        if (object instanceof Wall) {
            canMove = false;
        } else if (object instanceof JumpMan) {
            canMove = false;
        } else if (object instanceof DonkeyKong) {
            canMove = false;
        }else if(object instanceof FireBall){
            canMove=false;
        } else {
            canMove = true;
        }
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }
}