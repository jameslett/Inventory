
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


/**
 *
 * @author james lett
 */


/**
 * FUTURE ENHANCEMENT
 * This program could be improved by serializing the allProducts and allParts Observable and saving them to a file.
 * That way the they could be saved for later.
 * I would also like to create a better system to handle all of the events at once.

 */

public class Inventory extends Application {


   static ObservableList<Product> allProducts = FXCollections.observableArrayList();
   static ObservableList<Part> allParts = FXCollections.observableArrayList();
   private ObservableList<Part> currentPartList=FXCollections.observableArrayList();
   private ObservableList<Product> currentProductList=FXCollections.observableArrayList();
   private Part selectedPart;
   private Product selectedProduct;
   private TableView<Part> partsTable;
   private TableView<Product> productsTable;
   private Part productSelectedPart;

    /**
     * inventory\javaDoc
     * @param args
     */
    public static void main(String[] args) {
       
        launch(args);
        
        
    }

    /**
     * RUNTIME ERROR
     *  I had forgot to initialize the allProducts and allPart Observable list.This created a situation where I was trying to add objects to null.  That was easily solved by initializing them to an empty observable list.
     * @param primaryStage the current stage
     */
    @Override
    public void start(Stage primaryStage){

        //Create Label
        Label testLabel = new Label("Inventory Management System");
        testLabel.setStyle("-fx-font-weight: bold;");
        
        Label partsLabel = new Label("Parts");
        Label productsLabel = new Label("Products");
        
        
        //Create product search field
        TextField productsSearchField = new TextField();
        productsSearchField.setPromptText("Search By ProductID or Name");
        productsSearchField.setFocusTraversable(false); 
       
        
        //Create parts search field
        TextField partsSearchField = new TextField();
        partsSearchField.setPromptText("Search By PartID or Name");
        partsSearchField.setFocusTraversable(false);


        //create the parts and products table and set the size

        partsTable = createPartsTable(allParts);
        createProductsTable();
        partsTable.setMaxSize(400,300);
        productsTable.setMaxSize(400,300);
        
        
       //create all the needed button for the primary stage
       
        Button partsAddButton = new Button("Add");
        Button partsModButton = new Button("Modify");
        Button partsDeleteButton = new Button("Delete");
        
        
        Button productsAddButton = new Button("Add");
        Button productsModButton = new Button("Modify");
        Button productsDeleteButton = new Button("Delete");
        Button exit = new Button("Exit");

        //create hboxs to contain the buttons and align
        HBox partsButtonBox = new HBox(10,partsAddButton,partsModButton,partsDeleteButton);
        HBox productsButtonBox = new HBox(10,productsAddButton,productsModButton,productsDeleteButton);
        partsButtonBox.setAlignment(Pos.CENTER_RIGHT);
        productsButtonBox.setAlignment(Pos.CENTER_RIGHT);
        
        //create hboxs to contain the labels and search fields
        HBox topPartsBox = new HBox(50,partsLabel,partsSearchField);
        HBox topProductsBox = new HBox(50,productsLabel,productsSearchField);

        //create vbox for the parts panel

        VBox partsVBox = new VBox(10,topPartsBox,partsTable,partsButtonBox);
        partsVBox.setAlignment(Pos.CENTER_RIGHT);

        //create vbox for products panel

        VBox productsVBox = new VBox(10,topProductsBox,productsTable,productsButtonBox);
        

        // main hbox for the scene

        HBox mainHBox = new HBox(50,partsVBox,productsVBox);
        
        


        //Create VBox for main box and buttons
        VBox vbox = new VBox(20,mainHBox,exit);
        
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setPadding(new Insets(30));

        //Create Scene
        
        Scene mainScene = new Scene(vbox);
        
        primaryStage.setScene(mainScene);
        
        
        
        //Show Window
        primaryStage.show();



        
        
        
        
        //Handle Events
        

        //Set the event for clicking on the products table
        
         productsTable.setOnMouseClicked(mouseEvent -> {
        if (productsTable.getSelectionModel().getSelectedItem()!=null){
            selectedProduct = (Product)productsTable.getSelectionModel().getSelectedItem();
                   

    }
       });


        //Set the event for clicking the parts table
        partsTable.setOnMouseClicked(mouseEvent -> {
        if (partsTable.getSelectionModel().getSelectedItem()!=null){
            selectedPart = (Part)partsTable.getSelectionModel().getSelectedItem();

    }
       }); 
         
         
        
        
        
        
        
        //Set the product search field to search on ENTER
        
         productsSearchField.setOnKeyPressed(event->{
            
            
            if(event.getCode() == KeyCode.ENTER)
            {    
               String eventText = productsSearchField.getText();
            
                if(eventText.equals("")){
                    getProductsTable().setItems(getAllProducts());
                }
                else if(eventText.chars().allMatch(Character::isDigit)){
                
                    Product tempProduct = lookupProduct(Integer.parseInt(eventText));
                    if (tempProduct != null){
                        currentProductList = FXCollections.observableArrayList(tempProduct);
                        getProductsTable().setItems(currentProductList);
                    }
                    else{
                        getProductsTable().setItems(FXCollections.observableArrayList());
                    }
                    
                    
                }
                else{
                        currentProductList = lookupProduct(eventText);
                        getProductsTable().setItems(currentProductList);
                    
                    
                }
                    
            }
            
        });

        // Set the part search field to auto search on ENTER

        partsSearchField.setOnKeyPressed(event->{


            if(event.getCode() == KeyCode.ENTER)
            {    
               String eventText = partsSearchField.getText();

                if(eventText.equals("")){
                    getPartsTable().setItems(getAllParts());
                }
                else if(eventText.chars().allMatch(Character::isDigit)){
                
                    Part tempPart = lookupPart(Integer.parseInt(eventText));

                    if (tempPart != null){
                     currentPartList = FXCollections.observableArrayList(tempPart);
                        getPartsTable().setItems(currentPartList);
                    }
                    else{
                        getPartsTable().setItems(FXCollections.observableArrayList());
                    }
                    
                    
                }
                else{
                      currentPartList = lookupPart(eventText);
                        getPartsTable().setItems(currentPartList);


                }
                    
            }
            
        }); 

        //Set the delete button to remove selected part if it's not null create alert if it is
           partsDeleteButton.setOnAction(event->{



           if(selectedPart != null){
               Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete?", ButtonType.YES, ButtonType.NO);
               alert.showAndWait();

               if (alert.getResult() == ButtonType.YES) {
                   deletePart(selectedPart);
                   currentPartList.remove(selectedPart);
                   selectedPart = null;
               }


           }
           else{

                   Alert alert = new Alert(AlertType.INFORMATION);
                   alert.setTitle("Information Dialog");
                   alert.setHeaderText(null);
                   alert.setContentText("Please select a part to delete.");

                   alert.showAndWait();

           }
  
        });

          //Set add parts button to popup add part window
           partsAddButton.setOnAction(event -> {
               createAddPartPopup();
           });

         //Set modify part button to popup modify part window if not null, if null create alert
        partsModButton.setOnAction(event -> {

            if(selectedPart == null){
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Please select a part");

                alert.showAndWait();
            }
            else{
                createModPartPopup();
            }


        });

        //Set products mod button to open modify product window if not null, otherwise create an alert

        productsModButton.setOnAction(event -> {

            if(selectedProduct == null){
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Please select a product");

                alert.showAndWait();
            }
            else{
                createModProductPopup();
            }


        });

        //Set product add button to create add product window

        productsAddButton.setOnAction(event -> {
            createAddProductPopup();
        });


        //Set product delete button to delete if not null,otherwise create an alert,also check to see if product has any
        // associated parts if it does create an alert not to delete

                   productsDeleteButton.setOnAction(event->{
           if(selectedProduct != null){
               if(selectedProduct.getAllAssociatedParts().isEmpty()) {
                   Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete?", ButtonType.YES, ButtonType.NO);
                   alert.showAndWait();

                   if (alert.getResult() == ButtonType.YES) {
                       deleteProduct(selectedProduct);
                       currentProductList.remove(selectedProduct);
                       selectedProduct= null;
                   }


               }
               else{
                   Alert alert = new Alert(AlertType.INFORMATION);
                   alert.setTitle("Information Dialog");
                   alert.setHeaderText(null);
                   alert.setContentText("Remove all associated parts before deleting!");

                   alert.showAndWait();
               }
           }
           else{
               Alert alert = new Alert(AlertType.INFORMATION);
               alert.setTitle("Information Dialog");
               alert.setHeaderText(null);
               alert.setContentText("Please select a product to delete.");

               alert.showAndWait();
           }


        });

                   // Set the exit button to cancel the window
        exit.setOnAction(event -> {
        primaryStage.close();
        });



           
    }

    /**
     *Adds a part to the all parts list
     * @param p part to add
     */
   public static void addPart(Part p){
    allParts.add(p);
}

    /**
     * Adds a product to the all products list
     * @param p product to add
     */

public static void addProduct(Product p){
    allProducts.add(p);
}

    /**
     * Takes a part ID and returns a part alerts if  not found
     * @param partID ID of the part find
     * @return returns the desired part
     */
    private static Part lookupPart(int partID){
    
    for(Part p : allParts){
        if (p.getId()== partID){
            return p;
        }
    }
    //alert if part isn't found.
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Part not found");

        alert.showAndWait();
    return null;
    
}

    /**
     * Finds a part containing a string alerts if not found
     * @param partName the name of the part to find
     * @return a part with corresponding name
     */

private static ObservableList<Part> lookupPart(String partName){
    ObservableList<Part> temp = FXCollections.observableArrayList();
    
    for(Part p : allParts){
        if(p.getName().startsWith(partName)){
            temp.add(p);
        }
    }
    if (temp.isEmpty()){
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Information Dialog");
    alert.setHeaderText(null);
    alert.setContentText("Part not found");

    alert.showAndWait();}
    return temp;
}

    /**
     * Takes a string and returns a list of all products with a name containing string
     * @param productName name of product
     * @return List of products containing the string productName
     */

private static ObservableList<Product> lookupProduct(String productName){
    ObservableList<Product> temp = FXCollections.observableArrayList();
    
    for(Product p : allProducts){
        if(p.getName().startsWith(productName)){
            temp.add(p);
        }
    }
    if(temp.isEmpty()){
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Information Dialog");
    alert.setHeaderText(null);
    alert.setContentText("Product not found");

    alert.showAndWait();}
    return temp;
}

    /**
     * Updates a part at a certain index of all parts list
     * @param index the index of the part to be updated
     * @param selectedPart a reference to the new part
     */

private void updatePart(int index,Part selectedPart){
    allParts.set(index,selectedPart);
}

    /**
     *Updates a product at a certain index of all products list
     * @param index the index of the part to be updated
     * @param selectedProduct a reference to the new product
     */
   private static void updateProduct(int index,Product selectedProduct){
    allProducts.set(index,selectedProduct);
}

    /**
     * Deletes a part that matches the selected part from all products
     * @param selectedPart part to delete
     * @return true if part was deleted,false otherwise
     */
   private static boolean deletePart(Part selectedPart){
   return allParts.remove(selectedPart);
}

    /**
     * deletes a product that matches the selected product from all products
     * @param selectedProduct product to delete
     * @return true if product was deleted,false otherwise
     */
    boolean deleteProduct(Product selectedProduct){
   return allProducts.remove(selectedProduct);
}

    /**
     * Looks up a product in all products by product ID creates an alert if not found
     * @param productID id of the product
     * @return returns a reference to the product
     */
    private static Product lookupProduct(int productID){
    
    for(Product p : allProducts){
        if (p.getId()== productID){
            return p;
        }
    }
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Product not found");

        alert.showAndWait();

    return null;
    
}

    /**
     *
     * @return returns a observable list of all products
     */
    static ObservableList<Product> getAllProducts(){

    return allProducts;
}

    /**
     *
     * @return an observable list of all parts
     */
   static  ObservableList<Part> getAllParts(){

    return allParts;
}
/**
*Creates a the parts table
 * @param partList list of parts
 * @return a tableview of the parts
*/
private static TableView<Part> createPartsTable(ObservableList<Part> partList){
     TableView<Part> tempTable = new TableView<>();
        tempTable.setEditable(true);

        //Create all table columns and assign their cell values

        TableColumn partIDCol = new TableColumn("PartID");
        partIDCol.setMinWidth(75);
        partIDCol.setCellValueFactory(new PropertyValueFactory<Part,Integer>("id"));
        
        TableColumn partNameCol = new TableColumn("Part Name");
        partNameCol.setMinWidth(75);
        partNameCol.setCellValueFactory(new PropertyValueFactory<Part,String>("name"));
       
        TableColumn partInventoryLevelCol = new TableColumn("Inventory Level");
        partInventoryLevelCol.setMinWidth(100);
        partInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<Part,Integer>("stock"));
        
        TableColumn partCostCol = new TableColumn("Part Cost/ Per unit");
        partCostCol.setCellValueFactory(new PropertyValueFactory<Part,Double>("price"));
        
        tempTable.getColumns().addAll(partIDCol,partNameCol, partInventoryLevelCol, partCostCol);
        
       
        tempTable.setItems(allParts);

        return tempTable;
}

    /**
     * Creates a parts table view from an Observable list
     * @param partList an ObservableList of Parts
     * @return a TableView of the parts
     */
    private TableView<Part> createProductPartsTable(ObservableList<Part> partList){
        TableView<Part> tempTable = new TableView();

        tempTable.setEditable(true);

        //Create all table columns and set their cell values

        TableColumn partIDCol = new TableColumn("PartID");
        partIDCol.setMinWidth(75);
        partIDCol.setCellValueFactory(new PropertyValueFactory<Part,Integer>("id"));

        TableColumn partNameCol = new TableColumn("Part Name");
        partNameCol.setMinWidth(75);
        partNameCol.setCellValueFactory(new PropertyValueFactory<Part,String>("name"));

        TableColumn partInventoryLevelCol = new TableColumn("Inventory Level");
        partInventoryLevelCol.setMinWidth(100);
        partInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<Part,Integer>("stock"));

        TableColumn partCostCol = new TableColumn("Part Cost/ Per unit");
        partCostCol.setCellValueFactory(new PropertyValueFactory<Part,Double>("price"));

        tempTable.getColumns().addAll(partIDCol,partNameCol, partInventoryLevelCol, partCostCol);


        tempTable.setItems(partList);
        return  tempTable;
    }


/**
 * Returns the parts TableView
 * @return Returns a ref to the parts table. 
 */
public TableView<Part> getPartsTable(){
   
    if (partsTable == null){
        partsTable = createPartsTable(allParts);
    }
    return partsTable;
}

    /**
     * Creates the products pane tableview
     */
    public void createProductsTable(){
    productsTable = new TableView();
        productsTable.setEditable(true);
        
 
        TableColumn productIDCol = new TableColumn("ProductID");
        productIDCol.setMinWidth(75);
        productIDCol.setCellValueFactory(new PropertyValueFactory<Part,Integer>("id"));
        
        TableColumn productNameCol = new TableColumn("Product Name");
        productNameCol.setMinWidth(75);
        productNameCol.setCellValueFactory(new PropertyValueFactory<Part,String>("name"));
       
        TableColumn productInventoryLevelCol = new TableColumn("Inventory Level");
        productInventoryLevelCol.setMinWidth(100);
        productInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<Part,Integer>("stock"));
        
        TableColumn productCostCol = new TableColumn("Part Cost/ Per unit");
        productCostCol.setCellValueFactory(new PropertyValueFactory<Part,Double>("price"));

        productsTable.getColumns().addAll(productIDCol,productNameCol, productInventoryLevelCol, productCostCol);
        productsTable .setItems(allProducts);
}

    /**
     * Returns the product table
     * @return the product table
     */
    public TableView<Product> getProductsTable(){
    
    if (productsTable == null){
        createProductsTable();
    }
    
    return productsTable;
}

    /**
     * creates and shows the add part popup
     */

    private void createAddPartPopup(){

        //Create a new stage and add labels

        Stage newStage = new Stage();

        Label addPartLabel = new Label("Add Part");

        //Create radio buttons and add them to toggle group
        RadioButton inHouse = new RadioButton("In House");
        RadioButton outSourced = new RadioButton("Outsourced");

        ToggleGroup buttons = new ToggleGroup();
        inHouse.setToggleGroup(buttons);
        outSourced.setToggleGroup(buttons);
        inHouse.setSelected(true);






        //create radio button hbox
        HBox radioBox =  new HBox(10,addPartLabel,inHouse,outSourced);

        //create all the labels for the parts form


        Label partID = new Label("ID");
        Label name = new Label("Name");
        Label inv = new Label ("Inv");
        Label cost = new Label("Price/Cost");
        Label max = new Label("Max");
        Label min = new Label("Min");
        Label machineId = new Label("MachineID");
        Label companyName = new Label("Company Name");


        //create all the required text fields

        TextField partIdField = new TextField();

        partIdField.setPromptText("AutoGenerated");
        partIdField.setDisable(true);

        TextField nameField = new TextField();
        TextField invField = new TextField();
        TextField costField = new TextField();
        TextField maxField = new TextField();
        TextField minField = new TextField();
        TextField machineOrCompanyField = new TextField();
        machineOrCompanyField.minWidth(100);

        // create the save and cancel buttons

        Button save = new Button("Save");
        Button cancel = new Button("Cancel");
        HBox saveBox = new HBox(10,save,cancel);
        saveBox.setAlignment(Pos.CENTER_RIGHT);

        //create vbox for all of the text fields

        VBox fieldBox = new VBox(10,partIdField,nameField,invField,costField,maxField,minField,machineOrCompanyField);
        // create vbox for all of the labels
        VBox labelBox = new VBox(19,partID,name,inv,cost,max,min,machineId);
        labelBox.setMinWidth(100);

        //hbox for fields and labels
        HBox fields = new HBox(labelBox,fieldBox);

        //final vbox for the scene
        VBox partsVBox = new VBox(10,radioBox,fields,saveBox);
        partsVBox.setPadding(new Insets(30));



        Scene stageScene = new Scene(partsVBox);
        newStage.setScene(stageScene);


        //radio button listener

        buttons.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
              public void changed(ObservableValue<? extends Toggle> ov,Toggle old_toggle, Toggle new_toggle) {
                if (buttons.getSelectedToggle() != null) {

                    RadioButton selectedButton = (RadioButton) buttons.getSelectedToggle();
                    if (selectedButton.getText().equals("In House")) {
                        machineId.setText("Machine ID");

                    }
                    if(selectedButton.getText().equals("Outsourced")){
                       machineId.setText("Company Name");
                    }
                }

              }});

            //event handling
            cancel.setOnAction(event -> {
                newStage.close();
            });

            //Set the save event
            save.setOnAction(event->{

                try {
                    double tempPrice = Double.parseDouble(costField.getText());
                    int tempInv = Integer.parseInt(invField.getText());
                    int tempMin = Integer.parseInt(minField.getText());
                    int tempMax = Integer.parseInt(maxField.getText());
                    //if any field is empty prompt message
                    if(nameField.getText().isEmpty() ||
                       costField.getText().isEmpty() ||
                       invField.getText().isEmpty() ||
                       minField.getText().isEmpty() ||
                       maxField.getText().isEmpty() ||
                       machineOrCompanyField.getText().isEmpty()
                    ){
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("All fields must be full!");

                        alert.showAndWait();
                    }
                    else if(tempMin > tempMax || tempMax<tempMin ){
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Max must be greater than or equal to min!");

                        alert.showAndWait();
                    }
                    else if(tempInv< tempMin || tempInv > tempMax){
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Inv must be between Min and Max");

                        alert.showAndWait();
                    }
                    else if(tempPrice <= 0){
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Price must be greater than 0");

                        alert.showAndWait();
                    }
                    else if(tempMin <= 0){

                            Alert alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Information Dialog");
                            alert.setHeaderText(null);
                            alert.setContentText("Min must be greater than 0");

                            alert.showAndWait();

                    }
                    else if(inHouse.isSelected() && Integer.parseInt(machineOrCompanyField.getText()) <= 0){
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("MachineID must be greater than 0");

                        alert.showAndWait();
                    }




                    else{
                        String  tempName = nameField.getText();




                        if(inHouse.isSelected()){
                            int tempMachineId = Integer.parseInt(machineOrCompanyField.getText());



                            InHouse temp = new InHouse(generatePartID(),tempName,tempPrice,tempInv,tempMin,tempMax,tempMachineId);
                            addPart(temp);
                            newStage.close();
                        }
                        if(outSourced.isSelected()){

                            Outsourced temp = new Outsourced(generatePartID(),tempName,tempPrice,tempInv,tempMin,tempMax,machineOrCompanyField.getText());
                            addPart(temp);
                            newStage.close();
                        }
                        nameField.clear();
                        costField.clear();
                        invField.clear();
                        minField.clear();
                        maxField.clear();
                        machineOrCompanyField.clear();

                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Part Added!");

                        alert.showAndWait();

                    }

                } catch (NumberFormatException e) {

                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid Input!" +
                            "\n The Following fields must be a number:\nStock\nMin\nMax\nMachineID");

                    alert.showAndWait();
                }

            });

         newStage.initModality(Modality.APPLICATION_MODAL);
         newStage.showAndWait();
 
 
 

 
 

 
 

}

    /**
     * creates and shows the mod part popup
     */

private void createModPartPopup(){

        Stage newStage = new Stage();
        //create radio buttons and toggle group

        Label addPartLabel = new Label("Add Part");
        RadioButton inHouse = new RadioButton("In House");
        RadioButton outSourced = new RadioButton("Outsourced");

        ToggleGroup buttons = new ToggleGroup();
        inHouse.setToggleGroup(buttons);
        outSourced.setToggleGroup(buttons);

       //set radio buttons based on selected part
        if(selectedPart instanceof InHouse){
            inHouse.setSelected(true);

    }
        else{
            outSourced.setSelected(true);
        }





        // create required labels

        HBox radioBox =  new HBox(10,addPartLabel,inHouse,outSourced);

        Label partID = new Label("ID");
        Label name = new Label("Name");
        Label inv = new Label ("Inv");
        Label cost = new Label("Price/Cost");
        Label max = new Label("Max");
        Label min = new Label("Min");
        Label machineId = new Label("MachineID");



        TextField partIdField = new TextField();

        partIdField.setPromptText("AutoGenerated");
        partIdField.setDisable(true);

        //Create required text fields

        TextField nameField = new TextField(selectedPart.getName());
        TextField invField = new TextField(Integer.toString(selectedPart.getStock()));
        TextField costField = new TextField(Double.toString(selectedPart.getPrice()));
        TextField maxField = new TextField(Integer.toString(selectedPart.getMax()));
        TextField minField = new TextField(Integer.toString(selectedPart.getMin()));
        TextField machineOrCompanyField;

        //change the last field based on radio buttons
        if(inHouse.isSelected()) {
            InHouse temp = (InHouse)selectedPart;
            machineOrCompanyField = new TextField(Integer.toString(temp.getMachineID()));
        }
        else{
            Outsourced temp = (Outsourced)selectedPart;

             machineOrCompanyField = new TextField(temp.getCompanyName());
        }
        machineOrCompanyField.minWidth(100);

        //create save and cancel buttons and add them to a hbox

        Button save = new Button("Save");
        Button cancel = new Button("Cancel");
        HBox saveBox = new HBox(10,save,cancel);
        saveBox.setAlignment(Pos.CENTER_RIGHT);

        VBox fieldBox = new VBox(10,partIdField,nameField,invField,costField,maxField,minField,machineOrCompanyField);

        VBox labelBox = new VBox(19,partID,name,inv,cost,max,min,machineId);
        labelBox.setMinWidth(100);
        HBox fields = new HBox(labelBox,fieldBox);


        VBox partsVBox = new VBox(10,radioBox,fields,saveBox);
        partsVBox.setPadding(new Insets(30));



        Scene stageScene = new Scene(partsVBox);
        newStage.setScene(stageScene);


//radio button listener

        buttons.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,Toggle old_toggle, Toggle new_toggle) {
                if (buttons.getSelectedToggle() != null) {

                    RadioButton selectedButton = (RadioButton) buttons.getSelectedToggle();
                    if (selectedButton.getText().equals("In House")) {
                        machineId.setText("Machine ID");

                    }
                    if(selectedButton.getText().equals("Outsourced")){
                        machineId.setText("Company Name");
                    }
                }

            }});

        //event handling
        cancel.setOnAction(event -> {
            newStage.close();
        });

        //set the save event
        save.setOnAction(event->{

            try {

                String  tempName = nameField.getText();
                double tempPrice = Double.parseDouble(costField.getText());
                int tempInv = Integer.parseInt(invField.getText());
                int tempMin = Integer.parseInt(minField.getText());
                int tempMax = Integer.parseInt(maxField.getText());

                //alert if any field is empty
                if(nameField.getText().isEmpty() ||
                        costField.getText().isEmpty() ||
                        invField.getText().isEmpty() ||
                        minField.getText().isEmpty() ||
                        maxField.getText().isEmpty() ||
                        machineOrCompanyField.getText().isEmpty()
                ){
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("All fields must be full!");

                    alert.showAndWait();
                }
                else if(tempMin > tempMax || tempMax<tempMin ){
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Max must be greater than min!");

                    alert.showAndWait();
                }
                else if(tempInv< tempMin || tempInv > tempMax){
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Inv must be between Min and Max");

                    alert.showAndWait();
                }
                else if(tempPrice <= 0){
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Price must be greater than 0");

                    alert.showAndWait();
                }
                else if(tempMin <= 0){

                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Min must be greater than 0");

                    alert.showAndWait();

                }
                else if(inHouse.isSelected() && Integer.parseInt(machineOrCompanyField.getText()) <= 0){
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("MachineID must be greater than 0");

                    alert.showAndWait();
                }
                else{



                    if(inHouse.isSelected()){
                        int tempMachineId = Integer.parseInt(machineOrCompanyField.getText());
                        InHouse temp = new InHouse(selectedPart.getId(),tempName,tempPrice,tempInv,tempMin,tempMax,tempMachineId);
                        updatePart(allParts.indexOf(selectedPart),temp);
                        newStage.close();
                    }
                    else if(outSourced.isSelected()){
                        Outsourced temp = new Outsourced(selectedPart.getId(),tempName,tempPrice,tempInv,tempMin,tempMax,machineOrCompanyField.getText());
                        updatePart(allParts.indexOf(selectedPart),temp);
                        newStage.close();
                    }


                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Part Added!");

                    alert.showAndWait();

                }

            }
            //catch if any of the number fields are not digits
            catch (NumberFormatException e) {


                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Invalid Input!" +
                        "\n The Following fields must be a number:\nStock\nMin\nMax\nMachineID");

                alert.showAndWait();
            }

        });

        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.showAndWait();










    }

    /**
     * creates and shows the add product popup
     */
    private void createAddProductPopup(){

        Stage newStage = new Stage();
        ObservableList<Part> assPartsList = FXCollections.observableArrayList();
        Label addPartLabel = new Label("Add Product");
        TableView<Part> assPartsTable = createProductPartsTable(assPartsList);
        TableView<Part> tempPartsTable = createPartsTable(allParts);







        //create required labels

        Label addProduct = new Label("Add Product");
        Label partID = new Label("ID");
        Label name = new Label("Name");
        Label inv = new Label ("Inv");
        Label cost = new Label("Price");
        Label max = new Label("Max");
        Label min = new Label("Min");


        // create required text fields

        TextField partIdField = new TextField();

        partIdField.setPromptText("AutoGenerated");
        partIdField.setDisable(true);

        TextField nameField = new TextField();
        TextField invField = new TextField();
        TextField costField = new TextField();
        TextField maxField = new TextField();
        TextField minField = new TextField();


        //create save and cancel button

        Button save = new Button("Save");
        Button cancel = new Button("Cancel");
        HBox saveBox = new HBox(10,save,cancel);
        saveBox.setAlignment(Pos.CENTER_RIGHT);

        VBox fieldBox = new VBox(10,partIdField,nameField,invField,costField,maxField,minField);

        VBox labelBox = new VBox(19,partID,name,inv,cost,max,min);
        labelBox.setMinWidth(100);
        HBox fields = new HBox(labelBox,fieldBox);



        VBox productVBox = new VBox(10,addProduct,fields,saveBox);

        //create add and remove part buttons

        Button addPart = new Button("Add Part");
        Button removePart = new Button("Remove Associated Part");
        addPart.setAlignment(Pos.CENTER_RIGHT);
        tempPartsTable.setMaxSize(500,200);


        //Create parts search field
        TextField partsSearchField = new TextField();
        partsSearchField.setPromptText("Search By PartID or Name");
        partsSearchField.setFocusTraversable(false);



        VBox addPartVBox = new VBox(20,partsSearchField,tempPartsTable,addPart,assPartsTable,removePart);
        assPartsTable.setMaxSize(500,200);
        addPartVBox.setMaxSize(500,400);

        HBox productHBox = new HBox(20,productVBox,addPartVBox);
        productHBox.setPadding(new Insets(50));



        Scene stageScene = new Scene(productHBox);
        newStage.setScene(stageScene);



        //event handling
        cancel.setOnAction(event -> {
            newStage.close();
        });

        //set save action
        save.setOnAction(event->{

            try {
                double tempPrice = Double.parseDouble(costField.getText());
                int tempInv = Integer.parseInt(invField.getText());
                int tempMin = Integer.parseInt(minField.getText());
                int tempMax = Integer.parseInt(maxField.getText());

                //check to see if all fields are filled out
                if(nameField.getText().isEmpty() ||
                        costField.getText().isEmpty() ||
                        invField.getText().isEmpty() ||
                        minField.getText().isEmpty() ||
                        maxField.getText().isEmpty()

                ){
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("All fields must be full!");

                    alert.showAndWait();
                }
                else if(tempMin > tempMax || tempMax<tempMin ){
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Max must be greater than min!");

                    alert.showAndWait();
                }
                else if(tempInv< tempMin || tempInv > tempMax){
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Inv must be between Min and Max");

                    alert.showAndWait();
                }
                else if(tempPrice <= 0){
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Price must be greater than 0");

                    alert.showAndWait();
                }
                else if(tempMin <= 0){

                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Min must be greater than 0");

                    alert.showAndWait();

                }

                else{
                    String  tempName = nameField.getText();

                    Product tempProduct = new Product(generateProductID(),tempName,tempPrice,tempInv,tempMin,tempMax);
                    addProduct(tempProduct);

                    for(Part p : assPartsList){
                        tempProduct.addAssociatedPart(p);

                    }
                    newStage.close();
                    assPartsList.clear();

                    nameField.clear();
                    costField.clear();
                    invField.clear();
                    minField.clear();
                    maxField.clear();


                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Product Added!");

                    alert.showAndWait();

                }

            }
            //catch if any of the number fields are not a digit
            catch (NumberFormatException e) {


                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Invalid Input!" +
                        "\n The Following fields must be a number:\nStock\nMin\nMax");

                alert.showAndWait();
            }

        });

        tempPartsTable.setOnMouseClicked(mouseEvent -> {
            if (tempPartsTable.getSelectionModel().getSelectedItem()!=null){
                productSelectedPart = (Part)tempPartsTable.getSelectionModel().getSelectedItem();

            }
        });

        assPartsTable.setOnMouseClicked(mouseEvent -> {
            if (tempPartsTable.getSelectionModel().getSelectedItem()!=null){
                productSelectedPart = (Part)tempPartsTable.getSelectionModel().getSelectedItem();

            }
        });


        //add part on if selected,otherwise alert to select a part
        addPart.setOnAction(event -> {
            if(productSelectedPart!= null){
            assPartsList.add(productSelectedPart);

                productSelectedPart = null;

            }

            else{
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Please select a part to add.");

                alert.showAndWait();
            }
        });
        //remove part if selected,otherwise alert to select a part
        removePart.setOnAction(event->{
            if(productSelectedPart != null){
                Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to remove?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    assPartsList.remove(productSelectedPart);
                    productSelectedPart = null;
                }

            }
            else{
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Please select a part to remove.");

                alert.showAndWait();
            }


        });
        //set selected part to null
        newStage.setOnCloseRequest(event->{
            productSelectedPart = null;
        });

        //set search field to search on ENTER
        partsSearchField.setOnKeyPressed(event->{


            if(event.getCode() == KeyCode.ENTER)
            {
                String eventText = partsSearchField.getText();

                if(eventText.equals("")){
                    tempPartsTable.setItems(getAllParts());
                }
                else if(eventText.chars().allMatch(Character::isDigit)){

                    Part tempPart = lookupPart(Integer.parseInt(eventText));
                    if (tempPart != null){
                        tempPartsTable.setItems(FXCollections.observableArrayList(tempPart));
                    }
                    else{
                        tempPartsTable.setItems(FXCollections.observableArrayList());
                    }


                }
                else{

                    tempPartsTable.setItems(FXCollections.observableArrayList(lookupPart(eventText)));


                }

            }

        });



        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.showAndWait();











    }

    /** creates and shows the modify product popup
     *
     */
    private void createModProductPopup(){

        Stage newStage = new Stage();
        Label addPartLabel = new Label("Add Product");
        TableView<Part> assPartsTable = createProductPartsTable(selectedProduct.getAllAssociatedParts());
        TableView<Part> tempPartsTable = createPartsTable(allParts);








        Label addProduct = new Label("Add Product");
        Label partID = new Label("ID");
        Label name = new Label("Name");
        Label inv = new Label ("Inv");
        Label cost = new Label("Price");
        Label max = new Label("Max");
        Label min = new Label("Min");




        TextField partIdField = new TextField();

        partIdField.setPromptText("AutoGenerated");
        partIdField.setDisable(true);

        TextField nameField = new TextField(selectedProduct.getName());
        TextField invField = new TextField(Integer.toString(selectedProduct.getStock()));
        TextField costField = new TextField(Double.toString(selectedProduct.getPrice()));
        TextField maxField = new TextField(Integer.toString(selectedProduct.getMax()));
        TextField minField = new TextField(Integer.toString(selectedProduct.getMin()));




        Button save = new Button("Save");
        Button cancel = new Button("Cancel");
        HBox saveBox = new HBox(10,save,cancel);
        saveBox.setAlignment(Pos.CENTER_RIGHT);

        VBox fieldBox = new VBox(10,partIdField,nameField,invField,costField,maxField,minField);

        VBox labelBox = new VBox(19,partID,name,inv,cost,max,min);
        labelBox.setMinWidth(100);
        HBox fields = new HBox(labelBox,fieldBox);



        VBox productVBox = new VBox(10,addProduct,fields,saveBox);

        Button addPart = new Button("Add Part");
        Button removePart = new Button("Remove Associated Part");
        addPart.setAlignment(Pos.CENTER_RIGHT);
        tempPartsTable.setMaxSize(500,200);


        //Create parts search field
        TextField partsSearchField = new TextField();
        partsSearchField.setPromptText("Search By PartID or Name");
        partsSearchField.setFocusTraversable(false);



        VBox addPartVBox = new VBox(20,partsSearchField,tempPartsTable,addPart,assPartsTable,removePart);
        assPartsTable.setMaxSize(500,200);
        addPartVBox.setMaxSize(500,400);

        HBox productHBox = new HBox(20,productVBox,addPartVBox);
        productHBox.setPadding(new Insets(50));



        Scene stageScene = new Scene(productHBox);
        newStage.setScene(stageScene);



        //event handling
        cancel.setOnAction(event -> {
            newStage.close();
        });
        //set to null on window close
        newStage.setOnCloseRequest(event->{
            productSelectedPart = null;
            selectedProduct = null;
        });

        //set the save event
        save.setOnAction(event->{

            try {


                String  tempName = nameField.getText();
                double tempPrice = Double.parseDouble(costField.getText());
                int tempInv = Integer.parseInt(invField.getText());
                int tempMin = Integer.parseInt(minField.getText());
                int tempMax = Integer.parseInt(maxField.getText());

                //check if fields are filled out

                if(nameField.getText().isEmpty() ||
                        costField.getText().isEmpty() ||
                        invField.getText().isEmpty() ||
                        minField.getText().isEmpty() ||
                        maxField.getText().isEmpty()

                ){
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("All fields must be full!");

                    alert.showAndWait();
                }

                //min max stuff

                else if(tempMin > tempMax || tempMax<tempMin ){
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Max must be greater than min!");

                    alert.showAndWait();
                }
                else if(tempInv< tempMin || tempInv > tempMax){
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Inv must be between Min and Max");

                    alert.showAndWait();
                }
                else if(tempPrice <= 0){
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Price must be greater than 0");

                    alert.showAndWait();
                }
                else if(tempMin <= 0){

                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Min must be greater than 0");

                    alert.showAndWait();

                }
                else{

                    Product tempProduct = new Product(selectedProduct.getId(),tempName,tempPrice,tempInv,tempMin,tempMax);
                    updateProduct(allProducts.indexOf(selectedProduct),tempProduct);

                    for(Part p : selectedProduct.getAllAssociatedParts()){
                        tempProduct.addAssociatedPart(p);


                    }


                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Product Modified!");

                    alert.showAndWait();
                    newStage.close();
                }

            }
            //catch if fields are not digits
            catch (NumberFormatException e) {

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Invalid Input!" +
                        "\n The Following fields must be a number:\nStock\nMin\nMax");

                alert.showAndWait();
            }

        });
        //set up click event on temp parts table
        tempPartsTable.setOnMouseClicked(mouseEvent -> {
            if (tempPartsTable.getSelectionModel().getSelectedItem()!=null){
                productSelectedPart = (Part)tempPartsTable.getSelectionModel().getSelectedItem();

            }
        });

        //set up the add part button
        addPart.setOnAction(event -> {
            if(productSelectedPart!= null){
                selectedProduct.getAllAssociatedParts().add(productSelectedPart);}
            else{
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Please select a part to add.");

                alert.showAndWait();
            }
        });
        //setup the remove part button
        removePart.setOnAction(event->{
            if(productSelectedPart != null){

                Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to remove?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    selectedProduct.getAllAssociatedParts().remove(productSelectedPart);
                }


            }
            else{
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Please select a part to remove.");

                alert.showAndWait();
            }

        });

        //set up the part search field to work on ENTER
        partsSearchField.setOnKeyPressed(event->{


            if(event.getCode() == KeyCode.ENTER)
            {
                String eventText = partsSearchField.getText();

                if(eventText.equals("")){
                    tempPartsTable.setItems(getAllParts());
                }
                else if(eventText.chars().allMatch(Character::isDigit)){

                    Part tempPart = lookupPart(Integer.parseInt(eventText));
                    if (tempPart != null){
                        tempPartsTable.setItems(FXCollections.observableArrayList(tempPart));
                    }
                    else{
                        tempPartsTable.setItems(FXCollections.observableArrayList());
                    }


                }
                else{

                    tempPartsTable.setItems(FXCollections.observableArrayList(lookupPart(eventText)));


                }

            }

        });


        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.showAndWait();











    }

    /**
     *
     * @return a unique part id
     */
    private int generatePartID(){
    if (allParts.isEmpty()){

        return 1;
    }
    return allParts.get(allParts.size()-1).getId() + 1;
}

    /**
     *
     * @return a unique product id
     */
    private int generateProductID(){
    if(allProducts.isEmpty()){
        return 1;
    }
    return allProducts.get(allProducts.size()-1).getId() +1;
}

    /**
     * a class for in house parts
     */
    public static class InHouse extends Part{
        private int machineID;

        /**
         *
         * @param ID part id
         * @param name part name
         * @param price cost of the part
         * @param stock how many parts are in stock
         *
         * @param min min stock must be below max
         * @param max max stock must be more then min
         * @param machineID ID of machine that created part
         */

        InHouse(int ID,String name,double price,int stock,int min,int max,int machineID){
            super(ID,name,price,stock,min,max);
            this.machineID = machineID;


        }

        /**
         * Getter for machineID
         * @return the machine id
         */
        public int getMachineID(){
            return machineID;
        }

        /**
         * sets the machine id
         * @param id the id to set
         */
        public void setMachineID(int id){

            machineID = id;

        }
    }

    /**
     * a class for outsourced parts
     */
    public static class Outsourced extends Part{
        private String companyName;

        /**
         *
         * @param ID the part id
         * @param name the part name
         * @param price the price of the part
         * @param stock the number of part in stock
         * @param min min number of part to stock
         * @param max max number of part to stock
         * @param companyName the name of the company that created the part
         */

        Outsourced(int ID,String name,double price,int stock,int min,int max,String companyName){
            super(ID,name,price,stock,min,max);
            this.companyName = companyName;


        }
        /**
         * Gets the parts company name
         * @return  the company name
         */
        public String getCompanyName(){
            return companyName;
        }

        /**
         * set the company name
         * @param name desired company name
         */

        public void setCompanyName(String name){

           companyName = name;

        }
    }

    /**
     * a Class for products
     */
    public static class Product {

        ObservableList<Part> associatedParts;
        private int id;
        private String name;
        private double price;
        private int stock;
        private int min;
        private int max;

        /**
         *
         * @param id the product id
         * @param name the name of the product
         * @param price the price of the product
         * @param stock the current amount of product in inventory
         * @param min the minimum amount of product in inventory
         * @param max the max amount of product in inventory
         */
        public Product(int id, String name, double price, int stock, int min, int max) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.stock = stock;
            this.min = min;
            this.max = max;
            associatedParts = FXCollections.observableArrayList();
        }




        /**
         * returns product id
         * @return the id
         */
        public int getId() {
            return id;
        }

        /**
         * sets product id
         * @param id the id to set
         */
        public void setId(int id) {
            this.id = id;
        }

        /**
         * returns product name
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * sets product name
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * returns product price
         * @return the price
         */
        public double getPrice() {
            return price;
        }

        /**
         * sets product price
         * @param price the price to set
         */
        public void setPrice(double price) {
            this.price = price;
        }

        /**
         * returns the number of products in stock
         * @return the stock
         */
        public int getStock() {
            return stock;
        }

        /**
         * sets the number of items in stock
         * @param stock the stock to set
         */
        public void setStock(int stock) {
            this.stock = stock;
        }

        /**
         * returns the minimum number of stock
         * @return the min
         */
        public int getMin() {
            return min;
        }

        /**
         * sets the min number of stock
         * @param min the min to set
         */
        public void setMin(int min) {
            this.min = min;
        }

        /**
         * returns the max number of stock
         * @return the max
         */
        public int getMax() {
            return max;
        }

        /**
         * sets the max number of stock
         * @param max the max to set
         */
        public void setMax(int max) {
            this.max = max;
        }

        /**
         *adds a part to the product
         * @param part part to add
         */
        public void addAssociatedPart(Part part){
            associatedParts.add(part);
        }

        /**
         * returns a list of all associated parts
         * @return list of all associated parts
         */
        public ObservableList<Part> getAllAssociatedParts(){
            return associatedParts;
        }

        /**
         *removes a part from the product and returns a bool
         * @param part to remove
         * @return true if part was removed,else false
         */
        public boolean removeAssociatedPart(Part part){

            return associatedParts.remove(part);

        }
    }
}


