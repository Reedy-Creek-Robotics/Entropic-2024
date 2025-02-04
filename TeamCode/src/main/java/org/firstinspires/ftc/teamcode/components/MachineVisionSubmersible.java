package org.firstinspires.ftc.teamcode.components;

import android.util.Size;

import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.SortOrder;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ImageRegion;

import java.util.List;

public class MachineVisionSubmersible extends BaseComponent{

    ColorBlobLocatorProcessor colorLocator;
    VisionPortal portal;
    double camera_width = 1920;
    double camera_height = 1080;

    public MachineVisionSubmersible(RobotContext context) {
        super(context);
        colorLocator = new ColorBlobLocatorProcessor.Builder()

                .setTargetColorRange(context.getAlliance() == RobotContext.Alliance.RED ? ColorRange.RED : ColorRange.BLUE)         // use a predefined color match
                .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)    // exclude blobs inside blobs
                .setRoi(ImageRegion.asUnityCenterCoordinates(-0.5, 0.5, 0.5, -0.5))  // search central 1/4 of camera view
                .setDrawContours(true)                        // Show contours on the Stream Preview
                .setBlurSize(5)                               // Smooth the transitions between different colors in image
                .setDilateSize(10)
                .setErodeSize(10)
                .build();

        portal = new VisionPortal.Builder()
                .addProcessor(colorLocator)
                .setCameraResolution(new Size((int) camera_width, (int) camera_height))
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .build();
    }

    @Override
    public void init() {

    }

    private static double[] normalize(double[] vector) {
        double length = Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1] + vector[2] * vector[2]);
        return new double[]{vector[0] / length, vector[1] / length, vector[2] / length};
    }

    private static double[] matrixVectorMultiply(double[][] matrix, double[] vector) {
        double[] result = new double[3];
        for (int i = 0; i < 3; i++) {
            result[i] = matrix[i][0] * vector[0] + matrix[i][1] * vector[1] + matrix[i][2] * vector[2];
        }
        return result;
    }

    private static double[][] matrixMultiply(double[][] A, double[][] B) {
        double[][] result = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result[i][j] = A[i][0] * B[0][j] + A[i][1] * B[1][j] + A[i][2] * B[2][j];
            }
        }
        return result;
    }

    public static double[][] calculateRotationMatrix(double tiltDownDeg, double turnRightDeg) {
        double tiltDownRad = Math.toRadians(tiltDownDeg);
        double turnRightRad = Math.toRadians(turnRightDeg);

        double[][] R_x = {
                {1, 0, 0},
                {0, Math.cos(tiltDownRad), -Math.sin(tiltDownRad)},
                {0, Math.sin(tiltDownRad), Math.cos(tiltDownRad)}
        };

        double[][] R_y = {
                {Math.cos(turnRightRad), 0, Math.sin(turnRightRad)},
                {0, 1, 0},
                {-Math.sin(turnRightRad), 0, Math.cos(turnRightRad)}
        };

        return matrixMultiply(R_y, R_x);
    }

    public static double[] findRayGroundIntersection(double[] rayWorld, double[] rayOrigin) {
        double planeY = 0.0;

        if (rayWorld[1] == 0) return null;

        double t = (planeY - rayOrigin[1]) / rayWorld[1];
        if (t < 0) return null;

        return new double[]{rayOrigin[0] + t * rayWorld[0], rayOrigin[1] + t * rayWorld[1], rayOrigin[2] + t * rayWorld[2]};
    }

    public Pose2d runPipeline(){
        Pose2d elementPos;


        List<ColorBlobLocatorProcessor.Blob> blobs = colorLocator.getBlobs();
        ColorBlobLocatorProcessor.Util.filterByArea(15000, 500000, blobs);  //15000
        ColorBlobLocatorProcessor.Util.sortByArea(SortOrder.DESCENDING, blobs);

        ColorBlobLocatorProcessor.Blob bestElement = blobs.get(0);

        //pixel to camera ray
        double pixelX = bestElement.getBoxFit().center.x;
        double pixelY = bestElement.getBoxFit().center.y;
        double focalLength = 238;
        double cameraHeight = 15.157;
        double tiltDown = 30;
        double tiltRight = 15;
        double cx = camera_width /2;
        double cy = camera_height /2;


        double ndcX = (pixelX - cx) / focalLength;
        double ndcY = (pixelY - cy) / focalLength;

        double[] rayCamera = {ndcX, ndcY, 1.0};
        rayCamera = normalize(rayCamera);

        double[] cameraOrigin = {0,cameraHeight,0};
        double[][] cameraRotation = calculateRotationMatrix(tiltDown,tiltRight);

        double[] rayWorld = matrixVectorMultiply(cameraRotation, rayCamera);
        rayWorld = normalize(rayWorld);

        double[] intersection = findRayGroundIntersection(rayWorld, cameraOrigin);

        if (intersection != null) {
            elementPos = new Pose2d(intersection[2], intersection[0], new Rotation2d(bestElement.getBoxFit().angle));
        } else {
            elementPos = null;
        }

        return elementPos;

    }

}
