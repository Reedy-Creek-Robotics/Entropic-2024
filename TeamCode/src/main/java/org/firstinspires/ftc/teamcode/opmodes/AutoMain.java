package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.ScoringSlide;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

public abstract class AutoMain extends LinearOpMode {
    protected Robot robot;

    protected Pose2d currentEnd;

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
        currentEnd = getStartPosition();
    }

    public abstract void runPath();

    public abstract Pose2d getStartPosition();

    public abstract double getAlliance();

    public abstract void deliverPreload();
    public abstract void park();

    public void scoreSpecimen(){
        //lift up
        //moveback to outtake
        //outtake

        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.HIGH_BAR);
        robot.waitForCommandsToFinish();

        TrajectorySequence trajectorySequence = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .forward(4)
                .build();
        robot.getDriveTrain().followTrajectory(trajectorySequence);
        robot.waitForCommandsToFinish();

        robot.getIntake().timedIntake(-1,2000);
        robot.waitForCommandsToFinish();
    }

    public void collectPresets(Pose2d position, double angle){
        TrajectorySequence trajectorySequence = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .addDisplacementMarker(() -> {
                    robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.GROUND);
                })
                .setTangent(Math.toRadians(-90 * getAlliance()))
                .splineToLinearHeading(position,angle)
                .build();
        robot.getDriveTrain().followTrajectory(trajectorySequence);
        robot.waitForCommandsToFinish();

        currentEnd = trajectorySequence.end();

        robot.getIntake().timedIntake(1,2000);
        robot.waitForCommandsToFinish();
    }
}
