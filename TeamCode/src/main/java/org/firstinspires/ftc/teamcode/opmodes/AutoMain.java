package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

public abstract class AutoMain extends LinearOpMode {
    protected Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        initRobot();

        telemetry.log().add("Wait for start", "");

        waitForStart();

        runPath();

        robot.savePositionToDisk();

    }

    public void initRobot(){
        robot = new Robot(this);
        robot.init();
        robot.getDriveTrain().getRoadRunner().setPoseEstimate(getStartPosition());
    }

    public void runPath(){
        robot.getDriveTrain().followTrajectory(autoTrajectory(getStartPosition()));
        robot.waitForCommandsToFinish();
    }

    public abstract Pose2d getStartPosition();
    public abstract TrajectorySequence autoTrajectory(Pose2d pos);



    /*public abstract void park();*/
}
