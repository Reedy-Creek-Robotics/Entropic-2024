package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.teamcode.components.ScoringSlide;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

public abstract class AutoMain extends LinearOpMode {

    protected Robot robot;

    protected Pose2d currentEnd;

    @Override
    public void runOpMode() throws InterruptedException {
        try{
            initRobot();

            telemetry.log().add("Wait for start", "");

            waitForStart();

            runPath();
        }finally {
            robot.savePositionToDisk();
        }

    }

    public void initRobot(){
        robot = new Robot(this);
        robot.init();
        robot.getDriveTrain().getRoadRunner().setPoseEstimate(getStartPosition());
        currentEnd = getStartPosition();
    }

    public abstract void runPath();

    public abstract Pose2d getStartPosition();

    public abstract RobotContext.Alliance getAlliance();

    public abstract void park();

}
