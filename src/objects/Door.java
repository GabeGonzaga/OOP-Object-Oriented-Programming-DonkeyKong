package objects;

import pt.iscte.poo.utils.Point2D;

public class Door extends GameObject {
    
    private String name = "DoorClosed";

    public Door(Point2D position) {
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

    public void open() {
        name = "DoorOpen";
    }
}
