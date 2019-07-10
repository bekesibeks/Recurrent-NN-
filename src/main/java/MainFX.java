import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import math.utils.ConvolutionUtils;
import math.utils.MatrixUtils;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainFX extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    double[][] boxBlurKernel = {
            {1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0},
            {1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0},
            {1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0}};

    double[][] gaussianBlurKernel = {
            {1, 2, 1},
            {2, 4, 2},
            {1, 2, 1}};

    double[][] identityKernel = {
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0}};

    double[][] edgeDetectionKernel = {
            {-1, -1, -1},
            {-1, 8, -1},
            {-1, -1, -1}};

    double[][] embossKernel = {
            {-2, -1, 0},
            {-1, 1, 1},
            {0, 1, 2}};

    @Override
    public void start(Stage primaryStage) {
        gaussianBlurKernel = MatrixUtils.dotProduct(gaussianBlurKernel, 1.0 / 16.0);

        primaryStage.setTitle("Hello World!");

        Group mainGroup = new Group();

        Image sourceImage = new Image(MainFX.class.getResourceAsStream("/source_image_for_conv.jpg"));
        ImageView imageView = new ImageView();
        imageView.setImage(sourceImage);


        int dimension = (int) imageView.getImage().getWidth();

        double[][] redLayer = new double[dimension][dimension];
        double[][] blueLayer = new double[dimension][dimension];
        double[][] greenLayer = new double[dimension][dimension];

        WritableImage writableImage = new WritableImage(dimension, dimension);
        PixelWriter targetImage = writableImage.getPixelWriter();

        for (int x = 0; x < imageView.getImage().getWidth(); x++) {
            for (int y = 0; y < imageView.getImage().getHeight(); y++) {
                Color sourceColor = imageView.getImage().getPixelReader().getColor(x, y);
                redLayer[x][y] = sourceColor.getRed();
                blueLayer[x][y] = sourceColor.getBlue();
                greenLayer[x][y] = sourceColor.getGreen();
            }
        }


        int strideForRed = 1;
        int strideForGreen = 2;
        int strideForBlue = 3;

        double[][] convolvedRedValues = ConvolutionUtils.convolution(redLayer, edgeDetectionKernel, strideForRed);
        double[][] convolvedGreenValues = ConvolutionUtils.convolution(greenLayer, boxBlurKernel, strideForGreen);
        double[][] convolvedBlueValues = ConvolutionUtils.convolution(blueLayer, identityKernel, strideForBlue);

        // TODO -> more sophisticated solution
        List<double[][]> colorDimensions = new ArrayList<>();
        colorDimensions.add(convolvedBlueValues);
        colorDimensions.add(convolvedRedValues);
        colorDimensions.add(convolvedBlueValues);


        for (int x = 0; x < convolvedRedValues.length; x++) {
            for (int y = 0; y < convolvedRedValues[0].length; y++) {
                //  normalise colors to 0-1
                double redValue = Math.max(Math.min(convolvedRedValues[x][y], 1), 0);
                double greenValue = Math.max(Math.min(convolvedGreenValues[x][y], 1), 0);
                double blueValue =  Math.max(Math.min(convolvedBlueValues[x][y], 1), 0);

                targetImage.setColor(x, y, Color.color(redValue, greenValue, blueValue, 1));
            }
        }


        mainGroup.getChildren().add(imageView);

        ImageView writableImageView = new ImageView(writableImage);
        writableImageView.setTranslateX(500);
        mainGroup.getChildren().add(writableImageView);

        StackPane root = new StackPane();
        root.getChildren().add(mainGroup);
        primaryStage.setScene(new Scene(root, 1000, 410));
        primaryStage.show();

        WritableImage image = root.snapshot(new SnapshotParameters(), null);

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        File file = new File("test.png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}