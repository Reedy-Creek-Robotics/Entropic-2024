package org.firstinspires.ftc.teamcode.roadrunner.drive.tuning;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.Localizer;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.components.BaseComponent;
import org.firstinspires.ftc.teamcode.components.DriveTrain;
import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.teamcode.components.RobotDescriptor;
import org.firstinspires.ftc.teamcode.roadrunner.drive.ModifiedMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.drive.OpticalLocalizer;
import org.firstinspires.ftc.teamcode.roadrunner.drive.StandardTrackingWheelLocalizer;

import java.util.ArrayList;

@TeleOp
public class OpticalLocalizationTest extends LinearOpMode {

    Localizer OTOS;
    RobotDescriptor descriptor;
    RobotContext context;
    @Override
    public void runOpMode() throws InterruptedException {
        descriptor = new RobotDescriptor();
        context = new RobotContext(this,descriptor );
        OTOS = context.getLocalizer();


        telemetry.addLine("ready");
        telemetry.update();

        waitForStart();

        while (!isStopRequested()) {
            Pose2d poseEstimate = OTOS.getPoseEstimate();
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", (poseEstimate.getHeading()));
            telemetry.update();

            OTOS.update();
        }
    }
}
