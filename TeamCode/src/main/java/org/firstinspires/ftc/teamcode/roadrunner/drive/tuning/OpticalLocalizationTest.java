package org.firstinspires.ftc.teamcode.roadrunner.drive.tuning;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.Localizer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.components.BaseComponent;
import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.teamcode.components.RobotDescriptor;
import org.firstinspires.ftc.teamcode.roadrunner.drive.AprilTagLocalizer;

@TeleOp
public class OpticalLocalizationTest extends LinearOpMode {

    AprilTagLocalizer aprilTagLocalizer;

    private Position cameraPosition = new Position(DistanceUnit.MM,
            -180.467, 160.292, 384.632, 0);
    private YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(AngleUnit.DEGREES,
            -15, -120, 0, 0);
    @Override
    public void runOpMode() throws InterruptedException {
        aprilTagLocalizer = new AprilTagLocalizer(BaseComponent.createRobotContext(this),cameraPosition, cameraOrientation, this.hardwareMap.get(WebcamName.class, "Webcam 1"));

        telemetry.addLine("ready");
        telemetry.update();

        waitForStart();

        while (!isStopRequested()) {
            Pose2d poseEstimate = aprilTagLocalizer.getPoseEstimate();
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", (poseEstimate.getHeading()));


            telemetry.addData("detections:", aprilTagLocalizer.getDetections());
            telemetry.update();

            aprilTagLocalizer.update();
        }
    }
}
