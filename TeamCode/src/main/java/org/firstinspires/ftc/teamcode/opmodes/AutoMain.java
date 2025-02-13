package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.MachineVisionSubmersible;
import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.teamcode.components.ScoringSlide;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Config
public abstract class AutoMain extends LinearOpMode {
    public static String FILE_NAME = "Recorder.txt";

    protected Robot robot;
    protected RobotContext robotContext;

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
            robot.saveTimingRecord(FILE_NAME);
        }

    }

    public void initRobot(){
        robot = new Robot(this);
        robotContext = robot.getRobotContext();
        robot.getRobotContext().setAlliance(getAlliance());
        loadPaths();
        robot.init();
        robot.resetFiles(FILE_NAME);
        robot.getDriveTrain().getRoadRunner().setPoseEstimate(getStartPosition());
        currentEnd = getStartPosition();
    }

    public abstract void loadPaths();

    public abstract void runPath();

    public abstract Pose2d getStartPosition();

    public abstract RobotContext.Alliance getAlliance();

}
