package application;
	
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;


public class Main extends Application {
	private Desktop desktop = Desktop.getDesktop();
	 
    @Override
    public void start(Stage primaryStage) throws Exception {
 
        final FileChooser fileChooser = new FileChooser();
 
        TextArea textArea = new TextArea();
        textArea.setMinHeight(70);
        Button button1 = new Button("Select One File and Open");
        Button buttonM = new Button("Select Multi Files");
        MenuBar menubar = new MenuBar();  
        Menu fileMenu = new Menu("File");  
        MenuItem filemenu1=new MenuItem("new");  
        MenuItem filemenu2=new MenuItem("Save");  
        MenuItem filemenu3=new MenuItem("Exit");  
        Menu editMenu=new Menu("Edit");  
        MenuItem EditMenu1=new MenuItem("Cut");  
        MenuItem EditMenu2=new MenuItem("Copy");  
        MenuItem EditMenu3=new MenuItem("Paste");  
        editMenu.getItems().addAll(EditMenu1,EditMenu2,EditMenu3);  
        fileMenu.getItems().addAll(filemenu1,filemenu2,filemenu3);  
        menubar.getMenus().addAll(fileMenu,editMenu);  
 
        button1.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                textArea.clear();
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    openFile(file);
                    List<File> files = Arrays.asList(file);
                    printLog(textArea, files);
                }
            }
        });
 
        buttonM.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                textArea.clear();
                List<File> files = fileChooser.showOpenMultipleDialog(primaryStage);
 
                printLog(textArea, files);
            }
        });
 
        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(5);
        root.getChildren().addAll(menubar,textArea, button1, buttonM);
        Scene scene = new Scene(root, 400, 200);
        primaryStage.setTitle("JavaFX FileChooser");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
 
    private void printLog(TextArea textArea, List<File> files) {
        if (files == null || files.isEmpty()) {
            return;
        }
        for (File file : files) {
            textArea.appendText(file.getAbsolutePath() + "\n");
        }
    }
 
    private void openFile(File file) {
        try {
            this.desktop.open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    public static void main(String[] args) {
        Application.launch(args);
    }
}
