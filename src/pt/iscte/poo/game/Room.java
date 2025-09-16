package pt.iscte.poo.game;

import objects.*;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.io.File;

public class Room {
	
	private DonkeyKong DK = new DonkeyKong(new Point2D(2,0));
	private JumpMan manel;
	private Banana banana;
	private Bat bat;
	private BadGuy badguy;
	private FireBall fire;
	private Thief thief;
	private Point2D[] jmStartingPositions = new Point2D[100];

	private ArrayList<GameObject> elements = new ArrayList<>();
	private ArrayList<GameObject> originalElements = new ArrayList<>();
	private Point2D position = new Point2D(0,8);

	public Room() {
		manel = new JumpMan(position);
		manel.setCurrentRoom(this);
		DK.setCurrentRoom(this);
		displayRooms();
	}

	public void displayRooms() {
		ImageGUI.getInstance().addImages(elements);
	}

	public void moveManel() {
		manel.move();
	}

	public void moveDK() {
		DK.move();
	}

	public GameObject getElement(Point2D position) {
		for (GameObject object : elements) {
			if (object.getPosition().equals(position)) {
				return object;
			}
		}
		return null;
	}

	public ArrayList<GameObject> getElementsAt(Point2D position) {
		ArrayList<GameObject> objectsAtPosition = new ArrayList<>();
		for (GameObject object : elements) {
			if (object.getPosition().equals(position)) {
				objectsAtPosition.add(object);
			}
		}
		return objectsAtPosition;
	}

	public ArrayList<GameObject> getElements() {
		return elements;
	}

	public void addElement(GameObject obj) {
        elements.add(obj);
        ImageGUI.getInstance().addImage(obj); 
		ImageGUI.getInstance().update();
		System.out.println("Added element to room: " + obj.getName() + " at " + obj.getPosition());
    }
	
	public void setManel(JumpMan manel) {
		this.manel = manel;
		manel.setCurrentRoom(this);
	}
	
	public JumpMan getManel() {
		return manel;
	}

    public DonkeyKong getDK(){
        return DK;
    }

	public List<DonkeyKong> getDonkeyKongs() {
        ArrayList<DonkeyKong> donkeyKongs = new ArrayList<>();
        for (GameObject element : elements) {
            if (element instanceof DonkeyKong) {
                donkeyKongs.add((DonkeyKong) element);
            }
        }
        return donkeyKongs;
    }

    public void setDK(DonkeyKong dk){
        this.DK = dk;
        DK.setCurrentRoom(this);
    }

	public Banana getBanana() {
		return banana;
	}

	public void setBanana(Banana banana) {
		this.banana = banana;
		elements.add(banana);
		ImageGUI.getInstance().addImage(banana);
		ImageGUI.getInstance().update();
	}

	public Bat getBat() {
		return bat;
	}

	public void setBat(Bat bat) {
		this.bat = bat;
		elements.add(bat);
		bat.setCurrentRoom(this);
	}

	public BadGuy getBadGuy (){
		return badguy;
	}

	public void setBadGuy(BadGuy badguy){
		this.badguy=badguy;
		elements.add(badguy);
		badguy.setCurrentRoom(this);
	}

	public Thief getThief(){
		return thief;
	}

	public void setThief(Thief thief){
		this.thief = thief;
		elements.add(thief);
		thief.setCurrentRoom(this);
	}

	public void setFireball(FireBall fire){
		this.fire =fire;
		elements.add(fire);
		ImageGUI.getInstance().addImage(fire);
		ImageGUI.getInstance().update();
	}

	public FireBall getFireBall(){
		return fire;
	}

    public void setStartingPosition(int index, Point2D position) {
        if (index >= 0 && index < jmStartingPositions.length) {
            jmStartingPositions[index] = position;
            this.position = position;
        } else {
            throw new ArrayIndexOutOfBoundsException("Index " + index + " out of bounds for length " + jmStartingPositions.length);
        }
    }

    public Point2D getStartingPosition() {
        return position;
    }

    public void removeObjects() {
        Iterator<GameObject> iterator = elements.iterator();
        while (iterator.hasNext()) {
            GameObject obj = iterator.next();
            if (obj.willDie()) {
                iterator.remove();
                ImageGUI.getInstance().removeImage(obj);
                System.out.println("Removed object from room: " + obj.getName() + " at " + obj.getPosition());
            }
        }
    }

	public void removeObject(GameObject obj) {
		Iterator<GameObject> iterator = elements.iterator();
        while (iterator.hasNext()) {
            GameObject element = iterator.next();
            if (element.equals(obj)) {
                iterator.remove();
                ImageGUI.getInstance().removeImage(obj);
                System.out.println("Removed object from room: " + obj.getName() + " at " + obj.getPosition());
                break;
            }
        }
	}

	public void setOriginalElements() {
		originalElements.clear();
		for (GameObject obj : elements) {
			originalElements.add(obj);
		}

		GameObject obj = new JumpMan(position);
		if (obj instanceof JumpMan) {
			ImageGUI.getInstance().removeImage(obj);
			System.out.println("JumpMan instance at: " + obj.getPosition());
		}
	}

	public void setRoom() {
		elements.clear();
		for(GameObject obj : originalElements) {
			elements.add(obj);
		}
	}	

    public void processBombTicks() {
        for (GameObject obj : elements) {
            if (obj instanceof Bomb) {
                ((Bomb) obj).tick();
            } else if (obj instanceof Explosion) {
                ((Explosion) obj).tick();
            }
        }
    }
	
	public void resetManel() {
		if (manel != null) {
			elements.remove(manel);
			ImageGUI.getInstance().removeImage(manel);
			System.out.println("Manel removed: " + manel.getPosition());
			manel = null;
		}
	}

	public String checkFileExists(String filename) {
	    File file = new File(filename);
	    while (!file.exists()) {
	        filename = ImageGUI.getInstance().showInputDialog("File not found", "Please enter the correct filename:");
	        file = new File(filename);
	    }
	    return filename;
	}

	public String checkFolderExists(String folderName) {
	    File folder = new File(folderName);
	    while (!folder.exists() || folder.listFiles().length == 0) {
	        folderName = ImageGUI.getInstance().showInputDialog("Folder not found or empty", "Please enter the correct folder name:");
	        folder = new File(folderName);
	    }
	    return folderName;
	}
}