package application;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
import javafx.util.Duration;
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
	
	private boolean redMove=true;
	private Disc [][]grid = new Disc [columns][rows];
	private Pane discRoot=new Pane();
	
	private Parent createContent() {
		Pane root = new Pane();
		root.getChildren().add(discRoot);
		
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
			
			final int cl=x;
			//if(x%2==0) 
				rect.setOnMouseClicked(e->placeDisc(new Disc(redMove),cl));
			list.add(rect);
		}
		return list;
	}
	
	private void placeDisc(Disc disc , int cl) {
		int rw =rows-1;
		do {
			if(!getDisc(cl,rw).isPresent())
				break;
			rw--;
		}while(rw>=0);
		
		if(rw<0) return;
		grid[cl][rw]=disc;
		
		discRoot.getChildren().add(disc);
		disc.setTranslateX(cl*(tile_size+5)+tile_size/4);
		final int currentRow =rw;
		
		TranslateTransition animation = new TranslateTransition(Duration.seconds(0.5),disc);
		animation.setToY(rw * (tile_size + 5) + tile_size / 4);
		animation.setOnFinished(e->{
			if(gameEnded(cl,currentRow)) {
				gameOver();
			}
			redMove=!redMove;
		});
		animation.play();
	}
	
	private boolean gameEnded(int cl , int rw) {
		List<Point2D> vertical =IntStream.rangeClosed(rw-3, rw+3)
				.mapToObj(r->new Point2D(cl,r))
				.collect(Collectors.toList());
		
		
		List<Point2D> horizontal = IntStream.rangeClosed(cl-3, cl+3)
				.mapToObj(c->new Point2D(c,rw))
				.collect(Collectors.toList());
		
		Point2D topLeft= new Point2D(cl-3,rw-3);
		List<Point2D> diagonal1=IntStream.rangeClosed(0, 6).mapToObj(i->topLeft.add(i,i))
				.collect(Collectors.toList());
		
		Point2D botLeft = new Point2D(cl-3,rw+3);
		List<Point2D> diagonal2 = IntStream.rangeClosed(0, 6).mapToObj(i->botLeft.add(i, -i))
				.collect(Collectors.toList());
		
		
		return chechRange(vertical)||chechRange(horizontal)||chechRange(diagonal1)||chechRange(diagonal2);
	}
	
	
	
	private Optional<Disc> getDisc(int cl , int rw){
		if(cl<0||cl>=columns||rw<0||rw>=rows)
			return Optional.empty();
		return Optional.ofNullable(grid[cl][rw]);
	}
	
	private boolean chechRange(List<Point2D> points) {
		
		int chain =0;
		for(Point2D p : points) {
			int cl=(int) p.getX();
			int rw=(int) p.getY();
			
			Disc disc = getDisc(cl,rw).orElse(new Disc(!redMove));
			
			if(disc.isRed()==redMove) {
				chain++;
				if(chain==4) {
					return true;
				}
			} else {
				chain=0;
			}
		}
		
		return false;
	}
	

	
	private void gameOver() {
		System.out.println("Winner"+ (redMove ? "RED" : "YELLOW"));
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
