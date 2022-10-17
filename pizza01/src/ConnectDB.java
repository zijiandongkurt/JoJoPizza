import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.control.*;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.animation.TranslateTransition;

import java.sql.*;
import java.util.*;

public class ConnectDB  extends Application {
    //variables
    static Connection conn;
    static String databaseName = "";
    static String url = "jdbc:mysql://localhost:3306/"+ databaseName;

    static String username = "root";
    static String password = "DZJ007yeah";
    public static Statement stmt;

    //Customer set
    String currentName,currentPass,currentPhoneNumber,currentAddress,currentPostCode,helloString,addCustomer;
    int customerPhoneNum;



    //////
    Stage stage;
    Scene sceneRegister,sceneLogin,sceneMenu,sceneConfirmation,sceneStatus;
    Label hello;
    public static void main (String[]args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        sqlPart();
        launch(args);
//jdbc:mysql://localhost:3306/?user=root
    }
    public static void sqlPart() throws ClassNotFoundException, SQLException {


    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(url,username,password);
        stmt = conn.createStatement();
        stmt.execute("use pizzaSchema");
        String selectMenu = "Select * FROM Menu";
        String selectCustomer = "Select * FROM customer";
        String selectOrder = "Select * FROM Menu";
        String selectRestaurant = "Select * FROM Menu";
        //INSERT INTO `pizzaSchema`.`customer` (`username`, `email`, `password`) VALUES ('112', '22', '22');

        ArrayList<String> meatPizzaInfo = new ArrayList<String>();
        ArrayList<String> veganPizzaInfo = new ArrayList<String>();
        ArrayList<String> sweetInfo = new ArrayList<String>();
        ArrayList<String> drinkInfo = new ArrayList<String>();

   //     ArrayList<Integer> customerID = new ArrayList<Integer>();
   //     ArrayList<String> customerName = new ArrayList<String>();


        PreparedStatement ps = conn.prepareStatement(selectMenu);
        PreparedStatement ps2 = conn.prepareStatement(selectCustomer);
        ResultSet rs =  ps.executeQuery();

        while(rs.next()){
            //todo: continue here
            String name = rs.getString(3);
            Boolean isVegan = rs.getBoolean(8);
            String type = rs.getString((4));

            if(type.equals("Pizza")){
                if(isVegan) veganPizzaInfo.add(name);
                else meatPizzaInfo.add(name);
            }
            else if(type.equals("Sweet")){
                sweetInfo.add(name);
            }
            else if(type.equals("Drink")){
                drinkInfo.add(name);
            }
            double price = rs.getDouble(2);
            System.out.println(type);

        }
        System.out.println(meatPizzaInfo);
        System.out.println();
        System.out.println(veganPizzaInfo);





        VBox layoutRegister = new VBox(22);
        VBox layoutLogin = new VBox(22);
        VBox layoutMenu = new VBox(22);
        VBox layoutConfirmation = new VBox(22);
        VBox layoutStatus = new VBox(22);



        layoutRegister.setAlignment(Pos.CENTER);
        layoutRegister.setSpacing(5);
        sceneRegister = new Scene(layoutRegister,600,600);
        sceneLogin  = new Scene(layoutLogin,600,600);
        sceneMenu = new Scene(layoutMenu,600,600);
        sceneConfirmation = new Scene(layoutConfirmation,600,600);
        sceneStatus = new Scene(layoutStatus,600,600);

        FileInputStream pizzaBack1 = new FileInputStream("Resource/bit2.png");
        Image back1 = new Image(pizzaBack1);
        BackgroundImage BI1 = new BackgroundImage(back1,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background bGround1 = new Background(BI1);

        layoutRegister.setBackground(bGround1);
        layoutLogin.setBackground(bGround1);
        layoutMenu.setBackground(bGround1);
        layoutStatus.setBackground(bGround1);
        layoutConfirmation.setBackground(bGround1);

        TextArea inputName = new TextArea();
        inputName.setMaxSize(200,30);
        inputName.setMinSize(200,30);

        TextArea inputPass = new TextArea();
        inputPass.setMaxSize(200,30);
        inputPass.setMinSize(200,30);
        TextArea inputPhoneNumber = new TextArea();
        inputPhoneNumber.setMaxSize(200,30);
        inputPhoneNumber.setMinSize(200,30);
        TextArea inputAddress = new TextArea();
        inputAddress.setMaxSize(200,30);
        inputAddress.setMinSize(200,30);
        TextArea inputPostcode = new TextArea();
        inputPostcode.setMaxSize(200,30);
        inputPostcode.setMinSize(200,30);

        TextArea logInputName = new TextArea();
        logInputName.setMaxSize(200,30);
        logInputName.setMinSize(200,30);

        TextArea logInputPass = new TextArea();
        logInputPass.setMaxSize(200,30);
        logInputPass.setMinSize(200,30);


        Label restaurantName = new Label ("JoJo's Pizza");
        Label input1 = new Label("Welcome, please register:");
        Label input2 = new Label("I already have account");
        Label input3 = new Label("Enter your name:");
        Label input4 = new Label("Set your password:");
        Label input5 = new Label("Phone Number:");
        Label input6 = new Label("Address:");
        Label input7 = new Label("Postcode:");

        Label logInput3 = new Label("Enter your name:");
        Label logInput4 = new Label("Enter your password:");

        Label meatPizza = new Label("Meat Pizza");
        Label veganPizza = new Label("Vegan Pizza");
        Label sweet = new Label("Want some dessert?");
        Label drink = new Label("Drinks");

        helloString = new String("Hello, ");
        hello = new Label(helloString);
        Button register = new Button("Register!");
        Button login = new Button ("Login");
        Button goRegister = new Button("go to register page");
        Button goLogin = new Button("go to login page");
        Button restart = new Button("restart1");
        Button placeOrder = new Button("place order");
        Button pay = new Button("pay");
        Button backToFirst = new Button("restart2");


        ChoiceBox meatPizzaBox = new ChoiceBox(FXCollections.observableArrayList(meatPizzaInfo));
        ChoiceBox veganPizzaBox = new ChoiceBox(FXCollections.observableArrayList(veganPizzaInfo));
        ChoiceBox sweetBox= new ChoiceBox(FXCollections.observableArrayList(sweetInfo));
        ChoiceBox drinkBox = new ChoiceBox(FXCollections.observableArrayList(drinkInfo));

        String choice0 = ("---Select---");
        meatPizzaBox.setValue(choice0);
        veganPizzaBox.setValue(choice0);
        sweetBox.setValue(choice0);
        drinkBox.setValue(choice0);
        backToFirst.setOnAction(e->{
            stage.setScene(sceneRegister);
        });
        placeOrder.setOnAction(e->{
            stage.setScene(sceneConfirmation);
        });
        pay.setOnAction(e->{
            stage.setScene(sceneStatus);
        });

        restart.setOnAction(e->{
            //todo polish here
            stage.setScene(sceneRegister);
        });

        login.setOnAction(e->{
            // todo: check login information correctness
            //demo:
            currentName = logInputName.getText();
            currentPass = logInputPass.getText();
            boolean success = false;
            //check name and its password:
            try {
                ResultSet rs2 =  ps2.executeQuery();
                while(rs2.next()){
                String name = rs2.getString(1);
                String pass = rs2.getString(2);
                if(logInputName.getText().equals(name)&&logInputPass.getText().equals(pass)){
                    success = true;
                    helloString = "Hello, "+logInputName.getText();
                    System.out.println("Your ID is "+ rs2.getInt(5));
                    hello.setText(helloString);
                    stage.setScene(sceneMenu);

                }
                else System.out.println("Got Problem");
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        });
        goRegister.setOnAction(e-> {
            stage.setScene(sceneRegister);
        });
        goLogin.setOnAction(e->{
            stage.setScene(sceneLogin);
        });
        register.setOnAction(e->{
            currentName = inputName.getText();
            currentPass = inputPass.getText();
            currentPhoneNumber = inputPhoneNumber.getText();
            currentAddress = inputAddress.getText();
            currentPostCode = inputPostcode.getText();


            addCustomer = "INSERT INTO  `pizzaSchema`.`customer` (`username`, `password`, `PhoneNumber`, `address`, `postCode`) VALUES ('" + currentName+"', '"+currentPass+"', '"+currentPhoneNumber+"', '"+currentAddress+"', '"+currentPostCode+"');\n";
            System.out.println(addCustomer);
            try {
                Statement add2 = conn.createStatement();
                add2.execute(addCustomer);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            //INSERT INTO `pizzaSchema`.`customer` (`username`, `email`, `password`) VALUES ('112', '22', '22');
        });

        restaurantName.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 90));


        Color pizzaColor = Color.rgb(253, 242, 219,1);
        sceneRegister.setFill(pizzaColor);
        sceneLogin.setFill(pizzaColor);
        sceneMenu.setFill(pizzaColor);
        sceneConfirmation.setFill(pizzaColor);
        sceneStatus.setFill(pizzaColor);


        layoutRegister.getChildren().addAll(restaurantName,input1,input3,inputName,input4,inputPass,input5,inputPhoneNumber,input6,inputAddress,input7,inputPostcode,register,input2,goLogin);
        layoutLogin.getChildren().addAll(logInput3,logInputName,logInput4,logInputPass,login,goRegister);
        layoutMenu.getChildren().addAll(restart,meatPizza,veganPizza,sweet,drink,meatPizzaBox,veganPizzaBox,sweetBox,drinkBox,hello,placeOrder);
        layoutConfirmation.getChildren().addAll(pay);
        layoutStatus.getChildren().addAll(backToFirst);

        stage.setTitle("JoJo Pizza Online");
        stage.setScene(sceneRegister);
        stage.show();

    }
}
