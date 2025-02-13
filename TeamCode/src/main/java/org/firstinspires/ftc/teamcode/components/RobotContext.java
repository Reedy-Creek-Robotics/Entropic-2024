package org.firstinspires.ftc.teamcode.components;

import com.acmerobotics.roadrunner.localization.Localizer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.roadrunner.drive.AprilTagLocalizer;
import org.firstinspires.ftc.teamcode.roadrunner.drive.OpticalLocalizer;
import org.firstinspires.ftc.teamcode.util.DriveUtil;
import org.firstinspires.ftc.teamcode.util.MecanumUtil;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

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

    public OpMode opMode;

    public ElapsedTime clock;
    public String record;

    public RobotDescriptor descriptor;

    public Localizer localizer;

    public VisionPortal frontPortal, sidePortal;
    public VisionPortal.Builder frontPortalBuilder, sidePortalBuilder;
    public AprilTagProcessor frontAprilTagProcessor, sideAprilTagProcessor;

    public AprilTagLocalizer aprilTagLocalizer;
    public OpticalLocalizer opticalLocalizer;

    private Position frontCameraPosition = new Position(DistanceUnit.MM,
             160.292, 180.467,384.632, 0);
    private YawPitchRollAngles frontCameraOrientation = new YawPitchRollAngles(AngleUnit.DEGREES,
            -15-90, -120, 0, 0);

    private Position sideCameraPostion = new Position(DistanceUnit.MM,
            0,0,0,0);
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

        //Processors
        this.frontAprilTagProcessor = new AprilTagProcessor.Builder()
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .setCameraPose(frontCameraPosition, frontCameraOrientation)
                .setLensIntrinsics(237.835,237.835,328.272,237.727)
                .build();
        this.sideAprilTagProcessor = new AprilTagProcessor.Builder()
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .setCameraPose(sideCameraPostion, sideCameraOrientation)
                .setLensIntrinsics(0,0,0,0)
                .build();

        //Builders
        this.frontPortalBuilder = new VisionPortal.Builder();
        this.frontPortalBuilder.setCamera(frontWebcam);
        this.frontPortalBuilder.setStreamFormat(VisionPortal.StreamFormat.MJPEG);
        this.frontPortalBuilder.addProcessor(frontAprilTagProcessor);

        this.sidePortalBuilder = new VisionPortal.Builder();
        this.sidePortalBuilder.setCamera(sideWebcam);
        this.sidePortalBuilder.addProcessor(sideAprilTagProcessor);

        //Portal
        this.frontPortal = frontPortalBuilder.build();
        this.sidePortal = sidePortalBuilder.build();

        //this.localizer = new OpticalAprilTagLocalizer(this, cameraPosition, cameraOrientation, webcam);
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

    public Localizer getAprilTagLocalizer() {return aprilTagLocalizer;}

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
