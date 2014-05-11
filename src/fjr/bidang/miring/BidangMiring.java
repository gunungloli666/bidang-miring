package fjr.bidang.miring;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BidangMiring extends Application {

	double kotakWidth;
	double kotakHeight;

	double bidangMiringWidth;
	double bidangMiringHeight;
	double diagonalBidangMiring;

	double sudutBidangMiring;

	Canvas canvas;
	GraphicsContext gc;

	double rootWidth = 800;
	double rootHeight = 600;

	double xBidangMiring;
	double yBidangMiring;
	
	double coSudutBidangMiring; 
	
	Button playAnimasiButton; 
	Button resetPosisiButton;
	
	
	// Array koordinat dari Kotak di isi searah jarum jam mulai dari kotak top-left
	double kotakKoordinatX[] = new double[4];
	double kotakKoordinatY[] = new double[4]; 
	
	double xKoordinatBidangMiring[] = new double[3]; 
	double yKoordinatBidangMiring[] = new double[3]; 
	
	
	double canvasWidth = 900; 
	double canvasHeight = 900; 
		
	boolean drawForClick = false; // menentukan apakah event mouse berada dalam kotak

	
	// anchor untuk memindahkan bidang miring... 
	double xAnchor;
	double yAnchor;
	
	double xAnchorBidangMiring; 
	double yAnchorBidangMiring; 
	
	boolean isDragged = false ; // untuk mengatasi keterlambatan mouse keterlambatan mouse dragged event..
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
		primaryStage.setScene(new Scene(root, rootWidth, rootHeight));
		canvas = new Canvas(rootWidth, rootHeight);
		gc = canvas.getGraphicsContext2D(); 
		initShape();
		
		calculateShapePosition();
		
		root.getChildren().add(canvas);
		
		HBox box = new HBox(); 
		box.setSpacing(10);
		box.setTranslateX(10); 
		box.setTranslateY(root.getScene().getHeight()- 30);
		
		playAnimasiButton = new Button("PLAY");
		playAnimasiButton.setPrefWidth(100);
		box.getChildren().add(playAnimasiButton); 
		
		resetPosisiButton = new Button("RESET"); 
		resetPosisiButton.setPrefWidth(100);
		box.getChildren().add(resetPosisiButton); 
		root.getChildren().add(box); 
		root.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {				
				if(isInsideTriangle(event.getX(), event.getY())){
					drawForClick = true; 
					xAnchor = event.getX(); 
					yAnchor = event.getY(); 
					xAnchorBidangMiring = xBidangMiring; 
					yAnchorBidangMiring = yBidangMiring; 
				}else{
					drawForClick = false; 
				}
				redrawBidangMiring();
			}
		});
		
		
		root.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent mouseEvent) {
				if(drawForClick){
					changeBidangMiringSize(mouseEvent.getX(), mouseEvent.getY());
				}
			}
		});
		
		root.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				isDragged = false; 
			}
		});
		primaryStage.show();
	}
	
	
	/*
	 * untuk mendeteksi apakah event yang bersangkutan 
	 * berada di dalam are segitiga... 
	 */
	double margin = 10;
	public boolean isInsideTriangle(double x, double y){
		if(x < xBidangMiring - margin)
			return false; 
		if(x > xBidangMiring + bidangMiringWidth +margin)
			return false;
		if(y < yBidangMiring - margin)
			return false; 
		if(y>yBidangMiring + bidangMiringHeight + margin)
			return false; 
		return true; 
	}
	
	public void  changeBidangMiringSize(double x, double y){
		if(x <= xBidangMiring + margin && x >= xBidangMiring - margin){ // kiri
			if(y < yBidangMiring - margin)
				return ; 
			if(y>yBidangMiring + bidangMiringHeight + margin)
				return ; 
			changeXBeginBidangMiring(x);
		}else if(x <= xBidangMiring + bidangMiringWidth + margin 
				&& x>= xBidangMiring + bidangMiringWidth- margin){// kanan
			if(y < yBidangMiring - margin)
				return ; 
			if(y>yBidangMiring + bidangMiringHeight + margin)
				return ; 
			changeXEndBidangMiring(x);
		}else if(y <= yBidangMiring + margin && y >= yBidangMiring - margin){ // atas 
			if(x < xBidangMiring - margin)
				return ; 
			if(x > xBidangMiring + bidangMiringWidth +margin)
				return ;
			changeYBeginBidangMiring(y);
		}else if(y <= yBidangMiring + bidangMiringHeight + margin 
				&& y >= yBidangMiring + bidangMiringHeight - margin){ // bawah 
			if(x < xBidangMiring - margin)
				return ; 
			if(x > xBidangMiring + bidangMiringWidth +margin)
				return ;
			changeYEndBidangMiring(y);
		}else{
			if(isInsideTriangle(x, y)){
//				moveBidangMiring(x, y);
				
			}
		}
	}

	public void initShape() {
		xBidangMiring = 50;
		yBidangMiring = 50;

		kotakWidth = 100;
		kotakHeight = 45;

		bidangMiringWidth = 450;
		bidangMiringHeight = 300;

		setSudutBidangMiring();
	}
	
	/*
	 * yang pertama dihitung adalah titik bottom-left dari kotak
	 * kemudian titik-titik lain menyusul searah jarum jam  
	 * 
	 */
	public void setShapeProperty(){
		
		xKoordinatBidangMiring[0] = xBidangMiring;
		yKoordinatBidangMiring[0] = yBidangMiring; 
		xKoordinatBidangMiring[1] = xBidangMiring + bidangMiringWidth; 
		yKoordinatBidangMiring[1] = yBidangMiring + bidangMiringHeight; 
		xKoordinatBidangMiring[2] = xBidangMiring; 
		yKoordinatBidangMiring[2] = yBidangMiring + bidangMiringHeight; 
		
		
		sudutBidangMiring = Math.atan(Math.abs(bidangMiringHeight)/ Math.abs(bidangMiringWidth));
		
		// bottom-left
		kotakKoordinatX[3] = xKoordinatBidangMiring[0]; 
		kotakKoordinatY[3] = yKoordinatBidangMiring[0];
		//top-left
		kotakKoordinatX[0] = kotakKoordinatX[3] + kotakHeight * Math.sin(sudutBidangMiring); 
		kotakKoordinatY[0] = kotakKoordinatY[3] - kotakHeight * Math.cos(sudutBidangMiring); 
		//top-right
		kotakKoordinatX[1] = kotakKoordinatX[0] + kotakWidth * Math.cos(sudutBidangMiring); 
		kotakKoordinatY[1] = kotakKoordinatY[0] + kotakWidth * Math.sin(sudutBidangMiring); 
		//bottom-right
		kotakKoordinatX[2] = kotakKoordinatX[3] + kotakWidth * Math.cos(sudutBidangMiring); 
		kotakKoordinatY[2] = kotakKoordinatY[3] + kotakWidth * Math.sin(sudutBidangMiring); 
	}

	public void setSudutBidangMiring() {
		sudutBidangMiring = Math.atan(bidangMiringHeight / bidangMiringWidth);
		diagonalBidangMiring = Math.sqrt(bidangMiringHeight
				* bidangMiringHeight + bidangMiringWidth * bidangMiringWidth);
		
		coSudutBidangMiring = Math.PI/ 2.0 - sudutBidangMiring;  // maybe ini bagian dari postulat euclid 
	
		setShapeProperty();
		
		redrawBidangMiring();
	}

	/*
	 * rubah posisi x akhir dari bidang miring
	 */
	public void changeXEndBidangMiring(double x){
		double changeX =  x - xBidangMiring;
//		System.out.println("x change = "+changeX); 
		changeWidthBidangMiring(changeX);
	}

	public void changeYEndBidangMiring(double y){
		double changeY = y - yBidangMiring;
		changeHeightBidangMiring(changeY);
	}
	
	public void changeXBeginBidangMiring(double x){
		double changeX = bidangMiringWidth +  (xBidangMiring - x ); 
		xBidangMiring = x; 
		changeWidthBidangMiring(changeX);
	}

	public void changeYBeginBidangMiring(double y){
		double changeY = bidangMiringHeight + (yBidangMiring - y); 
		yBidangMiring = y;
		changeHeightBidangMiring(changeY);
	}
	
	
	public void changeXBidangMiring(double x){
		xBidangMiring = x; 
		setShapeProperty();
		redrawBidangMiring();
	}
	
	
	public void changeYBidangMiring(double y){
		yBidangMiring = y; 
		setShapeProperty();
		redrawBidangMiring();
	}
	
	public void moveBidangMiring(double x, double y){
		xBidangMiring = xBidangMiring + x; 
		yBidangMiring = yBidangMiring + y;
	}

	public void redrawBidangMiring() {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight()); // kosongkan area gambar
		
		//buat bidang miring dengan warna biru
		gc.setFill(Color.BLUE);
		gc.fillPolygon(
				new double[]{
					xBidangMiring, 
					xBidangMiring, 
					xBidangMiring + bidangMiringWidth
				},
				new double[]{
					yBidangMiring,
					yBidangMiring + bidangMiringHeight, 
					yBidangMiring + bidangMiringHeight
				},
				3);
		
		//buat kotak dengan warna kuning
		gc.setFill(Color.BLACK);
		gc.fillPolygon(new double[]{
				kotakKoordinatX[0],
				kotakKoordinatX[1], 
				kotakKoordinatX[2], 
				kotakKoordinatX[3]
		}, new double[]{
				kotakKoordinatY[0], 
				kotakKoordinatY[1], 
				kotakKoordinatY[2], 
				kotakKoordinatY[3]
		}, 4);
		
		if(drawForClick){
			gc.setStroke(Color.RED);
			gc.setLineWidth(2); 
			gc.strokeRect(xBidangMiring, yBidangMiring, bidangMiringWidth, bidangMiringHeight);
		}
		
	}

	public void changeHeightBidangMiring(double height) {
		bidangMiringHeight = height;
		setSudutBidangMiring();
	}

	public void changeWidthBidangMiring(double width) {
		bidangMiringWidth = width;
		setSudutBidangMiring();
	}

	public void calculateShapePosition(){
		
	}
	
	public void changeWidthKotak(double width) {
		kotakWidth = width;
	}

	public void changeHeightKotak(double height) {
		kotakHeight = height;
	}

	public static double getSudutInDegree(double sudut) {
		return sudut / Math.PI * 180;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
