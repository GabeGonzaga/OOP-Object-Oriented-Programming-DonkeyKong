package objects;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.game.Room;
import java.util.ArrayList;
import pt.iscte.poo.game.GameEngine;
import java.awt.event.KeyEvent;

public class JumpMan extends GameObject implements Interactable {

    private boolean canMove = true;
    private boolean hasSword = false;
    private boolean willDie = false;
    private boolean hasBomb = false;
    private boolean hasKey = false;

    private int hp = 100;
    private int damage = 10;
    private int lives = 2;

    //private int countTicks = 0; //Fazer que ao passarem 20 ticks na mesma below position: wall, a imagem passa a ser Grass.

    private Room currentRoom;

    private String name = "JumpMan";

    public JumpMan(Point2D position){
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

    public void move() {
		
        if (ImageGUI.getInstance().keyPressed() == KeyEvent.VK_B && hasBomb) {
            useBomb();
            return;
        }
		Point2D newPos = super.getPosition().plus(Direction.directionFor(ImageGUI.getInstance().keyPressed()).asVector());

        canMove = true;

        if (!canMove) {
            return;
        }

        ArrayList<GameObject> objs = currentRoom.getElementsAt(newPos);
        
        if (newPos.getX() < 0 || newPos.getX() >= 10 || newPos.getY() < 0 || newPos.getY() >= 10) {
            canMove = false;
        } else {
            for (GameObject obj : objs) {
                if (obj != null && obj.getLayer() == 1) {
                    interact(obj);
                    if (!canMove) break;
                }
                if (obj != null && obj.getLayer() == 0 && Direction.directionFor(ImageGUI.getInstance().keyPressed()).equals(Direction.UP)) {
                    interact(obj);
                    if (!canMove) break;
                }
            }
            if ((currentRoom.getElement(this.getPosition()) instanceof Stairs || currentRoom.getElement(this.getPosition()) instanceof Scaffolding) && Direction.directionFor(ImageGUI.getInstance().keyPressed()).equals(Direction.UP)) {
                canMove = true;
            }
        }
        if (canMove) {
            ImageGUI.getInstance().removeImage(this);
            super.setPosition(newPos);
            ImageGUI.getInstance().addImage(this);
        }
    }
    
    @Override
    public void interact(GameObject obj) {
        if (obj instanceof Wall || obj instanceof Floor || obj instanceof Trap || obj instanceof HiddenTrap) {
            canMove = false;
        } else if (obj instanceof DonkeyKong) {
            DonkeyKong DK = (DonkeyKong) obj;
            DK.interact(obj);
            canMove = false;
        } else if(obj instanceof Banana) {
            Banana banana = (Banana) obj;
            banana.interact(this);
            canMove = true;
        } else if (obj instanceof Bat) {
            Bat bat = (Bat) obj;
            bat.interact(this);
            bat.markToDie();
            canMove = false;
        } else if (obj instanceof Bomb) {
            hasBomb = true;
			Bomb bomb = (Bomb) obj;
			bomb.interact(this);
			canMove = false;
		} else if (obj instanceof Stairs) {
			canMove = true;
		} else if(obj instanceof Explosion) {
            Explosion explosion = (Explosion) obj;
            explosion.interact(this);
            canMove = true;
        }else if (obj instanceof FireBall){
            FireBall fire = (FireBall) obj;
            fire.interact(this);
            canMove=true;
        }else if(obj instanceof BadGuy){
            BadGuy badguy = (BadGuy) obj;
            badguy.interact(this);
            canMove = false;
        }else if (obj instanceof Door){
            Door door = (Door) obj;
            if(!hasKey){
                door.open();
                canMove = true;
            }else{
                canMove = false;
                }
        }else if(obj instanceof Flag){
            Flag flag = (Flag) obj;
            flag.interact(this);
            canMove = true;
        } else if (obj instanceof Scaffolding) {
           // Scaffolding escadote = (Scaffolding) obj;
            
			canMove = true;
        } else if (obj instanceof Thief){
            Thief thief = (Thief) obj;
            thief.interact(this);
            canMove = false;
        }else {
            canMove = true;
        }
    }
			
    public void setHP(int amount) {
        if (willDie) GameEngine.getInstance().reset();

        this.hp += amount;
        if (this.hp > 100) {
            this.hp = 100;
        } else if (this.hp <= 0) {
            this.hp = 0;
            if (this.lives == 0) {
                markToDie();
                ImageGUI.getInstance().setStatusMessage("You Died!");
                ImageGUI.getInstance().showMessage("Game Over", "You lost the game!");
                GameEngine.getInstance().reset();
            } else if (this.lives > 0) {
                this.lives--;
                resetPositionAndHP();
                ImageGUI.getInstance().setStatusMessage("Current Lives: " + this.lives);
            }
        }
        ImageGUI.getInstance().setStatusMessage("JumpMan's HP: " + this.hp);
    }

    private void resetPositionAndHP() {
        this.hp = 100;
        this.setPosition(currentRoom.getStartingPosition());
        ImageGUI.getInstance().removeImage(this);
        ImageGUI.getInstance().addImage(this);
        this.willDie = false;
    }

    public int getHP() {
        return hp;
    }

    public int getDamage() {
        return damage;
    }

    public int getLives() {
        return lives;
    }

    public void setSword() {
        this.hasSword = true;
        this.damage = 40;
    }

    public boolean hasSword() {
        return hasSword;
    }

    public void setHasSword(boolean hasSword) {

        this.hasSword = hasSword;

    }

    public void setKey(){
        this.hasKey = true;
    }

    public boolean hasKey(){
        return hasKey;
    }

    public boolean canMove() {
        return canMove;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public void damageDK(int hp) {
        Point2D newPos = super.getPosition().plus(Direction.directionFor(ImageGUI.getInstance().keyPressed()).asVector());
        GameObject obj = currentRoom.getElement(newPos);
        if (obj instanceof DonkeyKong) {
            DonkeyKong dk = (DonkeyKong) obj;
            dk.damageDK(hp);
        }
    }

    public void damageFromBanana() {
        Banana banana = (Banana) currentRoom.getElement(super.getPosition().plus(Direction.directionFor(ImageGUI.getInstance().keyPressed()).asVector()));
        if (banana != null) {
            banana.interact(this);
        }
    }

	public void useBomb(){
        if(hasBomb) {
            Bomb bomb = new Bomb(getPosition(), true);
            currentRoom.addElement(bomb);
            ImageGUI.getInstance().addImage(bomb);
            ImageGUI.getInstance().setStatusMessage("JumpMan used a bomb!");
            hasBomb = false;
        } else ImageGUI.getInstance().setStatusMessage("JumpMan doesn't have a bomb!");
    }

    public boolean hasBomb() {
        return hasBomb;
    }

    public  void setBomb() {
        hasBomb = true;
    }

    public void useFireBall(){
        FireBall fire = new FireBall(getPosition());
        fire.setCurrentRoom(currentRoom);   //AQUI
        currentRoom.addElement(fire);
        ImageGUI.getInstance().addImage(fire);
    }

    public void setCheckPoint(){
        currentRoom.setStartingPosition(1, this.getPosition());
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public boolean getCanMove() {
        return canMove;
    }
}