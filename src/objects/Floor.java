package objects;

import pt.iscte.poo.utils.Point2D;

public class Floor extends GameObject {

	public Floor(Point2D position) {
		super(position);
	}

	@Override
	public String getName() {
		return "Floor";
	}

	@Override
	public int getLayer() {
		return 0;
	}
}
