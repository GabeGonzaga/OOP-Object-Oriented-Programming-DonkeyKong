package objects;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.game.Room;
import java.util.ArrayList;
import java.util.List;
import pt.iscte.poo.game.GameEngine;

public class Bomb extends GameObject implements Interactable {

    private int ticks = 0;
    private boolean placed;

    public Bomb(Point2D position, boolean placed) {
        super(position);
        this.placed = placed;
    }

    @Override
    public String getName() {
        return "Bomb";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public void interact(GameObject object) {
        if (object instanceof JumpMan) {
            JumpMan manel = (JumpMan) object;
            if (!placed) {
                manel.setBomb();
                super.markToDie();
            } else {
                manel.setHP(-100);
                super.markToDie();
            }
        }
    }

    public void tick() {
        if (placed) {
            ticks++;
            if (ticks >= 5) {
                explode();
            }
        }
    }

    private void explode() {
        Room room = GameEngine.getInstance().getCurrentRoom();
        List<Point2D> explosionArea = getExplosionArea();
        for (Point2D pos : explosionArea) {
            Explosion explosion = new Explosion(pos, room);
            room.addElement(explosion);
            ImageGUI.getInstance().addImage(explosion);
        }
        room.removeObject(this);
        ImageGUI.getInstance().removeImage(this);
    }

    public boolean shouldExplode() {
        return ticks >= 5;
    }
    
    private List<Point2D> getExplosionArea() {
        List<Point2D> area = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                area.add(new Point2D(getPosition().getX() + dx, getPosition().getY() + dy));
            }
        }
        return area;
    }
}