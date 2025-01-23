package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.firstinspires.ftc.teamcode.components.ScoringSlide;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

public abstract class AutoRight extends AutoMain{

    @Override
    public void runPath() {
        deliverPreload();
        scoreSpecimen();

        collectPresets(new Pose2d(38,-30 * getAlliance(),Math.toRadians(45 * getAlliance())),Math.toRadians(45 * getAlliance()));
        deliverToHumanPlayer();

        collectPresets(new Pose2d(50,-30 * getAlliance(),Math.toRadians(45 * getAlliance())),Math.toRadians(45 * getAlliance()));
        deliverToHumanPlayer();

        collectPresets(new Pose2d(56,-24 * getAlliance(),Math.toRadians(0 * getAlliance())),Math.toRadians(0 * getAlliance()));
        deliverToHumanPlayer();

        park();
    }

    @Override
    public void deliverPreload() {
        TrajectorySequence trajectorySequence = robot.getDriveTrain().trajectoryBuilder(getStartPosition())
                .setTangent(Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(10,(-24-8) * getAlliance()),Math.toRadians(90 * getAlliance()))
                .addSpatialMarker(new Vector2d(0,0  * getAlliance()),() -> {
                    robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.UNDER_HIGH_BAR);
                })
                .build();

        robot.getDriveTrain().followTrajectory(trajectorySequence);
        robot.waitForCommandsToFinish();

        currentEnd = trajectorySequence.end();
    }

    public void deliverToHumanPlayer(){
        TrajectorySequence trajectorySequence = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(-90 * getAlliance()))
                .splineToLinearHeading(new Pose2d(48,-48 * getAlliance(),Math.toRadians(-90 * getAlliance())),Math.toRadians(-90 * getAlliance()))
                .build();
        robot.getDriveTrain().followTrajectory(trajectorySequence);
        robot.waitForCommandsToFinish();

        currentEnd = trajectorySequence.end();

        robot.getIntake().timedIntake(-1,2000);
        robot.waitForCommandsToFinish();
    }
    @Override
    public void park() {
        TrajectorySequence trajectorySequence = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(-90))
                .splineToConstantHeading(new Vector2d(36,-60  * getAlliance()),Math.toRadians(-90  * getAlliance()))
                .build();

        robot.getDriveTrain().followTrajectory(trajectorySequence);
        robot.waitForCommandsToFinish();

        currentEnd = trajectorySequence.end();
    }
}
