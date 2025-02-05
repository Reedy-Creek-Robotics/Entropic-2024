package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint;

import org.firstinspires.ftc.teamcode.components.ScoringSlide;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Config
public abstract class AutoLeft extends AutoMain{
    public static boolean PRELOAD = true, FIRST = false, SECOND = false, THIRD = false, PARK = false;

    @Override
    public Pose2d getStartPosition() {
        return new Pose2d((-47 + 9) * getAlliance().getTranslation(), (-72+9) * getAlliance().getTranslation(), Math.toRadians(90 + getAlliance().getRotation()));
    }

    @Override
    public void runPath() {
        if (PRELOAD) {
            deliverPreload();
        }
        if (FIRST) {
            collectFirstPreset();
            scoresample();
        }
        if (SECOND) {
            collectSecondPreset();
            scoresample();
        }
        if (THIRD){
            collectThirdPreset();
            scoresample();
        }
        if (PARK){
            park();
        }
    }

    public void deliverPreload() {
        TrajectorySequence trajectorySequence = robot.getDriveTrain().trajectoryBuilder(getStartPosition())
                .setTangent(Math.toRadians(135 + getAlliance().getRotation()))
                .splineToSplineHeading(new Pose2d(-48 * getAlliance().getTranslation(),-48 * getAlliance().getTranslation(), Math.toRadians(45 + getAlliance().getRotation())),Math.toRadians(180 + getAlliance().getRotation()))
//                .turn(Math.toRadians(-45))
                //TODO FIND POS, May want move raise to after movement
                .addSpatialMarker(new Vector2d(-38 * getAlliance().getTranslation(),-55 * getAlliance().getTranslation()),() -> {//find pos
                    robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.HIGH_BASKET);
                })
                .build();
        robot.getDriveTrain().followTrajectory(trajectorySequence);
        robot.waitForCommandsToFinish();
        currentEnd = trajectorySequence.end();

        robot.getIntake().timedIntake(-0.5,200);
        robot.waitForCommandsToFinish();
    }

    public void collectFirstPreset(){
        //turn to first
        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.GROUND);
        TrajectorySequence lineUp = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToSplineHeading(new Pose2d(-50 * getAlliance().getTranslation(), -50 * getAlliance().getTranslation(),Math.toRadians(86 + getAlliance().getRotation())))
                //.turn(Math.toRadians(45))
                .build();
        robot.getDriveTrain().followTrajectory(lineUp);
        robot.waitForCommandsToFinish();
        currentEnd = lineUp.end();

        //Intake first
        robot.getHorizontalSlide().extend(0.2);
        robot.getIntake().timedIntake(0,200);
        robot.waitForCommandsToFinish();

        TrajectorySequence intake = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setVelConstraint(new MecanumVelocityConstraint(10, robot.getRobotContext().getDescriptor().DRIVE_TUNER.driveTrackWidth))
                .forward(9)
                .resetVelConstraint()
                .build();
        robot.getDriveTrain().followTrajectory(intake);
        currentEnd = intake.end();
        robot.getHorizontalSlide().extend(0.3);
        robot.getIntake().timedIntake(1,1000);
        robot.waitForCommandsToFinish();
    }

    public void collectSecondPreset(){
        //turn to second
        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.GROUND);
        TrajectorySequence collect2 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToSplineHeading(new Pose2d(-55 * getAlliance().getTranslation(), -50 * getAlliance().getTranslation(),Math.toRadians(91 + getAlliance().getRotation())))
                //.turn(Math.toRadians(75))
                .build();
        robot.getDriveTrain().followTrajectory(collect2);
        robot.waitForCommandsToFinish();
        currentEnd = collect2.end();

        //Intake first
        robot.getHorizontalSlide().extend(0.25);
        robot.getIntake().timedIntake(0,170);
        robot.waitForCommandsToFinish();

        TrajectorySequence push1 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .forward(6)
                .build();
        robot.getDriveTrain().followTrajectory(push1);
        currentEnd = push1.end();
        robot.getHorizontalSlide().extend(0.35);
        robot.getIntake().timedIntake(1,1000);
        robot.waitForCommandsToFinish();
    }

    public void collectThirdPreset(){
        //drive to third
        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.GROUND);
        TrajectorySequence collect3 =robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToSplineHeading(new Pose2d(-50 * getAlliance().getTranslation(),-46 * getAlliance().getTranslation(),Math.toRadians((127 + getAlliance().getRotation()))))
                .build();
        robot.getDriveTrain().followTrajectory(collect3);
        robot.waitForCommandsToFinish();
        currentEnd = collect3.end();

        //Intake third
        robot.getHorizontalSlide().extend(0.5);
        robot.getIntake().timedIntake(0,300);
        robot.waitForCommandsToFinish();

        TrajectorySequence push1 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setVelConstraint(new MecanumVelocityConstraint(10, robot.getRobotContext().getDescriptor().DRIVE_TUNER.driveTrackWidth))
                .forward(9)
                .resetVelConstraint()
                .build();
        robot.getDriveTrain().followTrajectory(push1);
        currentEnd = push1.end();
        robot.getHorizontalSlide().extend(0.6);
        robot.getIntake().timedIntake(1,1000);
        robot.waitForCommandsToFinish();
    }

    public void scoresample(){
        TrajectorySequence deliver1 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToSplineHeading(new Pose2d(-48 * getAlliance().getTranslation(),-48 * getAlliance().getTranslation(), Math.toRadians(45 + getAlliance().getRotation())))
                //.turn(Math.toRadians(-45))
                .build();
        robot.getDriveTrain().followTrajectory(deliver1);
        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.HIGH_BASKET);
        robot.getHorizontalSlide().contract(0);
        robot.waitForCommandsToFinish();
        currentEnd = deliver1.end();

        //Outtake first
        robot.getIntake().timedIntake(-0.5,200);
        robot.waitForCommandsToFinish();
    }

    @Override
    public void park() {
        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.GROUND);
        TrajectorySequence trajectorySequence = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(90 + getAlliance().getRotation()))
                .splineToLinearHeading(new Pose2d(-24 * getAlliance().getTranslation(),-11 * getAlliance().getTranslation(),Math.toRadians(180 + getAlliance().getRotation())),Math.toRadians(0 + getAlliance().getRotation()))
                .build();

        robot.getDriveTrain().followTrajectory(trajectorySequence);
        robot.waitForCommandsToFinish();

        currentEnd = trajectorySequence.end();
    }

}
