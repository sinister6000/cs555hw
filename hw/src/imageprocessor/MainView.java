package imageprocessor;

import imageprocessor.model.GrayHistogram;
import imageprocessor.model.GrayImage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.awt.image.BufferedImage;
import java.io.File;
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
    private AnchorPane center;
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
    private ImageView iv1;
    @FXML
    private ImageView iv2;
    @FXML
    private Label leftInfo;
    @FXML
    private Label rightInfo;
    @FXML
    private Label bitDepthLabel;
    @FXML
    private Button buttonGrayRes;
    @FXML
    private javafx.scene.control.TextField logTransC;
    @FXML
    private Button buttonLogTrans;
    @FXML
    private javafx.scene.control.TextField powerTransC;
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
    private TextField histLocalFilterSize;
    @FXML
    private AnchorPane leftHistPane;
    @FXML
    private AnchorPane rightHistPane;

    public GrayImage getLeftGrayImage() {
        return leftGrayImage;
    }

    public GrayImage getRightGrayImage() {
        return rightGrayImage;
    }

    @FXML
    private void openFileFired(ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\imgs"));
        fileChooser.setTitle("Open Image");
        File file = fileChooser.showOpenDialog(imageprocessor.MainApp.primaryStage);

        if (file != null) {
            GrayImage gimg = new GrayImage(file);
            leftGrayImage = gimg;
            rightGrayImage = null;
            displayFXI(leftGrayImage, "left");
            clearDisplay("right");
        }
    }

    @FXML
    private void saveFileFired(ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\imgs"));
        fileChooser.setTitle("Save Image");
        File file = fileChooser.showSaveDialog(imageprocessor.MainApp.primaryStage);
        if (file != null) {
            rightGrayImage.toFile(file);
        }
    }

    @FXML
    private void exitItemFired(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void rightToLeftFired(ActionEvent event) {
        if (rightGrayImage != null) {
            leftGrayImage = rightGrayImage;
            displayFXI(leftGrayImage, "left");
            leftInfo.setText(rightInfo.getText());
            clearDisplay("right");

            setLeftHistogram();
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
        WritableImage fxi = gimg.toFXImage();

        if (side.equals("left")) {
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
        }

        else if (side.equals("right")) {
            iv2.setImage(fxi);
            rightInfo.setText("" + gimg.getWidth() + "x" + gimg.getHeight() + ", " + gimg.getBitDepth() + "-bit ");
            rightGrayImage = gimg;
            setRightHistogram();
        }
        return fxi;
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
        }

        else if (side.equals("right")) {
            iv2.setImage(null);
            rightGrayImage = null;
            rightInfo.setText(" ");
            setRightHistogram();
        }
    }

    @FXML
    private void buttonLogTransClicked() {
        if (leftGrayImage == null) {
            return;
        }
        String cText = logTransC.getText();
        if (cText.equals("")) {
            System.out.println("wrong type for c");
            /**
             * TODO: make a popup alert about value of c.
             */
        }

        else {
            double c = Double.parseDouble(cText);
            GrayImage result = leftGrayImage.logTrans(c);
            displayFXI(result, "right");
            String addedInfo = rightInfo.getText();
            addedInfo += " [Log transformation (c=" + c + ")]";
            rightInfo.setText(addedInfo);
        }
    }

    @FXML
    private void buttonPowerTransClicked() {
        if (leftGrayImage == null) {
            return;
        }
        String cText = powerTransC.getText();
        String gammaText = powerTransGamma.getText();
        if (cText.equals("") || gammaText.equals("")) {
            System.out.println("c and gamma need to be numbers");
            /**
             * TODO: make a popup alert about value of c/gamma.
             */
        } else {
            try {
                double c = Double.parseDouble(cText);
                double gamma = Double.parseDouble(gammaText);

                GrayImage result = leftGrayImage.powerTrans(c, gamma);
                displayFXI(result, "right");
                String addedInfo = rightInfo.getText();
                addedInfo += " [Power-law transformation (c=" + c + ", gamma=" + gamma + ")]";
                rightInfo.setText(addedInfo);

            } catch (NumberFormatException e) {
                System.out.println("c and gamma need to be numbers.");
                /**
                 * TODO: add alert screen
                 */
            }
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
            java.util.List<String> choices = new ArrayList<>();
            choices.add("Replication");
            choices.add("Nearest Neighbor");
            choices.add("Bilinear interpolation");
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
            if (zoomMethod.equals("Replication")) {
                displayFXI(leftGrayImage.zoomReplication(w, h), "right");
                newInfo = rightInfo.getText();
                newInfo += " [Spatial Resolution - Replication ";
            }
            else if (zoomMethod.equals("Nearest Neighbor")) {
                displayFXI(leftGrayImage.nearestNeighbor(w, h), "right");
                newInfo = rightInfo.getText();
                newInfo += " [Spatial Resolution - Nearest Neighbor ";
            }
            else if (zoomMethod.equals("Bilinear interpolation")) {
                displayFXI(leftGrayImage.bilinearInterpolation(w, h), "right");
                newInfo = rightInfo.getText();
                newInfo += " [Spatial Resolution - Bilinear Interpolation ";
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
            rightInfo.setText(newInfo);;
        }
        else if (histLocal.isSelected()) {
            try {
                int filterSize = Integer.parseInt(histLocalFilterSize.getText());
                displayFXI(leftGrayImage.histogramEqLocal(filterSize), "right");
                String newInfo = rightInfo.getText();
                newInfo += " [Local Histogram Equalization]";
                rightInfo.setText(newInfo);
            } catch (NumberFormatException e) {
                /**
                 * TODO: alert screen
                 */
            }
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


    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        histGlobal.setUserData("global");
        histLocal.setUserData("local");

        histEqToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (histEqToggleGroup.getSelectedToggle() != null) {
                String a = (histEqToggleGroup.getSelectedToggle().getUserData().toString());
                if (a.equals("global")) {
                    histGlobal.setSelected(true);
                    histLocalGroup.setDisable(true);
                    histEqButton.setDisable(false);
                }
                else if (a.equals("local")) {
                    histLocal.setSelected(true);
                    histLocalGroup.setDisable(false);
                    histEqButton.setDisable(false);
                }

            }
        });
    }

}
