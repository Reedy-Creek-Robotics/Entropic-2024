package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint;

import org.firstinspires.ftc.teamcode.components.ScoringSlide;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
@Config
public abstract class AutoRight extends AutoMain{

    public static boolean PRELOAD1 = true, COLLECT_PRESETS = true, PRELOAD2 = true, PRESET1 = false, PRESET2 = false, PRESET3 = false, PARK = false;

    @Override
    public Pose2d getStartPosition() {
        //return new Pose2d((24) * getAlliance().getTranslation(), (-72+9) * getAlliance().getTranslation(), Math.toRadians(90 + getAlliance().getRotation()));
        return new Pose2d((9) * getAlliance().getTranslation(), (-72+9) * getAlliance().getTranslation(), Math.toRadians(-90 + getAlliance().getRotation()));
    }

    @Override
    public void runPath() {
        if (PRELOAD1) {
            deliverFirst(10);
            scoreSpecimen();
        }

        if (COLLECT_PRESETS) {
            collectMiddlePreset();
            collectLeftPreset();
            //collectRightPreset();
        }

        if (PRELOAD2) {
            intakeSpecimen();
            deliverSpecimen(7);
            scoreSpecimen();
        }

        if (PRESET1) {
            intakeSpecimen();
            deliverSpecimen(4);
            scoreSpecimen();
        }
        if (PRESET2){
            intakeSpecimen();
            deliverSpecimen(1);
            scoreSpecimen();
        }

        if (PRESET3){
            intakeSpecimen();
            deliverSpecimen(-2);
            scoreSpecimen();
        }

        if (PARK){
            park();
        }
    }

    public void deliverFirst(double x) {
        robot.getRobotContext().record += "Drive to Deliver a Specimen \n";

        robot.getHorizontalSlide().contract(0);
        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.OVER_HIGH_BAR);
        robot.waitForCommandsToFinish(0.15)  ;

        TrajectorySequence trajectorySequence = robot.getDriveTrain().trajectoryBuilder(getStartPosition())
                .resetVelConstraint()
                .setTangent(Math.toRadians(90 + getAlliance().getRotation()))
                .lineToConstantHeading(new Vector2d(x * getAlliance().getTranslation(),(-36) * getAlliance().getTranslation()))
                .build();
        robot.getDriveTrain().followTrajectory(trajectorySequence);
        robot.waitForCommandsToFinish();
        currentEnd = trajectorySequence.end();
    }

    public void deliverSpecimen(double x) {
        robot.getRobotContext().record += "Drive to Deliver a Specimen \n";

        robot.getHorizontalSlide().contract(0);
        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.OVER_HIGH_BAR);
        TrajectorySequence trajectorySequence = robot.getDriveTrain().trajectoryBuilder(getStartPosition())
                .resetVelConstraint()
                .setTangent(Math.toRadians(90 + getAlliance().getRotation()))
                .lineToConstantHeading(new Vector2d(x * getAlliance().getTranslation(),(-36) * getAlliance().getTranslation()))
                .build();
        robot.getDriveTrain().followTrajectory(trajectorySequence);
        robot.waitForCommandsToFinish();
        currentEnd = trajectorySequence.end();
    }

    public void scoreSpecimen(){
        robot.getRobotContext().record += "Score a Specimen \n";

        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.GROUND);
        robot.waitForCommandsToFinish(0.15);

        //robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.GROUND);
    }

    public void intakeSpecimen(){
        robot.getRobotContext().record += "Intake a Specimen \n";

        TrajectorySequence collectOther = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                //.setTangent(Math.toRadians(270 + getAlliance().getRotation()))
                .splineToLinearHeading(new Pose2d(28 * getAlliance().getTranslation(), -42 * getAlliance().getTranslation(), Math.toRadians(-45 + getAlliance().getRotation())),Math.toRadians(315 + getAlliance().getRotation()))
                .build();
        robot.getDriveTrain().followTrajectory(collectOther);
        robot.waitForCommandsToFinish();
        currentEnd = collectOther.end();

        robot.getHorizontalSlide().extend(0.9);
        robot.waitForCommandsToFinish();

        robot.getIntake().timedIntake(1,1500);
        TrajectorySequence collect = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setVelConstraint(new MecanumVelocityConstraint(15, robot.getRobotContext().getDescriptor().DRIVE_TUNER.driveTrackWidth))
                .forward(5)
                .build();
        robot.getDriveTrain().followTrajectory(collect);
        robot.waitForCommandsToFinish();
        currentEnd = collect.end();
    }

    public void collectMiddlePreset(){
        robot.getRobotContext().record += "Intake the Middle Preset \n";

        //(58,-47,90),flip out
        TrajectorySequence lineUp = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(-90 + getAlliance().getRotation())
                .splineToLinearHeading(new Pose2d(57 * getAlliance().getTranslation(),-52 * getAlliance().getTranslation(),Math.toRadians(90 + getAlliance().getRotation())),Math.toRadians(0 + getAlliance().getRotation()))
                .addSpatialMarker(new Vector2d(48 * getAlliance().getTranslation(),-48 * getAlliance().getTranslation()),() -> {
                    robot.getHorizontalSlide().extend(0.875);
                })
                .build();
        robot.getDriveTrain().followTrajectory(lineUp);
        robot.waitForCommandsToFinish();
        currentEnd = lineUp.end();

        //forward, intake
        TrajectorySequence intake = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setVelConstraint(new MecanumVelocityConstraint(20, robot.getRobotContext().getDescriptor().DRIVE_TUNER.driveTrackWidth))
                .forward(3)
                .resetVelConstraint()
                .build();
        robot.getDriveTrain().followTrajectory(intake);
        robot.getIntake().timedIntake(1,450);
        robot.waitForCommandsToFinish();
        currentEnd = intake.end();

        //(48,-49,90)
        TrajectorySequence Dropoff = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d(47 * getAlliance().getTranslation(),-56 * getAlliance().getTranslation(),Math.toRadians(90 + getAlliance().getRotation())))
                .build();
        robot.getDriveTrain().followTrajectory(Dropoff);
        robot.getHorizontalSlide().contract(0);
        robot.waitForCommandsToFinish();
        currentEnd = Dropoff.end();

        //outtake
        robot.getIntake().timedIntake(-1,200);
        robot.waitForCommandsToFinish();

        //----------------OLD---------------------
        //drive to pos(51,-43,44), flip out
        //Intake and drive forward
        //flip in and repos(58,-49,90)
        //outtake
    }

    public void collectLeftPreset(){
        robot.getRobotContext().record += "Intake the Left Preset \n";

        //forward, flip out, intake
        robot.getHorizontalSlide().extend(0.4);
        robot.getIntake().timedIntake(0,200);
        robot.waitForCommandsToFinish();

        TrajectorySequence intake = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setVelConstraint(new MecanumVelocityConstraint(15, robot.getRobotContext().getDescriptor().DRIVE_TUNER.driveTrackWidth))
                .forward(15)
                .resetVelConstraint()
                .build();
        robot.getDriveTrain().followTrajectory(intake);
        robot.getIntake().timedIntake(1,1000);
        robot.waitForCommandsToFinish();
        currentEnd = intake.end();

        //(49,-49,90)
        TrajectorySequence Dropoff = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d(50 * getAlliance().getTranslation(),-56 * getAlliance().getTranslation(),Math.toRadians(90 + getAlliance().getRotation())))
                .build();
        robot.getDriveTrain().followTrajectory(Dropoff);
        robot.getHorizontalSlide().contract(0);
        robot.waitForCommandsToFinish();
        currentEnd = Dropoff.end();

        //outtake
        robot.getIntake().timedIntake(-1,150);
        robot.waitForCommandsToFinish();

        //----------------OLD-----------------
        //Intake and drive forward
        //flip in and drive back (48,-49,90)
        //outtake
    }

    public void collectRightPreset(){
        robot.getRobotContext().record += "Intake the Right Preset \n";

        //lineup(51,-45,47), flip out
        TrajectorySequence lineUp = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d(51 * getAlliance().getTranslation(),-45 * getAlliance().getTranslation(),Math.toRadians(47 + getAlliance().getRotation())))
                .build();
        robot.getDriveTrain().followTrajectory(lineUp);
        robot.getHorizontalSlide().extend(0.4);
        robot.waitForCommandsToFinish();
        currentEnd = lineUp.end();

        //forward, intake
        TrajectorySequence intake = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setVelConstraint(new MecanumVelocityConstraint(10, robot.getRobotContext().getDescriptor().DRIVE_TUNER.driveTrackWidth))
                .forward(12)
                .resetVelConstraint()
                .build();
        robot.getDriveTrain().followTrajectory(intake);
        robot.getHorizontalSlide().extend(0.25);
        robot.getIntake().timedIntake(1,1500);
        robot.waitForCommandsToFinish();
        currentEnd = intake.end();

        //(49,-49,90)
        TrajectorySequence Dropoff = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d(49 * getAlliance().getTranslation(),-49 * getAlliance().getTranslation(),Math.toRadians(90 + getAlliance().getRotation())))
                .build();
        robot.getDriveTrain().followTrajectory(Dropoff);
        robot.getHorizontalSlide().contract(0);
        robot.waitForCommandsToFinish();
        currentEnd = Dropoff.end();

        //outtake
        robot.getIntake().timedIntake(-1,200);
        robot.waitForCommandsToFinish();

        //---------------------OLD-----------------------
        //Intake and drive forward
        //flip in and drive back (48,-49,90)
        //outtake
    }

    public void giveSpace(){
        robot.getRobotContext().record += "Give Space for HP to Place Specimen \n";

        TrajectorySequence moveAway = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d(24 * getAlliance().getTranslation(),-48 * getAlliance().getTranslation(),Math.toRadians(-45 + getAlliance().getRotation())))
                .build();
        robot.getDriveTrain().followTrajectory(moveAway);
        robot.waitForCommandsToFinish();
        currentEnd = moveAway.end();
    }

    public void collectPresets(){

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
        robot.getRobotContext().record += "Park in Observation Zone \n";

        TrajectorySequence trajectorySequence = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d((48+9) * getAlliance().getTranslation(),-60 * getAlliance().getTranslation(),Math.toRadians(90 + getAlliance().getRotation())))
                .build();

        robot.getDriveTrain().followTrajectory(trajectorySequence);
        robot.waitForCommandsToFinish();
        currentEnd = trajectorySequence.end();
    }
}
