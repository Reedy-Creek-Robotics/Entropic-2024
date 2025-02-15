package org.firstinspires.ftc.teamcode.unfinishedAutos;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.ProfileAccelerationConstraint;

import org.firstinspires.ftc.teamcode.components.ScoringSlide;
import org.firstinspires.ftc.teamcode.opmodes.AutoMain;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
@Config
public abstract class AutoRightLong extends AutoMain {

    public static boolean MIDDLE = true, RIGHT = true, LEFT = true, PRELOAD1 = true, PRELOAD2 = true, PRESET1 = false, PRESET2 = false, PRESET3 = false, PARK = false;

    public static double intakeSpeed = 20;

    TrajectorySequence intakeMiddlePreset, dropoffMiddlePreset,
                       intakeRightPreset, dropoffRightPreset,
                       intakeLeftPreset, dropoffLeftPreset,
                       intakeFirstSpecimen, deliverFirstSpecimen,
                       intakeSecondSpecimen, deliverSecondSpecimen,
                       intakeThirdSpecimen, deliverThirdSpecimen,
                       intakeFourthSpecimen, deliverFourthSpecimen,
                       intakeFifthSpecimen, deliverFifthSpecimen,
                       park;

    @Override
    public Pose2d getStartPosition() {
        //return new Pose2d((24) * getAlliance().getTranslation(), (-72+9) * getAlliance().getTranslation(), Math.toRadians(90 + getAlliance().getRotation()));
        return new Pose2d((24) * getAlliance().getTranslation(), (-72+9) * getAlliance().getTranslation(), Math.toRadians(90 + getAlliance().getRotation()));
    }

    @Override
    public void loadPaths() {
        currentEnd = getStartPosition();

        //Middle Preset
        intakeMiddlePreset = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .addSpatialMarker(new Vector2d(48, -52), () -> {
                    robot.getHorizontalSlide().extend(0.95);
                })
                .lineToConstantHeading(new Vector2d(58,-52))
                .addDisplacementMarker(() -> {
                    robot.getIntake().timedIntake(1,1000);
                })
                .forward(7,new MecanumVelocityConstraint(intakeSpeed,15.5),new ProfileAccelerationConstraint(60))
                .build();
        currentEnd = intakeMiddlePreset.end();
        dropoffMiddlePreset = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToConstantHeading(new Vector2d(52,-53))
                .build();
        currentEnd = dropoffMiddlePreset.end();

        //Right Preset
        intakeRightPreset = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .addDisplacementMarker(() -> {
                    robot.getHorizontalSlide().extend(0.95);
                })
                .splineToLinearHeading(new Pose2d(52,-46,Math.toRadians(47)),Math.toRadians(80))
                .addDisplacementMarker(() -> {
                    robot.getIntake().timedIntake(1,800);
                })
                .forward(8,new MecanumVelocityConstraint(intakeSpeed,15.09),new ProfileAccelerationConstraint(60))
                .build();
        currentEnd = intakeRightPreset.end();
        dropoffRightPreset = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d(48,-56,Math.toRadians(90)))
                .build();
        currentEnd = dropoffRightPreset.end();

        //Left Preset
        intakeLeftPreset = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .addDisplacementMarker(() -> {
                    robot.getHorizontalSlide().extend(0.65);
                    robot.getIntake().timedIntake(1,1000);
                })
                .forward(10,new MecanumVelocityConstraint(intakeSpeed,15.5),new ProfileAccelerationConstraint(60))
                .build();
        currentEnd = intakeLeftPreset.end();
        dropoffLeftPreset = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToConstantHeading(new Vector2d(48,-53))
                .build();
        currentEnd = dropoffLeftPreset.end();



        //First Specimen
        intakeFirstSpecimen = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(80))
                .splineToLinearHeading(new Pose2d(30,-42,Math.toRadians(315)), Math.toRadians(180))
                .forward(6,new MecanumVelocityConstraint(intakeSpeed,robot.getRobotContext().getDescriptor().DRIVE_TUNER.driveTrackWidth),new ProfileAccelerationConstraint(robot.getRobotContext().getDescriptor().DRIVE_TUNER.maxAccel))
                .build();
        currentEnd = intakeFirstSpecimen.end();
        deliverFirstSpecimen = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(175))
                .splineToLinearHeading(new Pose2d(9,-36.5,Math.toRadians(-90)),Math.toRadians(130))
                .build();
        currentEnd = deliverFirstSpecimen.end();

        //Second Specimen
        intakeSecondSpecimen = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d(30,-42,Math.toRadians(-45)))
                .forward(6,new MecanumVelocityConstraint(intakeSpeed,robot.getRobotContext().getDescriptor().DRIVE_TUNER.driveTrackWidth),new ProfileAccelerationConstraint(robot.getRobotContext().getDescriptor().DRIVE_TUNER.maxAccel))
                .build();
        currentEnd = intakeSecondSpecimen.end();
        deliverSecondSpecimen = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(175))
                .splineToLinearHeading(new Pose2d(6,-36.5,Math.toRadians(-90)),Math.toRadians(140))
                .build();
        currentEnd = deliverSecondSpecimen.end();

        //Third Specimen
        intakeThirdSpecimen = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d(30,-42,Math.toRadians(-45)))
                .forward(6,new MecanumVelocityConstraint(intakeSpeed,robot.getRobotContext().getDescriptor().DRIVE_TUNER.driveTrackWidth),new ProfileAccelerationConstraint(robot.getRobotContext().getDescriptor().DRIVE_TUNER.maxAccel))
                .build();
        currentEnd = intakeThirdSpecimen.end();
        deliverThirdSpecimen = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(175))
                .splineToLinearHeading(new Pose2d(3,-36.5,Math.toRadians(-90)),Math.toRadians(150))
                .build();
        currentEnd = deliverThirdSpecimen.end();

        //Fourth Specimen
        intakeFourthSpecimen = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d(30,-42,Math.toRadians(-45)))
                .forward(6,new MecanumVelocityConstraint(intakeSpeed,robot.getRobotContext().getDescriptor().DRIVE_TUNER.driveTrackWidth),new ProfileAccelerationConstraint(robot.getRobotContext().getDescriptor().DRIVE_TUNER.maxAccel))
                .build();
        currentEnd = intakeFourthSpecimen.end();
        deliverFourthSpecimen = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(175))
                .splineToLinearHeading(new Pose2d(0,-36.5,Math.toRadians(-90)),Math.toRadians(155))
                .build();
        currentEnd = deliverFourthSpecimen.end();

        //Fifth Specimen
        intakeFifthSpecimen = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d(30,-42,Math.toRadians(-45)))
                .forward(6,new MecanumVelocityConstraint(intakeSpeed,robot.getRobotContext().getDescriptor().DRIVE_TUNER.driveTrackWidth),new ProfileAccelerationConstraint(robot.getRobotContext().getDescriptor().DRIVE_TUNER.maxAccel))
                .build();
        currentEnd = intakeFirstSpecimen.end();
        deliverFifthSpecimen = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(175))
                .splineToLinearHeading(new Pose2d(-3,-36.5,Math.toRadians(-90)),Math.toRadians(160))
                .build();
        currentEnd = deliverFifthSpecimen.end();

        //Park
        park = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d(28,-52,Math.toRadians(-45)))
                .build();
    }

    @Override
    public void runPath() {
        if(MIDDLE){
            collectPreset(intakeMiddlePreset, dropoffMiddlePreset);
        }
        if(RIGHT){
            collectPreset(intakeRightPreset,dropoffRightPreset);
        }
        if(LEFT){
            collectPreset(intakeLeftPreset, dropoffLeftPreset);
        }

        //first
        //second
        //third
        //fourth
        //fifth

        //park
    }

    public void collectPreset(TrajectorySequence intake, TrajectorySequence dropoff){
        //drive, flip out, intake
        robot.getDriveTrain().followTrajectory(intake);
        robot.waitForCommandsToFinish();

        //flip in, drop off
        robot.getHorizontalSlide().contract(0);
        robot.getDriveTrain().followTrajectory(dropoff);
        robot.waitForCommandsToFinish();

        //outtake
        robot.getIntake().timedIntake(-1, 150);
        robot.waitForCommandsToFinish();
    }

    public void scoreSpecimen(TrajectorySequence intake, TrajectorySequence score){
        //line up, flip out, intake, drive in
        robot.getDriveTrain().followTrajectory(intake);
        robot.waitForCommandsToFinish();

        //flip in, drive, and run up slides
        robot.getDriveTrain().followTrajectory(score);
        robot.waitForCommandsToFinish();

        //drop slides
        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.HIGH_BAR);
        robot.waitForCommandsToFinish(0.15);
    }

    public void park(TrajectorySequence park) {
        robot.getRobotContext().record += "Park in Observation Zone \n";

        robot.getDriveTrain().followTrajectory(park);
        robot.waitForCommandsToFinish();
    }
}
