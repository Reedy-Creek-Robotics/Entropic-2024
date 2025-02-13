package org.firstinspires.ftc.teamcode.components;

import android.util.Size;

import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.qualcomm.robotcore.util.SortOrder;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.teamcode.util.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.teamcode.util.ColorRange;
import org.firstinspires.ftc.teamcode.util.ImageRegion;
import org.firstinspires.ftc.teamcode.util.ColorSpace;
import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MachineVisionSubmersible extends BaseComponent{

    ColorBlobLocatorProcessor teamLocator;
    ColorBlobLocatorProcessor yellowLocator;
    VisionPortal portal;
    public static double camera_width = 640;
    public static double camera_height = 480;
    int numInside;
    //(5,169,109), (31,255,255)
    ColorRange betterYELLOW = new ColorRange(
            ColorSpace.HSV,
            new Scalar(15,111,125),
            new Scalar(50,255,255)
    );

    public MachineVisionSubmersible(RobotContext context) {
        super(context);
        teamLocator = new ColorBlobLocatorProcessor.Builder()
                .setTargetColorRange(context.getAlliance() == RobotContext.Alliance.RED ? ColorRange.RED : ColorRange.BLUE)         // use a predefined color match
                .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)    // exclude blobs inside blobs
                .setRoi(ImageRegion.asUnityCenterCoordinates(-1, 0, 1, -1))  // search central 1/4 of camera view
                .setDrawContours(true)                        // Show contours on the Stream Preview
                .setBlurSize(5)                               // Smooth the transitions between different colors in image
                .setDilateSize(10)
                .setErodeSize(10)
                .build();
        yellowLocator = new ColorBlobLocatorProcessor.Builder()
                .setTargetColorRange(betterYELLOW)//context.getAlliance() == RobotContext.Alliance.RED ? ColorRange.RED : ColorRange.BLUE)         // use a predefined color match
                .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)    // exclude blobs inside blobs
                .setRoi(ImageRegion.asUnityCenterCoordinates(-1, 0, 1, -1))  // search central 1/4 of camera view
                .setDrawContours(true)                        // Show contours on the Stream Preview
                .setBlurSize(5)                               // Smooth the transitions between different colors in image
                .setDilateSize(10)
                .setErodeSize(10)
                .build();

        portal = new VisionPortal.Builder()
                .addProcessors(teamLocator, yellowLocator)
                .setCameraResolution(new Size((int) camera_width, (int) camera_height))
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
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

    public Integer getNumInsideRegion(List<ColorBlobLocatorProcessor.Blob> blobs, double regionTop, double regionBottom, double regionLeft, double regionRight){
        numInside = 0;
        for (ColorBlobLocatorProcessor.Blob blob: blobs){
            double x = blob.getBoxFit().center.x;
            double y = blob.getBoxFit().center.y;
            //telemetry.addData("((regionLeft * camera_width/2) + camera_width/2): ", (regionLeft * camera_width/2) + camera_width/2);
            //telemetry.addData("((regionRight * camera_width/2) + camera_width/2): ", (regionRight * camera_width/2) + camera_width/2);
            //telemetry.addData("((regionTop * camera_height/2) + camera_height/2): ", (-regionTop * camera_height/2) + camera_height/2);
            //telemetry.addData("((regionBottom * camera_height/2) + camera_height/2): ", (-regionBottom * camera_height/2) + camera_height/2);
            if ((x >= (regionLeft * camera_width/2) + camera_width/2) && (x <= (regionRight * camera_width/2) + camera_width/2) && (y >= (-regionTop * camera_height/2) + camera_height/2) && (y <= (-regionBottom * camera_height/2) + camera_height/2)) {
                numInside++;
            }
        }
        return numInside;
    }

    public List<List<Integer>> getElementCounts(){
        List<List<Integer>> counts = new ArrayList<>();
        counts.add(new ArrayList<>());
        counts.add(new ArrayList<>());

        if(getBlobs(teamLocator) != null) {
            counts.get(0).add(getNumInsideRegion(getBlobs(teamLocator), RobotContext.topLim, RobotContext.bottomLim, RobotContext.region0LeftLim, RobotContext.region0RightLim));
            counts.get(0).add(getNumInsideRegion(getBlobs(teamLocator), RobotContext.topLim, RobotContext.bottomLim, RobotContext.region1LeftLim, RobotContext.region1RightLim));
            counts.get(0).add(getNumInsideRegion(getBlobs(teamLocator), RobotContext.topLim, RobotContext.bottomLim, RobotContext.region2LeftLim, RobotContext.region2RightLim));
        } else {telemetry.addLine("teamLocator null");counts.set(0, Stream.of(0,0,0).collect(Collectors.toList()));}
        if(getBlobs(yellowLocator) != null) {
            counts.get(1).add(getNumInsideRegion(getBlobs(yellowLocator), RobotContext.topLim, RobotContext.bottomLim, RobotContext.region0LeftLim, RobotContext.region0RightLim));
            counts.get(1).add(getNumInsideRegion(getBlobs(yellowLocator), RobotContext.topLim, RobotContext.bottomLim, RobotContext.region1LeftLim, RobotContext.region1RightLim));
            counts.get(1).add(getNumInsideRegion(getBlobs(yellowLocator), RobotContext.topLim, RobotContext.bottomLim, RobotContext.region2LeftLim, RobotContext.region2RightLim));
        } else {telemetry.addLine("yellowLocator null");counts.set(1, Stream.of(0,0,0).collect(Collectors.toList()));}
        return counts;
    }

    public Pose2d blobToPos(ColorBlobLocatorProcessor.Blob bestElement){
        Pose2d elementPos;
        //pixel to camera ray
        double pixelX = bestElement.getBoxFit().center.x;
        double pixelY = 1080 - bestElement.getBoxFit().center.y;
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
            telemetry.addLine("No intersection with ground");
            elementPos = null;
        }

        return elementPos;
    }

    public List<ColorBlobLocatorProcessor.Blob> getBlobs(ColorBlobLocatorProcessor colorLocator) {
        ColorBlobLocatorProcessor.Blob bestElement;

        List<ColorBlobLocatorProcessor.Blob> blobs = colorLocator.getBlobs();
        ColorBlobLocatorProcessor.Util.filterByArea(300, 50000, blobs);  //15000
        ColorBlobLocatorProcessor.Util.sortByArea(SortOrder.DESCENDING, blobs);
        if (!blobs.isEmpty()) {
            bestElement = blobs.get(0);
            //telemetry.addData("Blob Center x: ", bestElement.getBoxFit().center.x);
            //telemetry.addData("Blob Center y: ", bestElement.getBoxFit().center.y);
        } else {
            telemetry.addLine("No Blob Found");
            return null;
        }
        return blobs;
    }

}
