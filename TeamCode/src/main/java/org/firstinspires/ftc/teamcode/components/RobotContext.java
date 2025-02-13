package org.firstinspires.ftc.teamcode.components;

import com.acmerobotics.roadrunner.localization.Localizer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.opmodes.AutoMain;
import org.firstinspires.ftc.teamcode.roadrunner.drive.AprilTagLocalizer;
import org.firstinspires.ftc.teamcode.roadrunner.drive.OpticalAprilTagLocalizer;
import org.firstinspires.ftc.teamcode.roadrunner.drive.OpticalLocalizer;
import org.firstinspires.ftc.teamcode.roadrunner.drive.StandardTrackingWheelLocalizer;
import org.firstinspires.ftc.teamcode.roadrunner.drive.TwoWheelTrackingLocalizer;
import org.firstinspires.ftc.teamcode.util.DriveUtil;
import org.firstinspires.ftc.teamcode.util.MecanumUtil;

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

    public AprilTagLocalizer aprilTagLocalizer;
    public OpticalLocalizer opticalLocalizer;

    private Position cameraPosition = new Position(DistanceUnit.MM,
             160.292, 180.467,384.632, 0);
    private YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(AngleUnit.DEGREES,
            -15-90, -120, 0, 0);

    public DriveUtil driveUtil;

    public Alliance alliance;

    public WebcamName webcam;

    List<Integer> lastTrackingEncPositions = new ArrayList<>();
    List<Integer> lastTrackingEncVels = new ArrayList<>();
    public RobotContext(OpMode opMode, RobotDescriptor descriptor) {
        this.opMode = opMode;
        this.descriptor = descriptor;
        this.driveUtil = new MecanumUtil();
        this.webcam = opMode.hardwareMap.get(WebcamName.class, "Webcam 1");
        this.clock = new ElapsedTime();

        //this.opticalLocalizer = new OpticalLocalizer(this);
        //this.aprilTagLocalizer = new AprilTagLocalizer(this,cameraPosition, cameraOrientation, webcam);
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

    public WebcamName getWebcam() {
        return webcam;
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
