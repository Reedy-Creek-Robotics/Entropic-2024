package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint;

import org.firstinspires.ftc.teamcode.components.ScoringSlide;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
@Config
public abstract class AutoRight extends AutoMain{

    public static boolean PRELOAD1 = true, COLLECT_PRESETS = false, PRELOAD2 = false, PRESET1 = false, PRESET2 = false, PRESET3 = false, PARK = true;

    @Override
    public Pose2d getStartPosition() {
        return new Pose2d((9) * getAlliance().getTranslation(), (-72+9) * getAlliance().getTranslation(), Math.toRadians(-90 + getAlliance().getRotation()));
    }

    @Override
    public void runPath() {
        if (PRELOAD1) {
            deliverSpecimen(9);
            scoreSpecimen();
        }

        if (COLLECT_PRESETS) {
            collectFirstPreset();
            collectSecondPreset();
            collectThirdPreset();
            giveSpace();
        }

        if (PRELOAD2) {
            intakeSpecimen();
            deliverSpecimen(7);
            scoreSpecimen();
        }

        if (PRESET1) {
            intakeSpecimen();
            deliverSpecimen(5);
            scoreSpecimen();
        }
        if (PRESET2){
            intakeSpecimen();
            deliverSpecimen(3);
            scoreSpecimen();
        }

        if (PRESET3){
            intakeSpecimen();
            deliverSpecimen(1);
            scoreSpecimen();
        }

        if (PARK){
            park();
        }
    }

    public void deliverSpecimen(double x) {
        robot.getHorizontalSlide().contract(0);
        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.OVER_HIGH_BAR);
        robot.waitForCommandsToFinish(.5);
        TrajectorySequence trajectorySequence = robot.getDriveTrain().trajectoryBuilder(getStartPosition())
                .resetVelConstraint()
                .setTangent(Math.toRadians(90 + getAlliance().getRotation()))
                .lineToConstantHeading(new Vector2d(x * getAlliance().getTranslation(),(-36.5) * getAlliance().getTranslation()))
                .build();

        robot.getDriveTrain().followTrajectory(trajectorySequence);
        robot.waitForCommandsToFinish();
        currentEnd = trajectorySequence.end();
    }

    public void scoreSpecimen(){
        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.HIGH_BAR);
        robot.waitForCommandsToFinish();

        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.GROUND);
    }

    public void intakeSpecimen(){
        TrajectorySequence collectOther = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(270 + getAlliance().getRotation()))
                .splineToLinearHeading(new Pose2d(30 * getAlliance().getTranslation(), -40 * getAlliance().getTranslation(),Math.toRadians(315 + getAlliance().getRotation())),Math.toRadians(310 + getAlliance().getRotation()))
                .build();
        robot.getDriveTrain().followTrajectory(collectOther);
        robot.waitForCommandsToFinish();
        currentEnd = collectOther.end();

        robot.getHorizontalSlide().extend(0.1);
        robot.waitForCommandsToFinish();

        robot.getIntake().timedIntake(1,1500);
        TrajectorySequence collect = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setVelConstraint(new MecanumVelocityConstraint(10, robot.getRobotContext().getDescriptor().DRIVE_TUNER.driveTrackWidth))
                .forward(12)
                .build();
        robot.getDriveTrain().followTrajectory(collect);
        currentEnd = collect.end();
        robot.waitForCommandsToFinish();
    }

    public void collectFirstPreset(){

        //drive to pos(51,-43,44), flip out
        TrajectorySequence lineUp = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(-270 + getAlliance().getRotation())
                .splineToLinearHeading(new Pose2d(51 * getAlliance().getTranslation(),-43 * getAlliance().getTranslation(),Math.toRadians(44 + getAlliance().getRotation())),Math.toRadians(0 + getAlliance().getRotation()))
                .addSpatialMarker(new Vector2d(47 * getAlliance().getTranslation(),-47 * getAlliance().getTranslation()),() -> {
                    robot.getHorizontalSlide().extend(0.4);
                })
                .build();
        robot.getDriveTrain().followTrajectory(lineUp);
        robot.waitForCommandsToFinish();
        currentEnd = lineUp.end();

        //Intake and drive forward
        TrajectorySequence intake = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setVelConstraint(new MecanumVelocityConstraint(10, robot.getRobotContext().getDescriptor().DRIVE_TUNER.driveTrackWidth))
                .forward(12)
                .build();
        robot.getDriveTrain().followTrajectory(intake);
        robot.getIntake().timedIntake(1,1500);
        robot.waitForCommandsToFinish();
        currentEnd = intake.end();

        //flip in and repos(58,-49,90)
        TrajectorySequence Dropoff = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d(58 * getAlliance().getTranslation(),-49 * getAlliance().getTranslation(),Math.toRadians(90 + getAlliance().getRotation())))
                .build();
        robot.getDriveTrain().followTrajectory(Dropoff);
        robot.getHorizontalSlide().contract(0);
        robot.waitForCommandsToFinish();
        currentEnd = Dropoff.end();

        //outtake
        robot.getIntake().timedIntake(-1,200);
        robot.waitForCommandsToFinish();
    }

    public void collectSecondPreset(){
        //Intake and drive forward
        TrajectorySequence intake = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setVelConstraint(new MecanumVelocityConstraint(10, robot.getRobotContext().getDescriptor().DRIVE_TUNER.driveTrackWidth))
                .forward(12)
                .build();
        robot.getDriveTrain().followTrajectory(intake);
        robot.getHorizontalSlide().extend(0.25);
        robot.getIntake().timedIntake(1,1500);
        robot.waitForCommandsToFinish();
        currentEnd = intake.end();

        //flip in and drive back (48,-49,90)
        TrajectorySequence Dropoff = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d(48 * getAlliance().getTranslation(),-49 * getAlliance().getTranslation(),Math.toRadians(90 + getAlliance().getRotation())))
                .build();
        robot.getDriveTrain().followTrajectory(Dropoff);
        robot.getHorizontalSlide().contract(0);
        robot.waitForCommandsToFinish();
        currentEnd = Dropoff.end();

        //outtake
        robot.getIntake().timedIntake(-1,200);
        robot.waitForCommandsToFinish();
    }

    public void collectThirdPreset(){
        //Intake and drive forward
        TrajectorySequence intake = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setVelConstraint(new MecanumVelocityConstraint(10, robot.getRobotContext().getDescriptor().DRIVE_TUNER.driveTrackWidth))
                .forward(12)
                .build();
        robot.getDriveTrain().followTrajectory(intake);
        robot.getHorizontalSlide().extend(0.25);
        robot.getIntake().timedIntake(1,1500);
        robot.waitForCommandsToFinish();
        currentEnd = intake.end();

        //flip in and drive back (48,-49,90)
        TrajectorySequence Dropoff = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d(48 * getAlliance().getTranslation(),-49 * getAlliance().getTranslation(),Math.toRadians(90 + getAlliance().getRotation())))
                .build();
        robot.getDriveTrain().followTrajectory(Dropoff);
        robot.getHorizontalSlide().contract(0);
        robot.waitForCommandsToFinish();
        currentEnd = Dropoff.end();

        //outtake
        robot.getIntake().timedIntake(-1,200);
        robot.waitForCommandsToFinish();
    }

    public void giveSpace(){
        TrajectorySequence moveAway = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d(36 * getAlliance().getTranslation(),-36 * getAlliance().getTranslation(),Math.toRadians(0 + getAlliance().getRotation())))
                .build();
    }

    public void collectPresets(){
        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.GROUND);

        TrajectorySequence collect1 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(-90 + getAlliance().getRotation()))
                .splineToLinearHeading(new Pose2d((48+9) * getAlliance().getTranslation(),-48 * getAlliance().getTranslation(),Math.toRadians(90 + getAlliance().getRotation())),Math.toRadians(30 + getAlliance().getRotation()))
                .build();
        robot.getDriveTrain().followTrajectory(collect1);
        robot.waitForCommandsToFinish();
        currentEnd = collect1.end();

        robot.getHorizontalSlide().contract(0);
        TrajectorySequence backward5In1 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .back(5)
                .build();
        robot.getDriveTrain().followTrajectory(backward5In1);
        currentEnd = backward5In1.end();
        robot.waitForCommandsToFinish();

        robot.getIntake().timedIntake(-1,200);
        robot.waitForCommandsToFinish();

        /*//Drive
        TrajectorySequence collect1 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(-90 + getAlliance().getRotation()))
                .splineToLinearHeading(new Pose2d((48+9) * getAlliance().getTranslation(),-52 * getAlliance().getTranslation(),Math.toRadians(65 + getAlliance().getRotation())),Math.toRadians(30 + getAlliance().getRotation()))
                .build();
        robot.getDriveTrain().followTrajectory(collect1);
        robot.waitForCommandsToFinish();
        currentEnd = collect1.end();

        //Intake
        robot.getHorizontalSlide().extend(0.5);
        robot.waitForCommandsToFinish();
        robot.getIntake().timedIntake(1,1500);
        TrajectorySequence forward5In1 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                //.waitSeconds(.5)
                .forward(8)
                .build();
        robot.getDriveTrain().followTrajectory(forward5In1);
        currentEnd = forward5In1.end();
        robot.waitForCommandsToFinish();

        //Contract
        robot.getHorizontalSlide().contract(0);
        TrajectorySequence collect2 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(-90 + getAlliance().getRotation()))
                .splineToLinearHeading(new Pose2d((48+9) * getAlliance().getTranslation(),-52 * getAlliance().getTranslation(),Math.toRadians(115 + getAlliance().getRotation())),Math.toRadians(-90 + getAlliance().getRotation()))
                .build();
        robot.getDriveTrain().followTrajectory(collect2);
        robot.waitForCommandsToFinish();

        //Turn to 2
        currentEnd = collect2.end();
        robot.getDriveTrain().followTrajectory(collect2);
        robot.waitForCommandsToFinish();
        currentEnd = collect2.end();

        //Outtake first
        robot.getIntake().timedIntake(-1,200);
        robot.waitForCommandsToFinish();

        //Intake
        robot.getHorizontalSlide().extend(0.7);
        robot.waitForCommandsToFinish();
        robot.getIntake().timedIntake(1,1500);
        TrajectorySequence forward5In2 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .waitSeconds(.5)
                .forward(5)
                .build();
        robot.getDriveTrain().followTrajectory(forward5In2);
        currentEnd =forward5In2.end();
                robot.waitForCommandsToFinish();

        //Contract
        robot.getHorizontalSlide().contract(0);
        TrajectorySequence backward5In2 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .back(5)
                .build();
        robot.getDriveTrain().followTrajectory(backward5In2);
        currentEnd = backward5In2.end();
                robot.waitForCommandsToFinish();


        //Outtake second
        robot.getIntake().timedIntake(-1,200);
        robot.waitForCommandsToFinish();

        //Turn to 3
        TrajectorySequence collect3 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .turn(Math.toRadians(29))
                .build();
        robot.getDriveTrain().followTrajectory(collect3);
        robot.waitForCommandsToFinish();
        currentEnd = collect3.end();

        //Intake
        robot.getHorizontalSlide().extend(0.7);
        robot.waitForCommandsToFinish();
        robot.getIntake().timedIntake(1,1500);
        TrajectorySequence forward5In3 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .waitSeconds(.5)
                .forward(10)
                .build();
        robot.getDriveTrain().followTrajectory(forward5In3);
        currentEnd = forward5In3.end();
                robot.waitForCommandsToFinish();

        //Contract
        robot.getHorizontalSlide().contract(0);
        TrajectorySequence backward5In3 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .back(10)
                .build();
        robot.getDriveTrain().followTrajectory(backward5In3);
        currentEnd = backward5In3.end();
        robot.waitForCommandsToFinish();

        //extra time to allow slide to contract
        robot.getIntake().timedIntake(0,500);
        robot.waitForCommandsToFinish();

        //Outtake third
        robot.getIntake().timedIntake(-1,200);
        robot.waitForCommandsToFinish();*/
    }

    @Override
    public void park() {
        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.GROUND);
        TrajectorySequence trajectorySequence = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d((48+9) * getAlliance().getTranslation(),-60 * getAlliance().getTranslation(),Math.toRadians(90 + getAlliance().getRotation())))
                .build();

        robot.getDriveTrain().followTrajectory(trajectorySequence);
        robot.waitForCommandsToFinish();
        currentEnd = trajectorySequence.end();
    }
}
