package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.firstinspires.ftc.teamcode.components.ScoringSlide;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

public abstract class AutoLeft extends AutoMain{
    @Override
    public void runPath() {
        deliverPreload();
        scoreSpecimen();

        collectPresets(new Pose2d(-38,-30 * getAlliance(),Math.toRadians(135 * getAlliance())),Math.toRadians(135 * getAlliance()));
        driveToBaskets(ScoringSlide.Positions.HIGH_BASKET);
        scoreSample();

        collectPresets(new Pose2d(-50,-30 * getAlliance(),Math.toRadians(135 * getAlliance())),Math.toRadians(135 * getAlliance()));
        driveToBaskets(ScoringSlide.Positions.HIGH_BASKET);
        scoreSample();

        collectPresets(new Pose2d(-56,-24 * getAlliance(),Math.toRadians(180 * getAlliance())),Math.toRadians(180 * getAlliance()));
        driveToBaskets(ScoringSlide.Positions.HIGH_BASKET);
        scoreSample();

        park();
    }

    @Override
    public void deliverPreload() {
        TrajectorySequence trajectorySequence = robot.getDriveTrain().trajectoryBuilder(getStartPosition())
                .setTangent(Math.toRadians(90 * getAlliance()))
                .splineToConstantHeading(new Vector2d(-10,(-24-8) * getAlliance()),Math.toRadians(90 * getAlliance()))
                .addSpatialMarker(new Vector2d(0,0  * getAlliance()),() -> {
                    robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.UNDER_HIGH_BAR);
                })
                .build();

        robot.getDriveTrain().followTrajectory(trajectorySequence);
        robot.waitForCommandsToFinish();

        currentEnd = trajectorySequence.end();
    }

    public void driveToBaskets(ScoringSlide.Positions height){
        TrajectorySequence trajectorySequence = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(-90 * getAlliance()))
                .splineToLinearHeading(new Pose2d(-54,-54 * getAlliance(),Math.toRadians(225 * getAlliance())),Math.toRadians(225 * getAlliance()))
                .build();
        robot.getDriveTrain().followTrajectory(trajectorySequence);
        robot.waitForCommandsToFinish();

        currentEnd = trajectorySequence.end();

        robot.getIntake().timedIntake(-1,2000);
        robot.waitForCommandsToFinish();
    }

    public void scoreSample(){
        robot.getIntake().timedIntake(-1,2000);
        robot.waitForCommandsToFinish();
    }

    @Override
    public void park() {
        TrajectorySequence trajectorySequence = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(60 * getAlliance()))
                .splineToLinearHeading(new Pose2d(-30,-12 * getAlliance(),Math.toRadians(-90 * getAlliance())),Math.toRadians(90 * getAlliance()))
                .build();

        robot.getDriveTrain().followTrajectory(trajectorySequence);
        robot.waitForCommandsToFinish();

        currentEnd = trajectorySequence.end();
    }

}
