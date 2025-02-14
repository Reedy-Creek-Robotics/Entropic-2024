package org.firstinspires.ftc.teamcode.unfinishedAutos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.ProfileAccelerationConstraint;

import org.firstinspires.ftc.teamcode.components.LittleHanger;
import org.firstinspires.ftc.teamcode.components.ScoringSlide;
import org.firstinspires.ftc.teamcode.opmodes.AutoMain;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AutoLeftSingle extends AutoMain {
    public static boolean SCORE = true, PARK = true;

    TrajectorySequence scorePresets;

    @Override
    public Pose2d getStartPosition() {
        return new Pose2d((-47 + 9) * getAlliance().getTranslation(), (-72+9) * getAlliance().getTranslation(), Math.toRadians(90 + getAlliance().getRotation()));
    }

    @Override
    public void loadPaths() {
        telemetry.addData("team: ", robotContext.getAlliance());

        scorePresets = robot.getDriveTrain().trajectoryBuilder(getStartPosition())
                //Preload
                .setTangent(Math.toRadians(135 + getAlliance().getRotation()))
                .splineToSplineHeading(new Pose2d(-48 * getAlliance().getTranslation(),-48 * getAlliance().getTranslation(), Math.toRadians(45 + getAlliance().getRotation())),Math.toRadians(180 + getAlliance().getRotation()))
                //TODO FIND POS, May want move raise to after movement
                .addSpatialMarker(new Vector2d(-38 * getAlliance().getTranslation(),-55 * getAlliance().getTranslation()),() -> {//find pos
                    robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.HIGH_BASKET);
                    robot.waitForCommandsToFinish();

                    robot.getIntake().timedIntake(-1,170);
                    robot.waitForCommandsToFinish();

                    robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.GROUND);
                })

                //First
                .lineToLinearHeading(new Pose2d(-47 * getAlliance().getTranslation(), -48 * getAlliance().getTranslation(),Math.toRadians(90 + getAlliance().getRotation())))
                .addDisplacementMarker(() -> {
                    robot.getHorizontalSlide().extend(0.4);
                    robot.waitForCommandsToFinish();

                    robot.getIntake().timedIntake(1,800);
                })
                .forward(9,new MecanumVelocityConstraint(15,robot.getRobotContext().getDescriptor().DRIVE_TUNER.driveTrackWidth),new ProfileAccelerationConstraint(robot.getRobotContext().getDescriptor().DRIVE_TUNER.maxAccel))
                .addDisplacementMarker(() -> {
                    robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.HIGH_BASKET);
                    robot.getHorizontalSlide().contract(0);
                })
                .lineToSplineHeading(new Pose2d(-47.6 * getAlliance().getTranslation(),-47.6 * getAlliance().getTranslation(), Math.toRadians(45 + getAlliance().getRotation())))
                .addDisplacementMarker(()-> {
                    robot.waitForCommandsToFinish();

                    robot.getIntake().timedIntake(-0.5,200);
                    robot.waitForCommandsToFinish();

                    robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.GROUND);
                })

                //Second
                .lineToSplineHeading(new Pose2d(-56 * getAlliance().getTranslation(), -48 * getAlliance().getTranslation(),Math.toRadians(91 + getAlliance().getRotation())))
                .addDisplacementMarker(() -> {
                    robot.getHorizontalSlide().extend(0.4);
                    robot.waitForCommandsToFinish();

                    robot.getIntake().timedIntake(1,800);
                })
                .forward(7,new MecanumVelocityConstraint(15,robot.getRobotContext().getDescriptor().DRIVE_TUNER.driveTrackWidth),new ProfileAccelerationConstraint(robot.getRobotContext().getDescriptor().DRIVE_TUNER.maxAccel))
                .addDisplacementMarker(() -> {
                    robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.HIGH_BASKET);
                    robot.getHorizontalSlide().contract(0);
                })
                .lineToSplineHeading(new Pose2d(-47.6 * getAlliance().getTranslation(),-47.6 * getAlliance().getTranslation(), Math.toRadians(45 + getAlliance().getRotation())))
                .addDisplacementMarker(()-> {
                    robot.waitForCommandsToFinish();

                    robot.getIntake().timedIntake(-0.5,200);
                    robot.waitForCommandsToFinish();

                    robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.GROUND);
                })

                //Third
                .lineToSplineHeading(new Pose2d(-50 * getAlliance().getTranslation(),-46 * getAlliance().getTranslation(),Math.toRadians((127 + getAlliance().getRotation()))))
                .addDisplacementMarker(() -> {
                    robot.getHorizontalSlide().extend(0.6);
                    robot.waitForCommandsToFinish();

                    robot.getIntake().timedIntake(1,800);
                })
                .forward(9,new MecanumVelocityConstraint(12,robot.getRobotContext().getDescriptor().DRIVE_TUNER.driveTrackWidth),new ProfileAccelerationConstraint(robot.getRobotContext().getDescriptor().DRIVE_TUNER.maxAccel))
                .addDisplacementMarker(() -> {
                    robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.HIGH_BASKET);
                    robot.getHorizontalSlide().contract(0);
                })
                .lineToSplineHeading(new Pose2d(-47.6 * getAlliance().getTranslation(),-47.6 * getAlliance().getTranslation(), Math.toRadians(45 + getAlliance().getRotation())))
                .addDisplacementMarker(()-> {
                    robot.waitForCommandsToFinish();

                    robot.getIntake().timedIntake(-0.5,200);
                    robot.waitForCommandsToFinish();

                    robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.GROUND);
                })
                .build();

        currentEnd = scorePresets.end();
    }

    @Override
    public void runPath() {
        if (SCORE) {
            scoreSamples();
        }
        if (PARK){
            park();
        }
    }


    public void scoreSamples(){
        robot.getRobotContext().record += "Score Sample in High Basket \n";
        robot.getDriveTrain().followTrajectoryAlt(scorePresets);
    }

    public void deliverPreload() {
        robot.getRobotContext().record += "Drive to Deliver a Preloaded Sample \n";

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

    public void park() {
        robot.getRobotContext().record += "Park in Ascent Zone \n";

        TrajectorySequence trajectorySequence = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d(-48 * getAlliance().getTranslation(),-11 * getAlliance().getTranslation(), Math.toRadians(0 + getAlliance().getRotation())))
                .setTangent(Math.toRadians(0 + getAlliance().getRotation()))
                .lineToLinearHeading(new Pose2d(-20 * getAlliance().getTranslation(),-12.5 * getAlliance().getTranslation(),Math.toRadians(0 + getAlliance().getRotation())))//,Math.toRadians(0 + getAlliance().getRotation()))
                .build();
        robot.getDriveTrain().followTrajectory(trajectorySequence);
        currentEnd = trajectorySequence.end();
        robot.waitForCommandsToFinish();

        //machine vision
        List<List<Integer>> counts = robot.getMvs().getElementCounts();
        List<Double> totals = new ArrayList<>();
        totals.add(counts.get(0).get(0) + counts.get(1).get(0) * 1.1);
        totals.add(counts.get(0).get(1) + counts.get(1).get(1) * 1.1);
        totals.add(counts.get(0).get(2) + counts.get(1).get(2) * 1.1);

        int bestRegion = totals.indexOf(Collections.max(totals));
        robot.getRobotContext().record += "totals: ".concat(totals.toString()).concat("\n");
        if(Collections.max(totals) > 0) {
            //turnAround();
            robot.getRobotContext().record += "Best Region: ".concat((String.valueOf(bestRegion))).concat("\n");

            switch (bestRegion) {
                //Left region
                case 0:
                    robot.getRobotContext().record += "aiming left..\n";
                    aimLeftRegion();
                    break;
                //Center region
                case 1:
                    robot.getRobotContext().record += "aiming center..\n";
                    break;
                //Right region
                case 2:
                    robot.getRobotContext().record += "aiming right..\n";
                    aimRightRegion();
                    break;
            }

            collectRegion();
        } else{robot.getRobotContext().record += "No Elements Found on Field\n";}

        TrajectorySequence leaveSub = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(0 + getAlliance().getRotation()))
                .splineToSplineHeading(new Pose2d(-44 * getAlliance().getTranslation(),-15 * getAlliance().getTranslation(), Math.toRadians(90 + getAlliance().getRotation())), Math.toRadians(180 + getAlliance().getRotation()))
                .build();
        robot.getDriveTrain().followTrajectory(leaveSub);
        currentEnd = trajectorySequence.end();
        deliverPreload();


        robot.getLittleHanger().moveToHeight(LittleHanger.HangHeights.TOUCH);

        robot.waitForCommandsToFinish();
    }

    public void turnAround(){
        TrajectorySequence turnAround = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(180))
                .splineToLinearHeading(new Pose2d(-44,-10, Math.toRadians(180)), 180)
                .build();
        robot.getDriveTrain().followTrajectory(turnAround);
        currentEnd = turnAround.end();
        robot.waitForCommandsToFinish();
    }

    public void aimLeftRegion(){

    }
    public void aimRightRegion(){

    }

    public void collectRegion() {
        robot.getRobotContext().record += "collecting...\n";

        robot.getHorizontalSlide().extend(0.1);
        robot.getIntake().timedIntake(1,700);
        robot.waitForCommandsToFinish();
        robot.getIntake().timedIntake(1,1000);
        robot.getHorizontalSlide().linkageExtend();
        robot.waitForCommandsToFinish();
        robot.getHorizontalSlide().contract(0);
        robot.waitForCommandsToFinish();
    }

}

