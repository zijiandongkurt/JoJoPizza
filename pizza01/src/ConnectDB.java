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
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
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
import java.util.concurrent.TimeUnit;

public class ConnectDB  extends Application {
    //variables
    static Connection conn;
    static String databaseName = "";
    static String url = "jdbc:mysql://localhost:3306/"+ databaseName;

    static String username = "root";
    static String password = "DZJ007yeah";
    public static Statement stmt;

    //Customer set
    String currentName,currentPass,currentPhoneNumber,currentAddress,currentPostCode,helloString,addCustomer,currentItem,addCart,deliverManPhoneNumber;
    int currentID ,currentFoodID;
    double currentFoodPrice,currentMoney;
    double sum = 0;
    Boolean havePizza;
    Alert noPizza;
    String cartString = "", DeliveryManName;
    Timestamp nowTime,endTime,cancelTime;
    //////status page
    Label sName,sID,sMoney,sOrderID,sOrderTime,sendTime,sOrderDeliverMan,sCancel,sAddress,sPostCode ,sDeliveryManName,sDPhoneNumber;

    /////
    Stage stage;
    Scene sceneRegister,sceneLogin,sceneMenu,sceneConfirmation,sceneStatus;

    Label hello;
    public static void main (String[]args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        sqlPart();
        launch(args);
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
        String selectDeliveryMan = "Select * FROM DeliveryMan";
        String selectOrder = "Select * FROM Order";
        String selectRestaurant = "Select * FROM Menu";
        String selectCart = "Select * FROM Cart";
        //INSERT INTO `pizzaSchema`.`customer` (`username`, `email`, `password`) VALUES ('112', '22', '22');

        ArrayList<String> meatPizzaInfo = new ArrayList<String>();
        ArrayList<String> veganPizzaInfo = new ArrayList<String>();
        ArrayList<String> sweetInfo = new ArrayList<String>();
        ArrayList<String> drinkInfo = new ArrayList<String>();

   //     ArrayList<Integer> customerID = new ArrayList<Integer>();
   //     ArrayList<String> customerName = new ArrayList<String>();


        PreparedStatement ps = conn.prepareStatement(selectMenu);
        PreparedStatement ps2 = conn.prepareStatement(selectCustomer);
        PreparedStatement ps4 = conn.prepareStatement(selectCart);
        PreparedStatement psMan = conn.prepareStatement(selectDeliveryMan);
        PreparedStatement psOrder = conn.prepareStatement(selectOrder);
        PreparedStatement psRestaurant = conn.prepareStatement(selectRestaurant);

        ResultSet rs =  ps.executeQuery();
        while(rs.next()){
            //todo: continue here
            String name = rs.getString(3);
            Boolean isVegan = rs.getBoolean(7);
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
        layoutLogin.setAlignment(Pos.CENTER);
        layoutLogin.setSpacing(5);
        layoutMenu.setAlignment(Pos.BASELINE_LEFT);
        layoutRegister.setSpacing(1);

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

        TextArea inputCoupon = new TextArea();
        inputCoupon.setMaxSize(200,30);
        inputCoupon.setMinSize(200,30);

        Label restaurantName = new Label ("JoJo's Pizza");
        Label input1 = new Label("Welcome, please register:");
        Label input2 = new Label("I already have account");
        Label input3 = new Label("Enter your name:");
        Label input4 = new Label("Set your password:");
        Label input5 = new Label("Phone Number:");
        Label input6 = new Label("Address:");
        Label input7 = new Label("Postcode:");
        Label cartTitle = new Label("Your cart:");
        Label cartDescription = new Label();
        Label totaPrice = new Label("Total price: 0");
        Label invoice = new Label("Your order detail: ");
        Label invoiceDetail = new Label();
        Label logInput3 = new Label("Enter your name:");
        Label logInput4 = new Label("Enter your password:");
        Label laybelCoupon = new Label("Tips: you can get One discount coupon if you already bought 10 pizzas");
        Label meatPizza = new Label("Meat Pizza");
        Label veganPizza = new Label("Vegan Pizza");
        Label sweet = new Label("Want some dessert?");
        Label drink = new Label("Drinks");

        sName = new Label();
        sID = new Label();
        sMoney = new Label();
        sOrderTime = new Label();
        sendTime = new Label();
        sOrderDeliverMan = new Label();
        sCancel = new Label();
        sAddress = new Label();
        sPostCode = new Label();
        sDeliveryManName = new Label();
        sDPhoneNumber = new Label();

        helloString = new String("Hello, ");
        hello = new Label(helloString);
        Button register = new Button("Register!");
        Button login = new Button ("Login");
        Button goRegister = new Button("go to register page");
        Button goLogin = new Button("go to login page");
        Button restart = new Button("restart1");
        Button placeOrder = new Button("place order");
        Button pay = new Button("pay");
        Button backToFirst = new Button("log out");
        Button addItem = new Button("Add item");
        Button checkStatus  = new Button("My status");
        Button applyCoupon = new Button("Apply coupon");
        Button cancel = new Button("Cancel");

        ChoiceBox meatPizzaBox = new ChoiceBox(FXCollections.observableArrayList(meatPizzaInfo));
        ChoiceBox veganPizzaBox = new ChoiceBox(FXCollections.observableArrayList(veganPizzaInfo));
        ChoiceBox sweetBox= new ChoiceBox(FXCollections.observableArrayList(sweetInfo));
        ChoiceBox drinkBox = new ChoiceBox(FXCollections.observableArrayList(drinkInfo));

        String choice0 = ("---Select---");
        meatPizzaBox.setValue(choice0);
        veganPizzaBox.setValue(choice0);
        sweetBox.setValue(choice0);
        drinkBox.setValue(choice0);

        meatPizzaBox.setOnAction(e->{
        String currentMP = meatPizzaBox.getValue().toString();
            currentItem=currentMP;

        });
        veganPizzaBox.setOnAction(e->{
            String currentVP = veganPizzaBox.getValue().toString();
            currentItem=currentVP;

        });
        sweetBox.setOnAction(e->{
            String currentS = sweetBox.getValue().toString();
            currentItem=currentS;

        });
        drinkBox.setOnAction(e->{
            String currentD = drinkBox.getValue().toString();
            currentItem=currentD;

        });
        applyCoupon.setOnAction(e->{
            if(inputCoupon.getText().equals("Pizza123E")) sum *=0.9;
            double roundOff = Math.floor(sum*100) / 100d;
            totaPrice.setText("Total price: "+roundOff + "  (9% VAT inclusive)");
            applyCoupon.setDisable(true);
        });
        havePizza = false;
        addItem.setOnAction(e->{
          //   addCart = "---------------\n";
            try {
                ResultSet rs3 =  ps.executeQuery();
                while(rs3.next()){
                    if(currentItem.equals(rs3.getString(3))){
                        if(rs3.getString(4).equals("Pizza")){
                            havePizza=true;
                            String selectCustomer2 = "Select * FROM customer";
                            PreparedStatement ps22 = conn.prepareStatement(selectCustomer2);

                            ResultSet rsC = ps22.executeQuery();
                            while(rsC.next()){
                                if(rsC.getInt(5)==currentID){
                                    String addCredit = "UPDATE `pizzaSchema`.`customer` SET `CouponCredit` = '"+(rsC.getInt(9)+1)+"' WHERE (`ID` = '"+currentID+"');\n";
                                    conn.createStatement().execute(addCredit);
                                }
                            }


                        }
                        currentFoodID = rs3.getInt(1);
                        currentFoodPrice = rs3.getDouble(6);
                        cartString += rs3.getString(3)+ currentFoodPrice+", " ;
                   //     cartString += "\n";
                        sum +=currentFoodPrice;
                        /*
                        boolean hasItem = false;
                        ResultSet rs4 = ps4.executeQuery();
                        while(rs4.next()){
                            if(rs4.getInt(2)==currentID) hasItem =true;
                        }
                        if (hasItem){
                        }
*/
                        addCart = "INSERT INTO  `pizzaSchema`.`Cart` (`customerID`, `foodID`) VALUES ('" + currentID+"', '"+currentFoodID+"');\n";

                        Statement addItemQuary = conn.createStatement();
                        addItemQuary.execute(addCart);
                        ArrayList<String> cartString1= new ArrayList<String>();
                        ResultSet rs4 = ps4.executeQuery();

                        };
                }
                double roundOff = Math.floor(sum*100) / 100d;
                totaPrice.setText("Total price: "+roundOff + "  (9% VAT inclusive)");
                cartDescription .setText(cartString);

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        });
        cancel.setOnAction(e->{
            layoutStatus.getChildren().removeAll(sName,sID,sMoney,sAddress,sPostCode,sOrderTime,sCancel,sendTime,sDeliveryManName,sDPhoneNumber,cancel);
            Label canceled = new Label("Your order is being canceled");
            layoutStatus.getChildren().addAll(canceled);
        });

        backToFirst.setOnAction(e->{
            layoutStatus.getChildren().remove(cancel);
            layoutStatus.getChildren().addAll(sName,sID,sMoney,sAddress,sPostCode,sOrderTime,sCancel,sendTime,sDeliveryManName,sDPhoneNumber,cancel);

            havePizza = false;
            cartDescription.setText("");
            sum=0;
            currentFoodPrice=0;
            stage.setScene(sceneRegister);
        });
        placeOrder.setOnAction(e->{
            if(havePizza){
                stage.setScene(sceneConfirmation);
                invoiceDetail.setText(cartDescription.getText());

                cartString = "Your order detail: \n";
            }
            else{
                noPizza = new Alert(Alert.AlertType.WARNING);
                noPizza.setHeaderText("No Pizza Selected");
                noPizza.setContentText("You have to add at least one Pizza to place the order");
                noPizza.show();
            }

        });
        pay.setOnAction(e->{
      //      sName.setText(currentName);
       //     sID.setText(currentID+"");
       //     sMoney.setText(currentMoney+"");
            final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            currentMoney -= sum;

         nowTime = new Timestamp(System.currentTimeMillis());
         cancelTime = new Timestamp(nowTime.getTime() + TimeUnit.MINUTES.toMillis(5));
         endTime = new Timestamp(nowTime.getTime() + TimeUnit.MINUTES.toMillis(40));



                sName.setText("Name: "+ currentName);
         sID.setText("ID: "+currentID);
         sMoney.setText("Balance "+Math.floor(currentMoney*100) / 100d);
         sOrderTime.setText("You made your order at "+ sdf3.format(nowTime));
         sCancel.setText("You can cancel your order before "+sdf3.format(cancelTime));
         sendTime.setText("The order will arrive at "+endTime);
         sAddress.setText("Your address is "+currentAddress);
         sPostCode.setText("Post code: " + currentPostCode);



            applyCoupon.setDisable(false);
            laybelCoupon.setText("Tips: you can get One discount coupon if you already bought 10 pizzas");
        int DeliveryMan=-1;

            String changeBalance = "UPDATE `pizzaSchema`.`Customer` SET `Money` = '"+currentMoney+"' WHERE (`ID` = '"+currentID+"');\n";
            try {
                conn.createStatement().execute(changeBalance);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            try {
                ResultSet rsMan = psMan.executeQuery();
                Boolean stop = false;
                while(rsMan.next()){
                    if (rsMan.getInt(2)==1&&stop==false){
                        System.out.println("WO Honzg la   ");
                        DeliveryMan=rsMan.getInt(1);
                        DeliveryManName = rsMan.getString(3);
                        System.out.println(DeliveryManName);
                        deliverManPhoneNumber = rsMan.getString(4);
                        System.out.println(deliverManPhoneNumber);
                        sDeliveryManName.setText("Your delivery man is: "+ DeliveryManName);
                        sDPhoneNumber.setText("You can call the delivery man with "+ deliverManPhoneNumber);

                        //                        UPDATE `pizzaSchema`.`deliveryMan` SET `Avaliable` = '0' WHERE (`ID` = '9527');
            String changeStatus = "UPDATE `pizzaSchema`.`deliveryMan` SET `Avaliable` = '0' WHERE (`ID` = '"+rsMan.getInt(1)+"');\n";
                        if(new Timestamp(System.currentTimeMillis()).after(endTime)){
                             changeStatus = "UPDATE `pizzaSchema`.`deliveryMan` SET `Avaliable` = '1' WHERE (`ID` = '"+rsMan.getInt(1)+"');\n";  //reset the Delivery man
                        }

                            Statement changeStatusQuary = conn.createStatement();
                        changeStatusQuary.execute(changeStatus);
                        stop =true;
                    }
                }

                // Warning there is no avliable DeliveryMan.
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            //INSERT INTO `pizzaSchema`.`Order` (`DelieverymanID`, `CustomerID`, `FoodPrice`, `create_time`) VALUES ('9527', '1', '22', '');
            String insertOrder = "INSERT INTO `pizzaSchema`.`Order` (`DelieverymanID`, `CustomerID`, `FoodPrice`, `create_time`) VALUES ('"+DeliveryMan+"', '"+currentID+"', '"+sum+"', '"+nowTime+ " ' );\n";
           // String nowtime = "SELECT CURRENT_TIMESTAMP;\n";
      //      Timestamp orderStart =


            try {
                Statement addOrderQuary = conn.createStatement();
                addOrderQuary.execute(insertOrder);

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }




            cartString = "";
            stage.setScene(sceneStatus);


            try {
                ResultSet rsStatus = ps2.executeQuery();
                while(rsStatus.next()){
                    if(currentID==rsStatus.getInt(5));
                    if(rsStatus.getInt(9)>=10){
                        //UPDATE `pizzaSchema`.`customer` SET `CouponCredit` = '222' WHERE (`ID` = '1');
                        String str = "UPDATE `pizzaSchema`.`customer` SET `CouponCredit` = '" + (rsStatus.getInt(9)-10) + "' WHERE (`ID` = '"+currentID+"');\n";
                        String addCoupon = "UPDATE `pizzaSchema`.`customer` SET `Coupon` = '" + "Pizza123E"+ "' WHERE (`ID` = '"+currentID+"');\n";

                        laybelCoupon.setText("Your discount Code: Pizza123E");
                        conn.createStatement().execute(str);
                        conn.createStatement().execute(addCoupon);
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            Timer timer1 = new Timer();
         //   System.out.println(timer1);
            // SELECT DATEDIFF(MINUTE, '11:10:10' , '11:20:00') AS MinuteDiff

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
                    currentID=rs2.getInt(5);
                    currentMoney = rs2.getDouble(10);
                    helloString = "Hello, "+logInputName.getText()+", Your balance is: "+ Math.floor(currentMoney*100) / 100d ;
                    System.out.println("Your ID is "+ currentID);
                    hello.setText(helloString);
                    stage.setScene(sceneMenu);
                }
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
        checkStatus.setOnAction(e->{
        });

        restaurantName.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 70));
        Color c1 =  Color.rgb(255, 170, 0,1);
        restaurantName.setTextFill(c1);
        Font dFont = Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20);
        Font dFont2 = Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 14);
        layoutRegister.setSpacing(5);
        input1.setFont(dFont);
        input3.setFont(dFont2);
        input4.setFont(dFont2);
        inputPass.setFont(dFont2);
        inputPhoneNumber.setFont(dFont2);
        input6.setFont(dFont2);
        inputAddress.setFont(dFont2);input7.setFont(dFont2);inputPostcode.setFont(dFont2);register.setFont(dFont2);input2.setFont(dFont2);goLogin.setFont(dFont2);input5.setFont(dFont2);

        logInput3.setFont(dFont2);
        logInputName.setFont(dFont2);
        logInput4.setFont(dFont2);
        logInputPass.setFont(dFont2);
        login.setFont(dFont2);
        hello.setFont(dFont2);
        meatPizza.setFont(dFont2);
        veganPizza.setFont(dFont2);
        sweet.setFont(dFont2);
        drink.setFont(dFont2);
        addItem.setFont(dFont2);
        drink.setFont(dFont2);
        placeOrder.setFont(dFont2);
        cartTitle.setFont(dFont2);
        cartDescription.setFont(dFont2);
        invoice.setFont(dFont2);
        invoiceDetail.setFont(dFont2);
        inputCoupon.setFont(dFont2);
        applyCoupon.setFont(dFont2);
        pay.setFont(dFont2);
        backToFirst.setFont(dFont2);    sName.setFont(dFont2);    sID.setFont(dFont2);    sMoney.setFont(dFont2);    sAddress.setFont(dFont2);    sPostCode.setFont(dFont2);    sOrderTime.setFont(dFont2);    sCancel.setFont(dFont2);
        sendTime.setFont(dFont2);    sDeliveryManName.setFont(dFont2);    sDPhoneNumber.setFont(dFont2);
        Color pizzaColor = Color.rgb(253, 242, 219,1);
        sceneRegister.setFill(pizzaColor);
        sceneLogin.setFill(pizzaColor);
        sceneMenu.setFill(pizzaColor);
        sceneConfirmation.setFill(pizzaColor);
        sceneStatus.setFill(pizzaColor);

        layoutLogin.setSpacing(5);
        layoutRegister.getChildren().addAll(restaurantName,input1,input3,inputName,input4,inputPass,input5,inputPhoneNumber,input6,inputAddress,input7,inputPostcode,register,input2,goLogin);
        layoutLogin.getChildren().addAll(logInput3,logInputName,logInput4,logInputPass,login,goRegister);
        layoutMenu.getChildren().addAll(hello,meatPizza,meatPizzaBox,veganPizza,veganPizzaBox,sweet,sweetBox,drink,drinkBox,addItem,placeOrder,cartTitle,cartDescription);
        layoutConfirmation.getChildren().addAll(invoice,invoiceDetail,totaPrice,laybelCoupon,inputCoupon,applyCoupon,pay);
        layoutStatus.getChildren().addAll(backToFirst,sName,sID,sMoney,sAddress,sPostCode,sOrderTime,sCancel,sendTime,sDeliveryManName,sDPhoneNumber,cancel);


        stage.setTitle("JoJo Pizza Online");
        stage.setScene(sceneRegister);
        stage.show();

    }
}
