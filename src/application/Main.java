package application;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
	private GameSetup game = new GameSetup();
	private Pane side = new Pane();
	private int depth=0;
	
	private Parent createContent(int dp) {
		depth=dp;
		Pane root = new Pane();
		//Label my_label=new Label("This is an example of Label"); 
		root.getChildren().add(discRoot);
	//	root.getChildren().add(my_label);  
		Shape gridShape = makeGrid();
		root.getChildren().add(gridShape);
		root.getChildren().addAll(makeColumns());
		
		return root;
	}
	
	private Parent createContentPVP() {
		Pane root = new Pane();
		//Label my_label=new Label("This is an example of Label"); 
		root.getChildren().add(discRoot);
	//	root.getChildren().add(my_label);  
		Shape gridShape = makeGridPVP();
		root.getChildren().add(gridShape);
		root.getChildren().addAll(makeColumnsPVP());
		
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
	
	private Shape makeGridPVP() {
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
	
	private List<Rectangle> makeColumnsPVP(){
		List<Rectangle> list = new ArrayList <>();
		
		for(int x=0 ; x< columns ; x++) {
			Rectangle rect = new Rectangle(tile_size,(rows+1)*tile_size);
			rect.setTranslateX(x*(tile_size+5)+tile_size/4);
			rect.setFill(Color.TRANSPARENT);
			rect.setOnMouseEntered(e->rect.setFill(Color.rgb(200, 200, 50,0.3)));
			rect.setOnMouseExited(e->rect.setFill(Color.TRANSPARENT));
			
			final int cl=x;
			//if(x%2==0) 
				rect.setOnMouseClicked(e->placeDiscPVP(new Disc(redMove),cl));
			list.add(rect);
		}
		return list;
	}
	
	private void placeDiscPVP(Disc disc , int cl) {
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
	
	private void placeDisc(Disc disc , int cl) {
		int rw =rows-1;
		do {
			if(!getDisc(cl,rw).isPresent())
				break;
			rw--;
		}while(rw>=0);
		
		if(rw<0) return;
		grid[cl][rw]=disc;
		
		game.setBoardP(rw,cl);
		
		System.out.println("p	"+cl+"	"+rw);
		discRoot.getChildren().add(disc);
		disc.setTranslateX(cl*(tile_size+5)+tile_size/4);
		final int currentRow =rw;
		
		TranslateTransition animation = new TranslateTransition(Duration.seconds(1),disc);
		animation.setToY(rw * (tile_size + 5) + tile_size / 4);
		animation.setOnFinished(e->{
			if(gameEnded(cl,currentRow)) {
				gameOver();
			}
			redMove=!redMove;
		});
		animation.play();
		
		/*try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		disc= new Disc(!redMove);
		int cl1=(int) game.minimax(game.board, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true)[0];
		 rw =rows-1;
			do {
				if(!getDisc(cl1,rw).isPresent())
					break;
				rw--;
			}while(rw>=0);
			
			if(rw<0) return;
			System.out.println("aI	"+rw+"	"+cl1);
			
			if(cl1>=0) {
				grid[cl1][rw]=disc;
				
				game.setBoardAI(rw,cl1);
				discRoot.getChildren().add(disc);
				disc.setTranslateX(cl1*(tile_size+5)+tile_size/4);
				final int currentRow2 =rw;
				
				 animation = new TranslateTransition(Duration.seconds(3),disc);
				animation.setToY(rw * (tile_size + 5) + tile_size / 4);
				animation.setOnFinished(e->{
					if(gameEnded(cl1,currentRow2)) {
						gameOver();
					}
					redMove=!redMove;
				});
				animation.play();
			}
			
			
			game.print_board(game.board);
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
		
		StringWriter sw = new StringWriter();
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		
		
		if(redMove) {
			if(depth==0)
			alert.setHeaderText("Player 1 Win");
			else  alert.setHeaderText("Player  Win");
			alert.getDialogPane().setExpandableContent(new ScrollPane(new TextArea(sw.toString())));
			alert.show();
		}
		
		else {
			if(depth==0)
				alert.setHeaderText("Player 2 Win");
				else  alert.setHeaderText("AI Win");
			alert.getDialogPane().setExpandableContent(new ScrollPane(new TextArea(sw.toString())));
			alert.show();
		}
	}
	
	
	@Override
	public void start(Stage primaryStage) {
		
		
		StackPane layout2=new StackPane();
		Button button3=new Button("Easy (Vs AI)");
		button3.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
		button3.setPrefSize(300, 50);
		button3.setOnAction(e->primaryStage.setScene(new Scene(createContent(3))));
		
		Button button4=new Button("Medium (Vs AI)");
		button4.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
		button4.setPrefSize(300, 50);
		button4.setOnAction(e->primaryStage.setScene(new Scene(createContent(5))));
		
		Button button5=new Button("Hard (Vs AI)");
		button5.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
		button5.setPrefSize(300, 50);
		button5.setOnAction(e->primaryStage.setScene(new Scene(createContent(8))));
		
		VBox vbox2 = new VBox(5); 
	
		vbox2.getChildren().addAll(button3, button4,button5);
		vbox2.setAlignment(Pos.CENTER);
		
		layout2.getChildren().add(vbox2);
		StackPane.setAlignment(vbox2, Pos.CENTER);
		Scene scene2=new Scene(layout2,600,600);
		
		
		StackPane layout1=new StackPane();
		
		
		
		Label label1=new Label("Connect 4");
		Button button1=new Button("Player Vs Al");
		button1.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
		button1.setPrefSize(300, 50);
		
		Button button2=new Button("Player Vs Player");
		button2.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
		button2.setPrefSize(300, 50);
		
		button1.setOnAction( e->primaryStage.setScene(scene2));
		button2.setOnAction( e->primaryStage.setScene(new Scene(createContentPVP())));
		
		
		VBox vbox = new VBox(5); 
		vbox.getChildren().addAll(button1, button2);
		vbox.setAlignment(Pos.CENTER);
		
		
		
		
		layout1.getChildren().add(vbox);
		// StackPane.setAlignment(vbox, Pos.CENTER);
		Scene scene1=new Scene(layout1,600,600);
		
		
		
		
		
		primaryStage.setScene(scene1);
		primaryStage.show();
		
		/*StackPane layout1=new StackPane();
		layout1.setPadding(new Insets(10));
		
		
		
		Label label1=new Label("Connect 4");
		Button button1=new Button("Player Vs Al");
		Button button2=new Button("Player Vs Player");
		button1.setOnAction(e->primaryStage.setScene(new Scene(createContent())));
		
		
		
		
		layout1.getChildren().add(button1);
		//layout1.getChildren().add(button1);
		//layout1.getChildren().add(button2);
		Scene scene1=new Scene(layout1,600,300);
		
		
		
		
		primaryStage.setScene(scene1);
		primaryStage.show();*/
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

/*   */
