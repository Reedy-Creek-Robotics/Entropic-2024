package org.firstinspires.ftc.teamcode.roadrunner.drive.tuning;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.Localizer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.teamcode.components.RobotDescriptor;

@TeleOp
public class AprilTagLocalizationTest extends LinearOpMode {

    Localizer aprilTag;
    RobotDescriptor descriptor;
    RobotContext context;
    @Override
    public void runOpMode() throws InterruptedException {
        descriptor = new RobotDescriptor();
        context = new RobotContext(this,descriptor );
        aprilTag = context.getAprilTagLocalizer();


        telemetry.addLine("ready");
        telemetry.update();

        waitForStart();

        while (!isStopRequested()) {
            Pose2d poseEstimate = aprilTag.getPoseEstimate();
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", (poseEstimate.getHeading()));
            telemetry.update();

            aprilTag.update();
        }
    }
}
