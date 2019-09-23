package sample;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

  private TextArea ta = new TextArea();
  private Button btShowJobs = new Button("Show Records");
  private ComboBox<String> cboTableName = new ComboBox<>();

  private Statement stmt;

  @Override
  public void start(Stage primaryStage) {
    //establish the database connection
    initializeDB();
    //display the JOB Data
    btShowJobs.setOnAction(e -> showData());
    HBox hBox = new HBox(10);
    hBox.getChildren().addAll(new Label("Table Name"), cboTableName, btShowJobs);
    hBox.setAlignment(Pos.CENTER);

    BorderPane bpane = new BorderPane();
    bpane.setCenter(new ScrollPane(ta));
    bpane.setTop(hBox);

    Scene scene = new Scene(bpane, 420, 180);
    primaryStage.setTitle("Display JOB Information");
    primaryStage.setScene(scene);
    primaryStage.show();
  }//end method start

  private void initializeDB() {

//Add code that does the following
//Create a connection to your Oracle database using the orcluser account
    final String JDBC_DRIVER = "org.h2.Driver";
    final String DB_URL = "jdbc:h2:./res/HR";

    //  Database credentials
    final String USER = "";
    final String PASS = "";
    Connection conn = null;

//Use the connection to create a statement
//Use the Database MetaData to generate a resultSet based on tables that
    //contain the word job
    //Add the returned table names to the comboBox, selecting the first item
    try {
      // STEP 1: Register JDBC driver
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      stmt = conn.createStatement();

      DatabaseMetaData dbmd = conn.getMetaData();
      ResultSet rsTables = dbmd.getTables(null, null, "JOB%", new String[]{"TABLE"});
      while (rsTables.next()) {
        cboTableName.getItems().add(rsTables.getString("TABLE_NAME"));
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    }//end try catch
  }//end method initializeDB

  private void showData() {
    ta.clear();
    String tableName = cboTableName.getValue();
    try {
      String sql = "SELECT * FROM " + tableName;
      ResultSet rs = stmt.executeQuery(sql);
      ResultSetMetaData rsmd = rs.getMetaData();
      int numberOfColumns = rsmd.getColumnCount();

      for (int i = 1; i <= numberOfColumns; i++) {
        ta.appendText(rsmd.getColumnName(i) + "\t");
      }
      ta.appendText("\n");
      while (rs.next()) {
        for (int i = 1; i <= numberOfColumns; i++) {
          ta.appendText(rs.getString(i) + "\t");
        }
        ta.appendText("\n");
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }//end try catch
  }//end method showData

  public static void main(String[] args) {
    launch(args);
  }//end method main
}//end class DisplayJobs

