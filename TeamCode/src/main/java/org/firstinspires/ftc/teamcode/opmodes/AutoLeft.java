package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.firstinspires.ftc.teamcode.components.ScoringSlide;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

public abstract class AutoLeft extends AutoMain{
    @Override
    public Pose2d getStartPosition() {
        return new Pose2d((-47 + 9) * getAlliance().getTranslation(), (-72+9) * getAlliance().getTranslation(), Math.toRadians(90 + getAlliance().getRotation()));
    }

    @Override
    public void runPath() {
        deliverPreload();
        deliverFirstPreset();
        deliverSecondPreset();
        deliverThirdPreset();
        park();
    }

    @Override
    public void deliverPreload() {
        TrajectorySequence trajectorySequence = robot.getDriveTrain().trajectoryBuilder(getStartPosition())
                .setTangent(Math.toRadians(150 + getAlliance().getRotation()))
                .splineToConstantHeading(new Vector2d(-49 * getAlliance().getTranslation(),-49 * getAlliance().getTranslation()),Math.toRadians(180 + getAlliance().getRotation()))
                .turn(Math.toRadians(-45))
                .addSpatialMarker(new Vector2d(0 * getAlliance().getTranslation(),0 * getAlliance().getTranslation()),() -> {//find pos
                    robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.HIGH_BASKET);
                })
                .build();
        robot.getDriveTrain().followTrajectory(trajectorySequence);
        robot.waitForCommandsToFinish();
        currentEnd = trajectorySequence.end();

        robot.getIntake().timedIntake(-0.5,200);
        robot.waitForCommandsToFinish();
    }

    public void deliverFirstPreset(){
        //turn to first
        TrajectorySequence collect1 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .turn(Math.toRadians(45))
                .build();
        robot.getDriveTrain().followTrajectory(collect1);
        currentEnd = collect1.end();

        //Intake first
        robot.getHorizontalSlide().extend(0);
        robot.getIntake().timedIntake(1,1500);
        robot.waitForCommandsToFinish();

        //Contract
        robot.getHorizontalSlide().contract();
        robot.waitForCommandsToFinish();

        TrajectorySequence deliver1 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .turn(Math.toRadians(-45))
                .build();
        robot.getDriveTrain().followTrajectory(deliver1);
        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.HIGH_BASKET);
        robot.waitForCommandsToFinish();
        currentEnd = deliver1.end();

        //Outtake first
        robot.getIntake().timedIntake(-0.5,200);
        robot.waitForCommandsToFinish();
    }

    public void deliverSecondPreset(){
        //turn to second
        TrajectorySequence collect2 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .turn(Math.toRadians(75))
                .build();
        robot.getDriveTrain().followTrajectory(collect2);
        currentEnd = collect2.end();

        //Intake second
        robot.getHorizontalSlide().extend(0);
        robot.getIntake().timedIntake(1,1500);
        robot.waitForCommandsToFinish();

        //Contract
        robot.getHorizontalSlide().contract();
        robot.waitForCommandsToFinish();

        TrajectorySequence deliver2 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .turn(Math.toRadians(-75))
                .build();
        robot.getDriveTrain().followTrajectory(deliver2);
        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.HIGH_BASKET);
        robot.waitForCommandsToFinish();
        currentEnd = deliver2.end();

        //Outtake second
        robot.getIntake().timedIntake(-0.5,200);
        robot.waitForCommandsToFinish();
    }

    public void deliverThirdPreset(){
        //drive to third
        TrajectorySequence collect3 =robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d(-56 * getAlliance().getTranslation(),-49 * getAlliance().getTranslation(),Math.toRadians((45 + 82 + getAlliance().getRotation()))))
                .build();
        robot.getDriveTrain().followTrajectory(collect3);
        robot.waitForCommandsToFinish();
        currentEnd = collect3.end();

        //Intake third
        robot.getHorizontalSlide().extend(0);
        robot.getIntake().timedIntake(1,1500);
        robot.waitForCommandsToFinish();

        //Contract
        robot.getHorizontalSlide().contract();
        robot.waitForCommandsToFinish();

        TrajectorySequence deliver3 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d(-49 * getAlliance().getTranslation(),-49 * getAlliance().getTranslation(),Math.toRadians(45 + getAlliance().getRotation())))
                .build();
        robot.getDriveTrain().followTrajectory(deliver3);
        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.HIGH_BASKET);
        robot.waitForCommandsToFinish();
        currentEnd = deliver3.end();

        //Outtake third
        robot.getIntake().timedIntake(-0.5,200);
        robot.waitForCommandsToFinish();
    }

    @Override
    public void park() {
        TrajectorySequence trajectorySequence = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(90 + getAlliance().getRotation()))
                .splineToLinearHeading(new Pose2d(-24 * getAlliance().getTranslation(),-11 * getAlliance().getTranslation(),Math.toRadians(180 + getAlliance().getRotation())),Math.toRadians(0 + getAlliance().getRotation()))
                .build();

        robot.getDriveTrain().followTrajectory(trajectorySequence);
        robot.waitForCommandsToFinish();

        currentEnd = trajectorySequence.end();
    }

}
