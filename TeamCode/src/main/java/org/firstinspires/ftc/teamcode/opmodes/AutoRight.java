package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.firstinspires.ftc.teamcode.components.ScoringSlide;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
@Config
public abstract class AutoRight extends AutoMain{

    @Override
    public Pose2d getStartPosition() {
        return new Pose2d((23.5-9) * getAlliance().getTranslation(), (-72+9) * getAlliance().getTranslation(), Math.toRadians(-90 + getAlliance().getRotation()));
    }

    @Override
    public void runPath() {
        deliverPreload();
        scoreSpecimen();
        collectPresets();
        park();
    }

    @Override
    public void deliverPreload() {
        TrajectorySequence trajectorySequence = robot.getDriveTrain().trajectoryBuilder(getStartPosition())
                .setTangent(Math.toRadians(90 + getAlliance().getRotation()))
                .lineToConstantHeading(new Vector2d(9 * getAlliance().getTranslation(),(-38) * getAlliance().getTranslation()))
                .addSpatialMarker(new Vector2d(12 * getAlliance().getTranslation(),-48 * getAlliance().getTranslation()),() -> {
                    robot.getIntake().timedIntake(-0.3 * getAlliance().getTranslation(),80 * getAlliance().getTranslation());
                    robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.OVER_HIGH_BAR);
                })
                .build();

        robot.getDriveTrain().followTrajectory(trajectorySequence);
        robot.waitForCommandsToFinish();

        currentEnd = trajectorySequence.end();
    }

    public void scoreSpecimen(){
        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.HIGH_BAR);
        robot.waitForCommandsToFinish();

        TrajectorySequence trajectorySequence = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .forward(4)
                .build();
        robot.getDriveTrain().followTrajectory(trajectorySequence);
        robot.waitForCommandsToFinish();

        currentEnd = trajectorySequence.end();

        robot.getIntake().timedIntake(-1,100);
        robot.waitForCommandsToFinish();
    }

    public void collectPresets(){
        //Drive
        TrajectorySequence collect1 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(-90 + getAlliance().getRotation()))
                .splineToLinearHeading(new Pose2d((48+9) * getAlliance().getTranslation(),-52 * getAlliance().getTranslation(),Math.toRadians(65 + getAlliance().getRotation())),Math.toRadians(30 + getAlliance().getRotation()))
                .build();
        robot.getDriveTrain().followTrajectory(collect1);
        robot.waitForCommandsToFinish();
        currentEnd = collect1.end();

        //Intake
        robot.getHorizontalSlide().extend(0.87);
        robot.getIntake().timedIntake(1,1500);
        robot.waitForCommandsToFinish();

        //Contract
        robot.getHorizontalSlide().contract();
        robot.waitForCommandsToFinish();

        //Turn to 2
        TrajectorySequence collect2 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .turn(Math.toRadians(23))
                .build();
        robot.getDriveTrain().followTrajectory(collect2);
        robot.waitForCommandsToFinish();
        currentEnd = collect2.end();

        //Outtake first
        robot.getIntake().timedIntake(-0.5,200);
        robot.waitForCommandsToFinish();

        //Intake
        robot.getHorizontalSlide().extend(0.75);
        robot.getIntake().timedIntake(1,1500);
        robot.waitForCommandsToFinish();

        //Contract
        robot.getHorizontalSlide().contract();
        robot.waitForCommandsToFinish();

        //Turn to 3
        TrajectorySequence collect3 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .turn(Math.toRadians(23))
                .build();
        robot.getDriveTrain().followTrajectory(collect3);
        robot.waitForCommandsToFinish();
        currentEnd = collect3.end();

        //Outtake second
        robot.getIntake().timedIntake(-0.5,200);
        robot.waitForCommandsToFinish();

        //Intake
        robot.getHorizontalSlide().extend(0.87);
        robot.getIntake().timedIntake(1,1500);
        robot.waitForCommandsToFinish();

        //Contract
        robot.getHorizontalSlide().contract();
        robot.waitForCommandsToFinish();

        //extra time to allow slide to contract
        robot.getIntake().timedIntake(0,500);
        robot.waitForCommandsToFinish();

        //Outtake third
        robot.getIntake().timedIntake(-0.5,200);
        robot.waitForCommandsToFinish();
    }

    @Override
    public void park() {
        TrajectorySequence trajectorySequence = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d((48+9) * getAlliance().getTranslation(),-60 * getAlliance().getTranslation(),Math.toRadians(90 + getAlliance().getRotation())))
                .build();

        robot.getDriveTrain().followTrajectory(trajectorySequence);
        robot.waitForCommandsToFinish();

        currentEnd = trajectorySequence.end();
    }
}
