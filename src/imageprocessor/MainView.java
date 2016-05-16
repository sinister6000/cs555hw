package imageprocessor;

import imageprocessor.model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainView implements Initializable {

    static ArrayList<Image> imageHistory;

    private GrayImage leftGrayImage;
    private GrayImage rightGrayImage;
    @FXML
    private BarChart<String, Number> leftHistogram;
    @FXML
    private BarChart<String, Number> rightHistogram;
    @FXML
    private ScrollPane sp1;
    @FXML
    private ScrollPane sp2;
    @FXML
    private MenuItem openFileItem;
    @FXML
    private MenuItem saveFileItem;
    @FXML
    private MenuItem exitItem;
    @FXML
    private Button rightToLeft;
    @FXML
    private VBox right2LeftGroup;
    @FXML
    private ImageView iv1;
    @FXML
    private ImageView iv2;
    @FXML
    private TitledPane tp1;
    @FXML
    private TitledPane tp2;
    @FXML
    private Label leftInfo;
    @FXML
    private Label rightInfo;
    @FXML
    private Label bitDepthLabel;
    @FXML
    private Button buttonGrayRes;
    @FXML
    private TextField logTransC;
    @FXML
    private Button buttonLogTrans;
    @FXML
    private TextField powerTransC;
    @FXML
    private TextField powerTransGamma;
    @FXML
    private Button buttonPowerTrans;
    @FXML
    private Label sourceImgSizeLabel;
    @FXML
    private Button button640;
    @FXML
    private Button button80;
    @FXML
    private Button histEqGlobalButton;
    @FXML
    private Button histEqButton;
    @FXML
    private ToggleGroup histEqToggleGroup;
    @FXML
    private RadioButton histGlobal;
    @FXML
    private RadioButton histLocal;
    @FXML
    private Group histLocalGroup;
    @FXML
    private Group histLocalGroup2;
    @FXML
    private TextField histLocalFilterSize;
    @FXML
    private Button histMatchingButton;
    @FXML
    private VBox leftHistPane;
    @FXML
    private VBox rightHistPane;
    @FXML
    private TextField smoothingWidth;
    @FXML
    private TextField smoothingHeight;
    @FXML
    private Button smoothingButton;
    @FXML
    private TextField medianWidth;
    @FXML
    private TextField medianHeight;
    @FXML
    private Button medianButton;
    @FXML
    private ChoiceBox<String> rankType;
    @FXML
    private TextField rankWidth;
    @FXML
    private TextField rankHeight;
    @FXML
    private Button rankButton;
    @FXML
    private ChoiceBox<String> laplacianWidth;
    @FXML
    private Button laplacianButton;
    @FXML
    private TextField highBoostWidth;
    @FXML
    private TextField highBoostHeight;
    @FXML
    private TextField highBoostAmount;
    @FXML
    private Button highBoostButton;
    @FXML
    private TextField geoWidth;
    @FXML
    private TextField geoHeight;
    @FXML
    private Button geoButton;
    @FXML
    private TextField harmonicWidth;
    @FXML
    private TextField harmonicHeight;
    @FXML
    private Button harmonicButton;
    @FXML
    private TextField contraHarmonicWidth;
    @FXML
    private TextField contraHarmonicHeight;
    @FXML
    private TextField contraHarmonicQ;
    @FXML
    private Button contraHarmonicButton;
    @FXML
    private TextField alphaWidth;
    @FXML
    private TextField alphaHeight;
    @FXML
    private TextField alphaD;
    @FXML
    private Button alphaButton;

    public GrayImage getLeftGrayImage() {
        return leftGrayImage;
    }

    public GrayImage getRightGrayImage() {
        return rightGrayImage;
    }

    @FXML
    private void openFileFired(ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        File file = fileChooser.showOpenDialog(imageprocessor.MainApp.primaryStage);

        if (file != null) {
            leftGrayImage = new GrayImage(file);
            rightGrayImage = null;
            displayFXI(leftGrayImage, "left");
            clearDisplay("right");
        }
    }

    @FXML
    private void saveFileFired(ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        File file = fileChooser.showSaveDialog(imageprocessor.MainApp.primaryStage);
        if (file != null) {
            Image img = iv2.getImage();
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void exitItemFired(ActionEvent event) {
        Platform.exit();
        MainApp.primaryStage.close();
    }

    /**
     * Moves the image from right pane to left pane. The left pane is the "processing stage," the right pane displays
     * results of processing.
     *
     * @param event button click
     */
    @FXML
    private void rightToLeftFired(ActionEvent event) {
        if (rightGrayImage != null) {
            leftGrayImage = rightGrayImage;
            displayFXI(leftGrayImage, "left");
            leftInfo.setText(rightInfo.getText());
            clearDisplay("right");
        }
    }

    @FXML
    private void grayResTabSelected() {
        bitDepthLabel.setText(Integer.toString(leftGrayImage.getBitDepth()));
    }

    @FXML
    private void buttonGrayResClicked() {
        if (leftGrayImage == null) {
            return;
        }
        GrayImage result = leftGrayImage.decreaseGrayRes();
        rightGrayImage = result;
        displayFXI(result, "right");
        String info = rightInfo.getText();
        info += "[Decreased bit depth]";
        rightInfo.setText(info);
    }

    /**
     * Converts GrayImage to WritableImage then displays it on specified side.
     * Updates labels and histograms to reflect change in display.
     *
     * @param gimg GrayImage to display
     * @param side Specifies which panel to display the GrayImage
     * @return WritableImage that was displayed
     */
    private WritableImage displayFXI(GrayImage gimg, String side) {
        try {
            WritableImage fxi = gimg.toFXImage();

            if (side.equals("left")) {
                clearDisplay("left");
                iv1.setImage(fxi);
                leftInfo.setText("" + gimg.getWidth() + "x" + gimg.getHeight() + ", " + gimg.getBitDepth() + "-bit ");
                leftGrayImage = gimg;
                if (gimg.getBitDepth() == 1) {
                    buttonGrayRes.setDisable(true);
                } else {
                    buttonGrayRes.setDisable(false);
                }
                bitDepthLabel.setText(Integer.toString(leftGrayImage.getBitDepth()));
                setSourceImgSizeLabel();
                setLeftHistogram();
                tp1.setVisible(true);
                leftHistPane.setVisible(true);
            }

            else if (side.equals("right")) {
                clearDisplay("right");
                iv2.setImage(fxi);
                rightInfo.setText("" + gimg.getWidth() + "x" + gimg.getHeight() + ", " + gimg.getBitDepth() + "-bit ");
                rightGrayImage = gimg;
                setRightHistogram();
                tp2.setVisible(true);
                rightHistPane.setVisible(true);
                right2LeftGroup.setVisible(true);
            }
            return fxi;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return new WritableImage(5,5);
    }

    /**
     * Converts BufferedImage to GrayImage, then calls displayFXI(GrayImage, side)
     *
     * @param bi BufferedImage to be displayed
     * @param side Specifies which panel to display the BufferedImage
     * @return WritableImage that was displayed
     */
    public WritableImage displayFXI(BufferedImage bi, String side) {
        GrayImage gimg = new GrayImage(bi);
        return displayFXI(gimg, side);
    }

    private void clearDisplay(String side) {
        if (side.equals("left")) {
            iv1.setImage(null);
            leftGrayImage = null;
            leftInfo.setText(" ");
            bitDepthLabel.setText(" ");
            setSourceImgSizeLabel();
            setLeftHistogram();
            leftHistPane.setVisible(false);
            tp1.setVisible(false);
        } else if (side.equals("right")) {
            iv2.setImage(null);
            rightGrayImage = null;
            rightInfo.setText(" ");
            setRightHistogram();
            rightHistPane.setVisible(false);
            tp2.setVisible(false);
            right2LeftGroup.setVisible(false);
        }
    }

    @FXML
    private void buttonLogTransClicked() {
        try {
            double c = Double.parseDouble(logTransC.getText());
            GrayImage result = leftGrayImage.logTrans(c);
            displayFXI(result, "right");
            String addedInfo = rightInfo.getText();
            addedInfo += " [Log transformation (c=" + c + ")]";
            rightInfo.setText(addedInfo);
        } catch (NumberFormatException e) {
            displayAlert("Invalid Input", "Please fix input value", "c must be a decimal number");
        }
    }

    @FXML
    private void buttonPowerTransClicked() {
        if (leftGrayImage == null) {
            return;
        }
        try {
            double c = Double.parseDouble(powerTransC.getText());
            double gamma = Double.parseDouble(powerTransGamma.getText());

            GrayImage result = leftGrayImage.powerTrans(c, gamma);
            displayFXI(result, "right");
            String addedInfo = rightInfo.getText();
            addedInfo += " [Power-law transformation (c=" + c + ", gamma=" + gamma + ")]";
            rightInfo.setText(addedInfo);
        } catch (NumberFormatException e) {
            displayAlert("Invalid Input", "Please fix input values", "c and gamma must be decimal numbers");
        }
    }

    @FXML
    private void setSourceImgSizeLabel() {
        String size = "";
        if (leftGrayImage != null) {
            size += leftGrayImage.getWidth() + "x" + leftGrayImage.getHeight();
        }
        sourceImgSizeLabel.setText(size);
    }

    @FXML
    private void button640Clicked(){
        buttonZoomClicked(640, 480);
    }

    @FXML
    private void button80Clicked() {
        buttonZoomClicked(80, 60);
    }

    private void buttonZoomClicked(int w, int h) {
        if (leftGrayImage == null) {
            return;
        }
        if (leftGrayImage.getWidth() != w || leftGrayImage.getHeight() != h) {
            List<String> choices = new ArrayList<>();
            choices.add("Nearest Neighbor");
            choices.add("Linear Interpolation");
            choices.add("Bilinear Interpolation");
            zoomChoiceDialog(choices, w, h);
        }
    }

    private String zoomChoiceDialog(List<String> choices, int w, int h) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("Zoom/Shrink");
        dialog.setHeaderText("Choose zoom/shrink method");
        dialog.setContentText("Zoom/shrink method: ");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(zoomMethod -> {
            String newInfo = "";
            switch (zoomMethod) {
                case "Linear Interpolation":
                    displayFXI(leftGrayImage.linearInterpolation(w, h), "right");
                    newInfo = rightInfo.getText();
                    newInfo += " [Spatial Resolution - Linear Interpolation ";
                    break;
                case "Nearest Neighbor":
                    displayFXI(leftGrayImage.nearestNeighbor(w, h), "right");
                    newInfo = rightInfo.getText();
                    newInfo += " [Spatial Resolution - Nearest Neighbor ";
                    break;
                case "Bilinear Interpolation":
                    displayFXI(leftGrayImage.bilinearInterpolation(w, h), "right");
                    newInfo = rightInfo.getText();
                    newInfo += " [Spatial Resolution - Bilinear Interpolation ";
                    break;
            }
            newInfo += "(" + w + "x" + h + ")]";
            rightInfo.setText(newInfo);
        });
        return null;
    }
    
    @FXML
    private void histEqButtonClicked() {
        if (leftGrayImage == null) {
            return;
        }

        if (histGlobal.isSelected()) {
            displayFXI(leftGrayImage.histogramEqGlobal(), "right");
            String newInfo = rightInfo.getText();
            newInfo += " [Global Histogram Equalization]";
            rightInfo.setText(newInfo);
        }
        else if (histLocal.isSelected()) {
            try {
                int filterSize = Integer.parseInt(histLocalFilterSize.getText());
                if ((filterSize % 2) == 0) {
                    throw new NumberFormatException("Must be odd integer");
                }
                displayFXI(leftGrayImage.histogramEqLocal(filterSize), "right");
                String newInfo = rightInfo.getText();
                newInfo += " [Local Histogram Equalization]";
                rightInfo.setText(newInfo);
            } catch (NumberFormatException e) {
                displayAlert("Invalid Input", "Please fix input value", "n needs to be an odd, positive integer");
            }
        }
    }

    @FXML
    private void histMatchingButtonFired() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Set Reference Image");
        File file = fileChooser.showOpenDialog(imageprocessor.MainApp.primaryStage);

        if (file != null) {
            GrayImage gimg = new GrayImage(file);
            displayFXI(leftGrayImage.histogramMatching(gimg), "right");
            String addedInfo = rightInfo.getText();
            addedInfo += " [Histogram Matching]";
            rightInfo.setText(addedInfo);
        }
    }

    @FXML
    private void setLeftHistogram() {
        leftHistogram.getData().clear();
        if (leftGrayImage != null) {
            GrayHistogram grayHist = new GrayHistogram(leftGrayImage);
            leftHistogram.getData().add(grayHist.getSeriesGray());
        }
    }

    @FXML
    private void setRightHistogram() {
        rightHistogram.getData().clear();
        if (rightGrayImage != null) {
            GrayHistogram grayHist = new GrayHistogram(rightGrayImage);
            rightHistogram.getData().add(grayHist.getSeriesGray());
        }
    }

    @FXML
    private void smoothingButtonFired() {
        try {
            int filterWidth = Integer.parseInt(smoothingWidth.getText());
            int filterHeight = Integer.parseInt(smoothingHeight.getText());
            if ((filterWidth % 2) == 0 || (filterHeight % 2) == 0) {
                throw new NumberFormatException("Must be odd integers");
            }
            BoxFilter smoother = new BoxFilter(filterWidth, filterHeight);
            displayFXI(smoother.runFilter(leftGrayImage), "right");
            String newInfo = rightInfo.getText();
            newInfo += " [Box Filter (" + filterWidth + "x" + filterHeight + " mask)]";
            rightInfo.setText(newInfo);
        } catch (NumberFormatException e) {
            displayAlert("Invalid Input", "Please fix input values", "Use odd, positive integers");
        }
    }
    
    @FXML
    private void medianButtonFired() {
        try {
            int filterWidth = Integer.parseInt(medianWidth.getText());
            int filterHeight = Integer.parseInt(medianHeight.getText());
            if ((filterWidth % 2) == 0 || (filterHeight % 2) == 0) {
                throw new NumberFormatException("Must be odd integers");
            }
            MedianFilter mf = new MedianFilter(filterWidth, filterHeight);
            displayFXI(mf.runFilter(leftGrayImage), "right");
            String newInfo = rightInfo.getText();
            newInfo += " [Median Filter (" + filterWidth + "x" + filterHeight + " mask)]";
            rightInfo.setText(newInfo);
        } catch (NumberFormatException e) {
            displayAlert("Invalid Input", "Please fix input values", "Use odd, positive integers");
        }
    }
    
    @FXML
    private void rankButtonFired() {
        try {
            String type = rankType.getValue();
            int filterWidth = Integer.parseInt(rankWidth.getText());
            int filterHeight = Integer.parseInt(rankHeight.getText());
            if ((filterWidth % 2) == 0 || (filterHeight % 2) == 0) {
                throw new NumberFormatException("Must be odd integers");
            }
            RankFilter rf = new RankFilter(type, filterWidth, filterHeight);
            displayFXI(rf.runFilter(leftGrayImage), "right");
            String newInfo = rightInfo.getText();
            newInfo += " [" + type + " Filter (" + filterWidth + "x" + filterHeight + " mask)]";
            rightInfo.setText(newInfo);
        } catch (NumberFormatException e) {
            displayAlert("Invalid Input", "Please fix input values", "Use odd, positive integers");
        }
    }

    @FXML
    private void laplacianButtonFired() {
        try {
            String laplacWidth = laplacianWidth.getValue();
            int filterWidth = Integer.parseInt(laplacWidth);
            if ((filterWidth % 2) == 0) {
                throw new NumberFormatException("Must be a positive, odd integer");
            }
            LaplacianFilter filter = new LaplacianFilter(filterWidth);
            displayFXI(filter.runFilter(leftGrayImage), "right");
            String newInfo = rightInfo.getText();
            newInfo += " [Laplacian Filter (" + filterWidth + "x" + filterWidth + " mask)]";
            rightInfo.setText(newInfo);
        } catch (NumberFormatException e) {
            displayAlert("Invalid Input", "Please fix input value", "Use odd, positive integer");
        }
    }

    @FXML
    private void highBoostButtonFired() {
        try {
            int filterWidth = Integer.parseInt(highBoostWidth.getText());
            int filterHeight = Integer.parseInt(highBoostHeight.getText());
            double filterAmount = Double.parseDouble(highBoostAmount.getText());
            if ((filterWidth % 2) == 0 || (filterHeight % 2) == 0) {
                throw new NumberFormatException("Must be odd integers");
            }
            HighBoostFilter hbf = new HighBoostFilter(filterWidth, filterHeight, filterAmount);
            displayFXI(hbf.runFilter(leftGrayImage), "right");
            String newInfo = rightInfo.getText();
            newInfo += " [HighBoost Filter (" + filterWidth + "x" + filterHeight + " mask)]";
            rightInfo.setText(newInfo);
        } catch (NumberFormatException e) {
            displayAlert("Invalid Input", "Please fix input values", "Height & Width: odd, positive integers" +
                                                                     "\nAmount: positive decimal");
        }
    }

    @FXML
    private void geoButtonFired() {
        try {
            int filterWidth = Integer.parseInt(geoWidth.getText());
            int filterHeight = Integer.parseInt(geoHeight.getText());
            if ((filterWidth % 2) == 0 || (filterHeight % 2) == 0) {
                throw new NumberFormatException("Must be odd integers");
            }
            GeoMeanFilter gmf = new GeoMeanFilter(filterWidth, filterHeight);
            displayFXI(gmf.runFilter(leftGrayImage), "right");
            String newInfo = rightInfo.getText();
            newInfo += " [Geometric Mean Filter (" + filterWidth + "x" + filterHeight + " mask)]";
            rightInfo.setText(newInfo);
        } catch (NumberFormatException e) {
            displayAlert("Invalid Input", "Please fix input values", "Use odd, positive integers");
        }
    }

    @FXML
    private void harmonicButtonFired() {
        try {
            int filterWidth = Integer.parseInt(harmonicWidth.getText());
            int filterHeight = Integer.parseInt(harmonicHeight.getText());
            if ((filterWidth % 2) == 0 || (filterHeight % 2) == 0) {
                throw new NumberFormatException("Must be odd integers");
            }
            HarmonicMeanFilter hf = new HarmonicMeanFilter(filterWidth, filterHeight);
            displayFXI(hf.runFilter(leftGrayImage), "right");
            String newInfo = rightInfo.getText();
            newInfo += " [Harmonic Mean Filter (" + filterWidth + "x" + filterHeight + " mask)]";
            rightInfo.setText(newInfo);
        } catch (NumberFormatException e) {
            displayAlert("Invalid Input", "Please fix input values", "Use odd, positive integers");
        }
    }

    @FXML
    private void contraHarmonicButtonFired() {
        try {
            int filterWidth = Integer.parseInt(contraHarmonicWidth.getText());
            int filterHeight = Integer.parseInt(contraHarmonicHeight.getText());
            double q = Double.parseDouble(contraHarmonicQ.getText());
            if ((filterWidth % 2) == 0 || (filterHeight % 2) == 0) {
                throw new NumberFormatException("Must be odd integers");
            }
            ContraHarmonicMeanFilter chmf = new ContraHarmonicMeanFilter(filterWidth, filterHeight, q);
            displayFXI(chmf.runFilter(leftGrayImage), "right");
            String newInfo = rightInfo.getText();
            newInfo += " [ContraHarmonic Mean Filter (" + filterWidth + "x" + filterHeight + " mask, Q = " + q + ")]";
            rightInfo.setText(newInfo);
        } catch (NumberFormatException e) {
            displayAlert("Invalid Input", "Please fix input values", "Use odd, positive integers." +
                        "\nQ must be a decimal number.");
        }
    }

    @FXML
    private void alphaButtonFired() {
        try {
            int filterWidth = Integer.parseInt(alphaWidth.getText());
            int filterHeight = Integer.parseInt(alphaHeight.getText());
            int filterD = Integer.parseInt(alphaD.getText());
            if ((filterWidth % 2) == 0 || (filterHeight % 2) == 0) {
                throw new NumberFormatException("Must be odd integers");
            }
            if ((filterD % 2) != 0) {
                throw new NumberFormatException("d Must be even integer");
            }
            AlphaTrimmedMeanFilter atf = new AlphaTrimmedMeanFilter(filterWidth, filterHeight, filterD);
            displayFXI(atf.runFilter(leftGrayImage), "right");
            String newInfo = rightInfo.getText();
            newInfo += " [AlphaTrimmed Mean Filter (" + filterWidth + "x" + filterHeight + " mask, d=" + filterD + "]";
            rightInfo.setText(newInfo);
        } catch (NumberFormatException e) {
            displayAlert("Invalid Input", "Please fix input values", "Width & Height are odd, positive integers" +
                        "\nd is an even, positive integer");
        }
    }
    
    
    private void displayAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
    }
    

    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        tp1.setVisible(false);
        tp2.setVisible(false);
        leftHistPane.setVisible(false);
        rightHistPane.setVisible(false);



        right2LeftGroup.setVisible(false);

        histGlobal.setUserData("global");
        histLocal.setUserData("local");

        laplacianWidth.setItems(FXCollections.observableArrayList("3", "5", "7"));
        laplacianWidth.setValue("3");

        rankType.setItems(FXCollections.observableArrayList("Median", "Max", "Min", "Midpoint"));
        rankType.setValue("Median");

        histEqToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (histEqToggleGroup.getSelectedToggle() != null) {
                String a = (histEqToggleGroup.getSelectedToggle().getUserData().toString());
                if (a.equals("global")) {
                    histGlobal.setSelected(true);
                    histLocalGroup.setDisable(true);
                    histLocalGroup2.setDisable(true);
                    histLocalGroup.setVisible(false);
                    histLocalGroup2.setVisible(false);
                    histEqButton.setDisable(false);
                }
                else if (a.equals("local")) {
                    histLocal.setSelected(true);
                    histLocalGroup.setVisible(true);
                    histLocalGroup2.setVisible(true);
                    histLocalGroup.setDisable(false);
                    histLocalGroup2.setDisable(false);
                    histEqButton.setDisable(false);
                }

            }
        });
    }

}
