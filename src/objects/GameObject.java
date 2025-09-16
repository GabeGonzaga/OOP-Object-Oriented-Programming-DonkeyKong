package objects;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public abstract class GameObject implements ImageTile {

    private Point2D position;  
	private boolean willDie = false;

    public GameObject(Point2D position) {
        this.position = position;
    }

    public abstract String getName();
    public abstract int getLayer();

    
    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }
    
	public boolean willDie() { 
		return willDie;
	}

    public void markToDie() { 
		this.willDie = true; 
	}
}
