package pt.iscte.poo.game;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import pt.iscte.poo.utils.Point2D;
import java.awt.event.KeyEvent;
import objects.*;

public class GameEngine implements Observer {

	private ArrayList<Room> rooms = new ArrayList<>();
	private JumpMan JP;
	private Room currentRoom = new Room();
	private int lastTickProcessed = 0;
	private static GameEngine INST;

	private LeaderBoard leaderBoard;
	private int restartTime;
	private String playerName;
	
	public GameEngine() {

		INST = this;
		loadRooms();
		leaderBoard = new LeaderBoard();

		updateRoomImages();
		ImageGUI.getInstance().update();
	}

	@Override
	public void update(Observed source) {

		if(playerName== null || playerName.trim().isEmpty()) {
			playerName = ImageGUI.getInstance().showInputDialog(playerName, "Enter your name: ");
		}

		if (ImageGUI.getInstance().wasKeyPressed()) {
			int k = ImageGUI.getInstance().keyPressed();
			if (Direction.isDirection(k)) {
				Point2D nextPos = currentRoom.getManel().getPosition().plus(Direction.directionFor(k).asVector());
				GameObject nextElement = currentRoom.getElement(nextPos);
				
				if (nextElement instanceof Door) 
					if(currentRoom.getManel().hasKey() == true){
						nextLvl();
					}else if(!currentRoom.getManel().hasKey() && currentRoom.getElements().stream().noneMatch(element -> element instanceof Key)){
						nextLvl();
					}else{
						ImageGUI.getInstance().setStatusMessage( "You need a key to open the door");
					}

				currentRoom.moveManel();

				if (nextElement instanceof Interactable) handleInteraction(nextElement);
			}
			if (k == KeyEvent.VK_B) {
				currentRoom.getManel().useBomb();
			}
			if(k == KeyEvent.VK_F){
				currentRoom.getManel().useFireBall();
			}
		}
		int t = ImageGUI.getInstance().getTicks();
		while (lastTickProcessed < t) {
			processTick();
		}
		ImageGUI.getInstance().update();
	}

	private void loadRooms() {
		String folderName = "rooms";
		folderName = currentRoom.checkFolderExists(folderName);
		File folder = new File(folderName);
		File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));
    
		if (listOfFiles != null) {
			Arrays.sort(listOfFiles, (file1, file2) -> file1.getName().compareTo(file2.getName()));
			
			for (File file : listOfFiles) {
				Room room = new Room();
				readRoomFile(room, file.getName());
				room.setOriginalElements();
				rooms.add(room);
			}
		}

		currentRoom = rooms.get(0);
		currentRoom.setManel(JP);
		JP.setCurrentRoom(currentRoom);
		JP.setPosition(currentRoom.getStartingPosition());
	}


	private void readRoomFile(Room room, String filename) {
	    try {
	        filename = room.checkFileExists("rooms/" + filename);
	        Scanner sc = new Scanner(new File(filename));
	        sc.nextLine();
	        for (int i = 0; i < 10; i++) {
	            if (sc.hasNextLine()) {
	                String line = sc.nextLine();
	                for (int j = 0; j < 10; j++) {
	                    Point2D position = new Point2D(j, i);
	                    if (j < line.length()) {
	                        char c = line.charAt(j);
	                        if (isValidCharacter(c)) {
	                            addTile(room, c, position, rooms.size());
	                        } else {
	                            ImageGUI.getInstance().showMessage("Warning", "Unknown character '" + c + "' at line " + (i + 1) + ", position " + (j + 1) + " in file: " + filename);
	                            room.getElements().add(new Floor(position));
	                        }
	                    } else {
	                        ImageGUI.getInstance().showMessage("Warning", "Missing character at line " + (i + 1) + ", position " + (j + 1) + " in file: " + filename);
	                        room.getElements().add(new Floor(position));
	                    }
	                }
	            } else {
	                ImageGUI.getInstance().showMessage("Error", "Missing line in file: " + filename);
	                ImageGUI.getInstance().dispose();
	                System.exit(1);
	            }
	        }
	        room.getElements().add(room.getManel());
	        sc.close();
	    } catch (FileNotFoundException e) {	
	        e.printStackTrace();
	    }
	    room.getManel().setPosition(room.getStartingPosition());
	}

	private boolean isValidCharacter(char c) {
		return "WSsHtGP0mbBTgkCZh ".indexOf(c) != -1;
	}

	public void addTile(Room room, char c, Point2D position, int roomIndex){

		switch (c) {
			case 'W':
				room.getElements().add(new Wall(position));
				break;
			case ' ':
				room.getElements().add(new Floor(position));
				break;
			case 'S':
				room.getElements().add(new Stairs(position));
				break;
			case 's':
				room.getElements().add(new Sword(position));
				room.getElements().add(new Floor(position));
				break;
			case 'H':
				room.getElements().add(new Floor(position));
				if (JP == null) {
					JP = new JumpMan(position);
				} else {
					JP.setPosition(position);
				}
				room.setManel(JP);
				room.setStartingPosition(roomIndex, position);
				break;
			case 't':
				room.getElements().add(new Trap(position));
				break;
			case 'G':
				DonkeyKong dk = new DonkeyKong(position);
				dk.setCurrentRoom(room);
				room.getElements().add(dk);
				room.getElements().add(new Floor(position));
				break;
			case 'P':
				room.getElements().add(new Princess(position));
				room.getElements().add(new Floor(position));
				break;
			case '0':
				room.getElements().add(new Door(position));
				break;
			case 'm':
				room.getElements().add(new Beef(position));
				room.getElements().add(new Floor(position));
				break;
			case 'b':
				Bat bat = new Bat(position);
				bat.setCurrentRoom(room);
				room.getElements().add(new Floor(position));
				room.getElements().add(bat);
				break;
			case 'B':
				room.getElements().add(new Floor(position));
				room.getElements().add(new Bomb(position, false));
				break;
			case 'T':
				room.getElements().add(new HiddenTrap(position));
				break;
			case 'g':
				BadGuy badguy = new BadGuy(position);
				badguy.setCurrentRoom(room);
				room.getElements().add(new Floor(position));
				room.getElements().add(badguy);
				break;
			case 'k':
				room.getElements().add(new Key(position));
				room.getElements().add(new Floor(position));
				break;
			case 'C':
				room.getElements().add(new Flag(position));
				room.getElements().add(new Floor(position));
				room.setStartingPosition(roomIndex, position);
				break;
			case 'Z':
				room.getElements().add(new Scaffolding(position));
				break;
			case 'h':
				Thief thief = new Thief(position);
				thief.setCurrentRoom(room);
				room.getElements().add(new Floor(position));
				room.getElements().add(thief);
				break;
		}
	}

	private void processTick() {
		System.out.println("Tic Tac : " + lastTickProcessed);
		for (GameObject obj : new ArrayList<>(currentRoom.getElements())) {
			if (obj instanceof Movable) {
				((Movable) obj).move();
			}
			if (obj instanceof Beef) {
				((Beef) obj).age();
			}
			if (obj instanceof DonkeyKong) {
				((DonkeyKong) obj).spawnBanana();
				}
			if (obj instanceof Bomb) {
				Bomb bomb = (Bomb) obj;
				bomb.tick();
			}
			// if(obj instanceof Wall){
			// 	Wall wall = (Wall) obj;
			// 	if(currentRoom.getManel().getPosition().equals(wall.getPosition()))
			// 		wall.wallToGrass();
			// 	wall.wallToGrass();
			// }
		}
		currentRoom.removeObjects();
		currentRoom.processBombTicks();
		fall();
		lastTickProcessed++;
	}

	public void fall() {
		Point2D belowPos = currentRoom.getManel().getPosition().plus(Direction.DOWN.asVector());
		GameObject belowObj = currentRoom.getElement(belowPos);
		if (belowObj != null && (belowObj instanceof Floor || belowObj instanceof Trap || belowObj instanceof HiddenTrap || belowObj instanceof Banana )) {
			currentRoom.getManel().setPosition(belowPos);
			handleInteraction(belowObj);
		}
	}

	public void handleInteraction(GameObject obj) {
        if (obj == null) return;

        JumpMan manel = currentRoom.getManel();
		DonkeyKong DK = currentRoom.getDK();

		if (obj instanceof Princess) {
			leaderBoard.addScore(playerName, getTotalTime());
			ImageGUI.getInstance().showMessage("Congratulations", leaderBoard.getScores());
			ImageGUI.getInstance().dispose();
		} else if (obj instanceof Interactable) {
			((Interactable) obj).interact(manel);
		}else if (obj instanceof DonkeyKong){
			((Interactable) obj).interact(DK);
		}
    }

	public int getTotalTime(){
		return lastTickProcessed-restartTime;
	   }

	public Room getCurrentRoom() {
		return currentRoom;
	}

	public void nextLvl() {
		if (rooms.indexOf(currentRoom) < rooms.size() - 1) {
			int nextRoomIndex = rooms.indexOf(currentRoom) + 1;
			currentRoom = rooms.get(nextRoomIndex);
			JP.setPosition(currentRoom.getStartingPosition());
			currentRoom.setManel(JP);
			JP.setCurrentRoom(currentRoom);
			updateRoomImages();
		}
	}

	public int getCurrentLevel() {
		return rooms.indexOf(currentRoom) + 1;
	}

	public static GameEngine getInstance() {
		if(INST == null)
			return new GameEngine();
		return INST;
	}

	private void updateRoomImages() {
		ImageGUI.getInstance().clearImages();
		ImageGUI.getInstance().addImages(currentRoom.getElements());
		ImageGUI.getInstance().addImage(JP);
	}

	public void reset() {
	    rooms.clear();
	    ImageGUI.getInstance().removeImage(JP);
	    ImageGUI.getInstance().clearImages();
		currentRoom.resetManel();
	    loadRooms();
	    JP = new JumpMan(currentRoom.getStartingPosition());
	    currentRoom.setManel(JP);
	    JP.setCurrentRoom(currentRoom);
	    JP.setHP(100);
	    JP.setLives(2);
	    updateRoomImages();
	    ImageGUI.getInstance().update();
	}
}

//nunca verifica niveis com outros nomes então nunca dá a exceção1
