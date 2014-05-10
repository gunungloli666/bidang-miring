package fjr.bidang.miring;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
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

	double rootWidth = 600;
	double rootHeight = 400;

	double xBidangMiring;
	double yBidangMiring;
	
	double coSudutBidangMiring; 
	
	Button playAnimasiButton; 
	Button resetPosisiButton;
	
	
	// Array koordinat dari Kotak di isi searah jarum jam mulai dari kotak top-left
	double kotakKoordinatX[] = new double[4];
	double kotakKoordinatY[] = new double[4]; 
		

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
		
		primaryStage.show();
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
	 * Di sini kitak menghitung koordinat sudut Kotak.. karena nilai koordinat Kotak 
	 * penentuan-nya agak berbeda dengan penentuan Koordinat bidang miring yang relatif mudah
	 * Maksud saya adalah, yang pertama dihitung adalah titik bottom-left dari kotak
	 * kemudian titik-titik lain menyusul searah jarum jam  
	 * 
	 */
	public void setKotakProperty(){
		// bottom-left
		kotakKoordinatX[3] = xBidangMiring; 
		kotakKoordinatY[3] = yBidangMiring;
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
	
		setKotakProperty();
		
		redrawBidangMiring();
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
		gc.setFill(Color.YELLOW);
		gc.fillPolygon(new double[]{
				kotakKoordinatX[0],
				kotakKoordinatX[1], 
				kotakKoordinatX[2], 
				kotakKoordinatX[3]
		}, new double[]{
				kotakKoordinatY[0], 
				kotakKoordinatY[1], 
				kotakKoordinatY[2], 
				kotakKoordinatX[3]
		}, 4);
		
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
