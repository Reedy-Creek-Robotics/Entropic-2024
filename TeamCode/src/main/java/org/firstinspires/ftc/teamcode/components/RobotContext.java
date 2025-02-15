package org.firstinspires.ftc.teamcode.components;

import android.util.Size;

import com.acmerobotics.roadrunner.localization.Localizer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.roadrunner.drive.AprilTagLocalizer;
import org.firstinspires.ftc.teamcode.roadrunner.drive.OpticalAprilTagLocalizer;
import org.firstinspires.ftc.teamcode.roadrunner.drive.OpticalLocalizer;
import org.firstinspires.ftc.teamcode.util.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.teamcode.util.ColorRange;
import org.firstinspires.ftc.teamcode.util.ColorSpace;
import org.firstinspires.ftc.teamcode.util.DriveUtil;
import org.firstinspires.ftc.teamcode.util.ImageRegion;
import org.firstinspires.ftc.teamcode.util.MecanumUtil;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.List;

public class RobotContext {
    public static double topLim = -0.25;
    public static double bottomLim = -0.9;
    public static double region0LeftLim = -0.6;
    public static double region0RightLim = -0.2;
    public static double region1LeftLim = -0.2;
    public static double region1RightLim = 0.2;
    public static double region2LeftLim = 0.2;
    public static double region2RightLim = 0.6;

    public enum Alliance{
        RED(0,1),
        BLUE(180,-1);

        double rotation;
        int translation;

        Alliance(double rotation, int translation) {
            this.rotation = rotation;
            this.translation = translation;
        }

        public double getRotation() {
            return rotation;
        }

        public int getTranslation() {
            return translation;
        }
    }

    ColorRange betterYELLOW = new ColorRange(
            ColorSpace.HSV,
            new Scalar(15,111,125),
            new Scalar(50,255,255)
    );

    public OpMode opMode;

    public ElapsedTime clock;
    public String record;

    public RobotDescriptor descriptor;

    public OpticalLocalizer localizer;

    public VisionPortal frontPortal, sidePortal;
    public AprilTagProcessor frontAprilTagProcessor, sideAprilTagProcessor;
    ColorBlobLocatorProcessor teamLocator, yellowLocator;

    public OpticalLocalizer opticalLocalizer;

    public static double camera_width = 640;
    public static double camera_height = 480;

    private Position frontCameraPosition = new Position(DistanceUnit.MM,
             160.292, 180.467,384.632, 0);
    private YawPitchRollAngles frontCameraOrientation = new YawPitchRollAngles(AngleUnit.DEGREES,
            -15-90, -120, 0, 0);

    private Position sideCameraPostion = new Position(DistanceUnit.MM,
            214.071,38.961,158.5,0);
    private YawPitchRollAngles sideCameraOrientation = new YawPitchRollAngles(AngleUnit.DEGREES,
            0,-90,0,0);

    public DriveUtil driveUtil;

    public Alliance alliance;

    public WebcamName frontWebcam, sideWebcam;

    List<Integer> lastTrackingEncPositions = new ArrayList<>();
    List<Integer> lastTrackingEncVels = new ArrayList<>();
    public RobotContext(OpMode opMode, RobotDescriptor descriptor) {
        this.opMode = opMode;
        this.descriptor = descriptor;
        this.driveUtil = new MecanumUtil();
        this.frontWebcam = opMode.hardwareMap.get(WebcamName.class, "Front Webcam");
        this.sideWebcam = opMode.hardwareMap.get(WebcamName.class, "Side Webcam");
        this.clock = new ElapsedTime();

        int[] viewIds = VisionPortal.makeMultiPortalView(2, VisionPortal.MultiPortalLayout.VERTICAL);

        // We extract the two view IDs from the array to make our lives a little easier later.
        // NB: the array is 2 long because we asked for 2 portals up above.
        int portal1ViewId = viewIds[0];
        int portal2ViewId = viewIds[1];

        //Processors
        this.frontAprilTagProcessor = new AprilTagProcessor.Builder()
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .setCameraPose(frontCameraPosition, frontCameraOrientation)
                .setLensIntrinsics(237.835,237.835,328.272,237.727)
                .build();
        this.sideAprilTagProcessor = new AprilTagProcessor.Builder()
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .setCameraPose(sideCameraPostion, sideCameraOrientation)
                .setLensIntrinsics(456.881,456.881,310.532 ,243.914)
                .build();
        this.teamLocator = new ColorBlobLocatorProcessor.Builder()
                .setTargetColorRange(getAlliance() == RobotContext.Alliance.RED ? ColorRange.RED : ColorRange.BLUE)         // use a predefined color match
                .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)    // exclude blobs inside blobs
                .setRoi(ImageRegion.asUnityCenterCoordinates(-1, 0, 1, -1))  // search central 1/4 of camera view
                .setDrawContours(true)                        // Show contours on the Stream Preview
                .setBlurSize(5)                               // Smooth the transitions between different colors in image
                .setDilateSize(10)
                .setErodeSize(10)
                .build();
        this.yellowLocator = new ColorBlobLocatorProcessor.Builder()
                .setTargetColorRange(betterYELLOW)//context.getAlliance() == RobotContext.Alliance.RED ? ColorRange.RED : ColorRange.BLUE)         // use a predefined color match
                .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)    // exclude blobs inside blobs
                .setRoi(ImageRegion.asUnityCenterCoordinates(-1, 0, 1, -1))  // search central 1/4 of camera view
                .setDrawContours(true)                        // Show contours on the Stream Preview
                .setBlurSize(5)                               // Smooth the transitions between different colors in image
                .setDilateSize(10)
                .setErodeSize(10)
                .build();

        //Portal
        this.frontPortal = new VisionPortal.Builder()
                .setCamera(frontWebcam)
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .addProcessors(frontAprilTagProcessor, teamLocator, yellowLocator)
                .setCameraResolution(new Size((int) camera_width, (int) camera_height))
                .setLiveViewContainerId(portal1ViewId)
                .build();
        this.sidePortal = new VisionPortal.Builder()
                .setCamera(sideWebcam)
                .addProcessors(sideAprilTagProcessor)
                .setLiveViewContainerId(portal2ViewId)
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .build();

        //this.localizer = new OpticalAprilTagLocalizer(this);
        this.localizer = new OpticalLocalizer(this);
        //new StandardTrackingWheelLocalizer(opMode.hardwareMap, lastTrackingEncPositions, lastTrackingEncVels, this.descriptor.ODOMETRY_TUNER);
        //new TwoWheelTrackingLocalizer(opMode.hardwareMap,this.descriptor);

        this.alliance = Alliance.RED; //blue is negative one, red is positive one

        this.record = "";
    }

    public OpMode getOpMode() {
        return opMode;
    }

    public RobotDescriptor getDescriptor() {
        return descriptor;
    }

    public Localizer getLocalizer() {
        return localizer;
    }

    public DriveUtil getDriveUtil() {
        return driveUtil;
    }

    public Alliance getAlliance() {
        return alliance;
    }

    public void setAlliance(Alliance alliance) {
        this.alliance = alliance;
    }

    public WebcamName getFrontWebcam() {
        return frontWebcam;
    }

    /*public enum Alliance{
        BLUE(1,-90),
        RED(-1,90);

        int value;
        int rotation;

        Alliance(int value, int rotation) {
            this.value = value;
            this.rotation = rotation;
        }

        public int getValue() {
            return value;
        }

        public int getRotation() {
            return rotation;
        }
    }*/

    /**
     * Represents A component that knows how to obtain the robot's current position.
     */


}
