package application;

import javafx.scene.paint.Color;

import javafx.scene.shape.Circle;

public class Disc extends Circle {
	private static final int tile_size=80;
	private final boolean red;
	
	public Disc(boolean red) {
		super(tile_size/2, red ? Color.RED : Color.YELLOW );
		this.red = red;
		setCenterX(tile_size/2);
		setCenterY(tile_size/2);
		
	}

	public boolean isRed() {
		return red;
	}
	
	

}
