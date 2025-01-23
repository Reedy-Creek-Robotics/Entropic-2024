package org.firstinspires.ftc.teamcode.components;

import com.acmerobotics.roadrunner.localization.Localizer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
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

    public OpMode opMode;

    public RobotDescriptor descriptor;

    public Localizer localizer;

    public AprilTagLocalizer aprilTagLocalizer;
    public OpticalLocalizer opticalLocalizer;

    private Position cameraPosition = new Position(DistanceUnit.MM,
            -180.467, 160.292, 384.632, 0);
    private YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(AngleUnit.DEGREES,
            -15-90, -120, 0, 0);

    public DriveUtil driveUtil;

    public int alliance;

    public WebcamName webcam;

    List<Integer> lastTrackingEncPositions = new ArrayList<>();
    List<Integer> lastTrackingEncVels = new ArrayList<>();
    public RobotContext(OpMode opMode, RobotDescriptor descriptor) {
        this.opMode = opMode;
        this.descriptor = descriptor;
        this.driveUtil = new MecanumUtil();
        this.webcam = opMode.hardwareMap.get(WebcamName.class, "Webcam 1");

        //this.opticalLocalizer = new OpticalLocalizer(this);
        //this.aprilTagLocalizer = new AprilTagLocalizer(this,cameraPosition, cameraOrientation, webcam);
        this.localizer = new OpticalAprilTagLocalizer(this, cameraPosition, cameraOrientation, webcam);
        //this.localizer = new OpticalLocalizer(this); //new StandardTrackingWheelLocalizer(opMode.hardwareMap, lastTrackingEncPositions, lastTrackingEncVels, this.descriptor.ODOMETRY_TUNER);//new TwoWheelTrackingLocalizer(opMode.hardwareMap,this.descriptor);


        this.alliance = 1; //blue is negative one, red is positive one
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

    public int getAlliance() {
        return alliance;
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
