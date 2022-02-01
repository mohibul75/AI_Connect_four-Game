package application;
	
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;


public class Main extends Application {
	
	private static final int tile_size=80;
	private static final int columns=7;
	private static final int rows= 6;
	
	private Parent createContent() {
		Pane root = new Pane();
		Shape gridShape = makeGrid();
		root.getChildren().add(gridShape);
		root.getChildren().addAll(makeColumns());
		
		return root;
	}
	
	private Shape makeGrid() {
		Shape shape =new Rectangle((columns+1)*tile_size , (rows+1)*tile_size);
		
		for(int y=0 ; y< rows ; y++) {
			for(int x=0 ; x<columns ; x++) {
				Circle circle =new Circle(tile_size/2);
				circle.setCenterX(tile_size/2);
				circle.setCenterY(tile_size/2);
				circle.setTranslateX(x*(tile_size+5)+tile_size/4);
				circle.setTranslateY(y*(tile_size+5)+tile_size/4);
				
				shape=shape.subtract(shape, circle);
			}
		}
		
		
		Light.Distant light=new Light.Distant();
		light.setAzimuth(45.0);
		light.setElevation(30.0);
		
		Lighting lighting = new Lighting();
		lighting.setLight(light);
		lighting.setSurfaceScale(5.0);
		
		shape.setFill(Color.BLUE);
		shape.setEffect(lighting);
		
		
		
		return shape;
	}
	
	private List<Rectangle> makeColumns(){
		List<Rectangle> list = new ArrayList <>();
		
		for(int x=0 ; x< columns ; x++) {
			Rectangle rect = new Rectangle(tile_size,(rows+1)*tile_size);
			rect.setTranslateX(x*(tile_size+5)+tile_size/4);
			rect.setFill(Color.TRANSPARENT);
			rect.setOnMouseEntered(e->rect.setFill(Color.rgb(200, 200, 50,0.3)));
			rect.setOnMouseExited(e->rect.setFill(Color.TRANSPARENT));
			list.add(rect);
		}
		return list;
	}
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setScene(new Scene(createContent()));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
