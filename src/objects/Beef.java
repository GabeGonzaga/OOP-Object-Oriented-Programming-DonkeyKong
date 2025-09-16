package objects;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.game.GameEngine;

public class Beef extends GameObject implements Interactable {

    private static final int ROTTEN_THRESHOLD = 16; 
    private String name = "GoodMeat";
    private int age = 0; 

    public Beef(Point2D position) {
        super(position);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public void interact(GameObject object) {
        if (object instanceof JumpMan) {
            JumpMan manel = (JumpMan) object;
            if (name.equals("GoodMeat")) {
                manel.setHP(70); 
                System.out.println("Manel ate fresh beef!");
            } else if (name.equals("BadMeat")) {
                manel.setHP(-20); 
                System.out.println("Manel ate rotten beef and got sick!");
            }
            GameEngine.getInstance().getCurrentRoom().removeObject(this);
        }
    }

    public void age() {
        age++;
        if (age > ROTTEN_THRESHOLD && name.equals("GoodMeat")) {
            name = "BadMeat";
            System.out.println("The beef has rotted and turned into bad meat.");
        }
    }
}
