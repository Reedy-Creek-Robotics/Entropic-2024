package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.ProfileAccelerationConstraint;

import org.firstinspires.ftc.teamcode.components.ScoringSlide;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

public abstract class AutoSpecimen3 extends AutoMain {

    public static boolean LEFT = true, PRELOAD1 = true, PRELOAD2 = true, PRESET1 = false, PARK = false;

    public static double intakeSpeed = 20;

    TrajectorySequence
            scoreFirstSpecimen,
            intakeLeftPreset, dropoffLeftPreset,
            intakeSecondSpecimen, deliverSecondSpecimen,
            intakeThirdSpecimen, deliverThirdSpecimen,
            park;

    @Override
    public Pose2d getStartPosition() {
        //return new Pose2d((24) * getAlliance().getTranslation(), (-72+9) * getAlliance().getTranslation(), Math.toRadians(90 + getAlliance().getRotation()));
        return new Pose2d((9) * getAlliance().getTranslation(), (-72+9) * getAlliance().getTranslation(), Math.toRadians(-90 + getAlliance().getRotation()));
    }

    @Override
    public void loadPaths() {
        currentEnd = getStartPosition();

        //Preload
        scoreFirstSpecimen = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineTo(new Vector2d(9,-36.5))
                .build();
        currentEnd = scoreFirstSpecimen.end();

        //Collect Left Preset
        intakeLeftPreset = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .addSpatialMarker(new Vector2d(38, -48), () -> {
                    robot.getHorizontalSlide().extend(0.95);
                })
                .setTangent(Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(48,-52,Math.toRadians(90)),Math.toRadians(0))
                .addDisplacementMarker(() -> {
                    robot.getIntake().timedIntake(1,1000);
                })
                .forward(7,new MecanumVelocityConstraint(intakeSpeed,15.5),new ProfileAccelerationConstraint(60))
                .build();
        currentEnd = intakeLeftPreset.end();
        dropoffLeftPreset = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToConstantHeading(new Vector2d(48,-53))
                .build();
        currentEnd = dropoffLeftPreset.end();

        //Score Preload 2
        intakeSecondSpecimen = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(80))
                .splineToLinearHeading(new Pose2d(30,-42,Math.toRadians(315)), Math.toRadians(180))
                .addDisplacementMarker(() -> {
                    robot.getIntake().timedIntake(1,1000);
                })
                .forward(6,new MecanumVelocityConstraint(intakeSpeed,robot.getRobotContext().getDescriptor().DRIVE_TUNER.driveTrackWidth),new ProfileAccelerationConstraint(robot.getRobotContext().getDescriptor().DRIVE_TUNER.maxAccel))
                .build();
        currentEnd = intakeSecondSpecimen.end();
        deliverSecondSpecimen = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(175))
                .splineToLinearHeading(new Pose2d(6,-36.5,Math.toRadians(-90)),Math.toRadians(130))
                .build();
        currentEnd = deliverSecondSpecimen.end();

        //Score Left Specimen
        intakeSecondSpecimen = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d(30,-42,Math.toRadians(-45)))
                .addDisplacementMarker(() -> {
                    robot.getIntake().timedIntake(1,1000);
                })
                .forward(6,new MecanumVelocityConstraint(intakeSpeed,robot.getRobotContext().getDescriptor().DRIVE_TUNER.driveTrackWidth),new ProfileAccelerationConstraint(robot.getRobotContext().getDescriptor().DRIVE_TUNER.maxAccel))
                .build();
        currentEnd = intakeSecondSpecimen.end();
        deliverSecondSpecimen = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(175))
                .splineToLinearHeading(new Pose2d(3,-36.5,Math.toRadians(-90)),Math.toRadians(140))
                .build();
        currentEnd = deliverSecondSpecimen.end();

        //Park
        park = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .addSpatialMarker(new Vector2d(21,48), () -> {
                    robot.getHorizontalSlide().extend(0.95);
                })
                .lineToLinearHeading(new Pose2d(28,-52,Math.toRadians(-45)))
                .build();
    }

    @Override
    public void runPath() {
        if (PRELOAD1){
            scorePreload(scoreFirstSpecimen);
        }

        if(LEFT){
            collectPreset(intakeLeftPreset,dropoffLeftPreset);
        }

        if(PRELOAD2){
            scoreSpecimen(intakeSecondSpecimen,deliverSecondSpecimen);
        }

        if (PRESET1){
            scoreSpecimen(intakeThirdSpecimen, deliverThirdSpecimen);
        }

        if(PARK){
            park(park);
        }
    }

    public void scorePreload(TrajectorySequence score){
        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.OVER_HIGH_BAR);
        robot.waitForCommandsToFinish(0.35);
        robot.getDriveTrain().followTrajectory(score);
        robot.waitForCommandsToFinish();

        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.GROUND);
        robot.waitForCommandsToFinish(0.15);
    }

    public void collectPreset(TrajectorySequence intake, TrajectorySequence deliver){
        //drive, flip out, intake
        robot.getHorizontalSlide().extend(0.95);
        robot.getDriveTrain().followTrajectory(intake);
        robot.waitForCommandsToFinish();

        robot.getHorizontalSlide().contract(0);
        robot.getDriveTrain().followTrajectory(deliver);
        robot.waitForCommandsToFinish();

        robot.getIntake().timedIntake(-1, 150);
        robot.waitForCommandsToFinish();
    }

    public void scoreSpecimen(TrajectorySequence intake, TrajectorySequence score){
        //line up, flip out, intake, drive in
        robot.getDriveTrain().followTrajectory(intake);
        robot.waitForCommandsToFinish();

        //flip in, drive, and run up slides
        robot.getHorizontalSlide().contract(0);
        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.OVER_HIGH_BAR);
        robot.getDriveTrain().followTrajectory(score);
        robot.waitForCommandsToFinish();

        //drop slides
        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.GROUND);
        robot.waitForCommandsToFinish(0.15);
    }

    public void park(TrajectorySequence park) {
        robot.getRobotContext().record += "Park in Observation Zone \n";

        robot.getHorizontalSlide().extend(0.1);
        robot.getDriveTrain().followTrajectory(park);
        robot.waitForCommandsToFinish();
    }
}