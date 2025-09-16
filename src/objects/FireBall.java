package objects;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Movable;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;

public class FireBall extends GameObject implements Movable,Interactable {

    private Room currentRoom;

    public FireBall(Point2D position){
        super(position);
    }

    @Override
    public String getName(){
        return "Fire";
    }

    @Override
    public int getLayer(){
        return 1;
    }

    public void setCurrentRoom(Room currentRoom){
        this.currentRoom=currentRoom;
    }

    public Room getCurrentRoom(){
        return currentRoom;
    }

    @Override
    public void move() {
        //System.out.println("FireBall is moving from: " + super.getPosition());
        Point2D newPos = super.getPosition().plus(Direction.UP.asVector());
        // System.out.println("FireBall new position: " + newPos);
    
        if (newPos.getY() >= 10) {
            super.markToDie();
            // System.out.println("FireBall marked to die");
        } else {
            DonkeyKong dk = currentRoom.getDK();
            Bat bat = currentRoom.getBat();
            BadGuy badGuy = currentRoom.getBadGuy();

            if (dk !=null && currentRoom.getDK().getPosition().equals(newPos)) {
                interact(currentRoom.getDK());
            } else if (bat != null && currentRoom.getBat().getPosition().equals(newPos)) {
                interact(currentRoom.getBat());
            }else if (badGuy != null && currentRoom.getBadGuy().getPosition().equals(newPos)) {
                interact(currentRoom.getBadGuy());
            }
            super.setPosition(newPos);
            //System.out.println("FireBall moved to: " + newPos);
        }
    }

    // @Override
    // public void interact(GameObject obj){
    //     if(obj instanceof DonkeyKong){
    //         DonkeyKong dk = (DonkeyKong) obj;
    //         dk.damageDK(100);
    //         super.markToDie();
    //         dk.markToDie();
    //         // this.markToDie();
    //         GameEngine.getInstance().getCurrentRoom().removeObject(this);
    //         System.out.println("DK was Hit by FireBall");
    //     }
    // }
    @Override
    public void interact(GameObject obj) {
        if (obj instanceof DonkeyKong) {
            DonkeyKong dk = (DonkeyKong) obj;
            System.out.println("FireBall hit DonkeyKong at position: " + dk.getPosition());

            dk.damageDK(100); // Aplica 100 de dano
            // dk.markToDie();
            // dk.willDie();
            super.markToDie(); // Marca a FireBall para remoção

            // if (dk.getHP() <= 0) {
            //     dk.markToDie();
            //     dk.willDie();
            //     GameEngine.getInstance().getCurrentRoom().removeObject(this);
            //     System.out.println("DonkeyKong is dead and will be removed.");
            //}
        }
    }
}
