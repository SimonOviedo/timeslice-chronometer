package gui;

import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Logger;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class VideoArea extends BorderPane {

	// TODO: Comments needed
	private Media media = null;
	private MediaPlayer mediaPlayer = null;
	private MediaView mediaView = null;
	
	private double originalWindowWidth = 0;

	// TODO: Comments needed
	public void play() {
		if (mediaPlayer != null) {
			mediaPlayer.play();
		}
	}
	
	public void pause() {
		if (mediaPlayer != null) {
			mediaPlayer.pause();
		}
	}

	public void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}
	}
	
	public void updateSize() {
		// check if media exists
		if (media == null) {
			return;
		}
		// calc new size
		double windowWidth = TimesliceChronometer.getInstance().getPrimaryStage().getWidth();
    	double windowHeight = TimesliceChronometer.getInstance().getPrimaryStage().getHeight();
    	double scaleX =  (0.75*windowWidth) / media.getWidth();
    	double scaleY = (0.75*windowHeight) / media.getHeight();
    	double scale = Math.min(scaleX, scaleY);
    	double translationX = -(0.25+0.75/2.)*(originalWindowWidth-20) + (0.25+0.75/2.)*windowWidth;
    	// apply new size
    	mediaView.setScaleX(scale);
		mediaView.setScaleY(scale);		
		mediaView.setTranslateX(translationX);
		mediaView.setTranslateY(+20);
	}

	private void loadVideo(File videoFile) {
		// check if no file has been chosen
		if (videoFile == null) {
			return;
		}
		// init video
		String url = null;
		try {
			url = videoFile.toURI().toURL().toString();
		} catch (MalformedURLException e) {
			Logger.getGlobal().warning(e.getLocalizedMessage());
		}
		media = new Media(url);
		mediaPlayer = new MediaPlayer(media);
		mediaView = new MediaView(mediaPlayer);
		// remove open-button
		this.getChildren().clear();
		// arrange and add view
		this.setCenter(mediaView);
		originalWindowWidth = TimesliceChronometer.getInstance().getPrimaryStage().getWidth();
		// position video when loaded
		mediaPlayer.setOnReady(new Runnable() {    
            @Override
            public void run() {
            	updateSize();  
            }
        });
	}
	
	/**
	 * Initialize Area
	 */
	public VideoArea() {
		// table styling
		this.setPadding(new Insets(10, 10, 10, 10));
		// add open-button
		Button openButton = new Button("Open Video");
		openButton.setOnAction((event) -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			Stage parentWindow = TimesliceChronometer.getInstance().getPrimaryStage();
			File videoFile = fileChooser.showOpenDialog(parentWindow);
			loadVideo(videoFile);
		});
		this.setTop(openButton);
	}
}