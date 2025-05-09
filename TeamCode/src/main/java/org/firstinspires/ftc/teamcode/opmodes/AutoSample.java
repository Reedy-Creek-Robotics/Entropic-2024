package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint;

import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.teamcode.components.ScoringSlide;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Config
public abstract class AutoSample extends AutoMain{
    public static boolean PRELOAD = true, FIRST = true, SECOND = true, THIRD = true, PARK = true;

    @Override
    public Pose2d getStartPosition() {
        return new Pose2d((-47 + 9) * getAlliance().getTranslation(), (-72+9) * getAlliance().getTranslation(), Math.toRadians(90 + getAlliance().getRotation()));
    }

    @Override
    public void loadPaths() {
        telemetry.addData("team: ", robotContext.getAlliance());


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

    public void collectFirstPreset(){
        robot.getRobotContext().record += "Collect Right Preset Sample \n";

        //turn to first
        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.GROUND);
        robot.getHorizontalSlide().extend(0.1);
        TrajectorySequence lineUp = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d(-47 * getAlliance().getTranslation(), -48 * getAlliance().getTranslation(),Math.toRadians(90 + getAlliance().getRotation())))
                //.turn(Math.toRadians(45))
                .build();
        robot.getDriveTrain().followTrajectory(lineUp);
        robot.waitForCommandsToFinish();
        currentEnd = lineUp.end();

        TrajectorySequence intake = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setVelConstraint(new MecanumVelocityConstraint(15, robot.getRobotContext().getDescriptor().DRIVE_TUNER.driveTrackWidth))
                .forward(7)
                .resetVelConstraint()
                .build();
        robot.getDriveTrain().followTrajectory(intake);
        currentEnd = intake.end();
        robot.getHorizontalSlide().extend(0.4);
        robot.getIntake().timedIntake(1,850);
        robot.waitForCommandsToFinish();
    }

    public void collectSecondPreset(){
        robot.getRobotContext().record += "Collect Middle Preset Sample \n";

        //turn to second
        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.GROUND);
        robot.getHorizontalSlide().extend(0.1);
        TrajectorySequence collect2 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToSplineHeading(new Pose2d(-57 * getAlliance().getTranslation(), -48 * getAlliance().getTranslation(),Math.toRadians(91 + getAlliance().getRotation())))
                //.turn(Math.toRadians(75))
                .build();
        robot.getDriveTrain().followTrajectory(collect2);
        robot.waitForCommandsToFinish();
        currentEnd = collect2.end();

        TrajectorySequence push1 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setVelConstraint(new MecanumVelocityConstraint(15, robot.getRobotContext().getDescriptor().DRIVE_TUNER.driveTrackWidth))
                .forward(7)
                .build();
        robot.getDriveTrain().followTrajectory(push1);
        currentEnd = push1.end();
        robot.getHorizontalSlide().extend(0.4);
        robot.getIntake().timedIntake(1,850);
        robot.waitForCommandsToFinish();
    }

    public void collectThirdPreset(){
        robot.getRobotContext().record += "Collect Left Preset Sample \n";

        //drive to third
        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.GROUND);
        robot.getHorizontalSlide().extend(0.2);
        TrajectorySequence collect3 =robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToSplineHeading(new Pose2d(-51 * getAlliance().getTranslation(),-46 * getAlliance().getTranslation(),Math.toRadians((127 + getAlliance().getRotation()))))
                .build();
        robot.getDriveTrain().followTrajectory(collect3);
        robot.waitForCommandsToFinish();
        currentEnd = collect3.end();

        TrajectorySequence push1 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setVelConstraint(new MecanumVelocityConstraint(12, robot.getRobotContext().getDescriptor().DRIVE_TUNER.driveTrackWidth))
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
        robot.getRobotContext().record += "Score Sample in High Basket \n";

        TrajectorySequence deliver1 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToSplineHeading(new Pose2d(-47.6 * getAlliance().getTranslation(),-47.6 * getAlliance().getTranslation(), Math.toRadians(45 + getAlliance().getRotation())))
                //.turn(Math.toRadians(-45))
                .build();
        robot.getDriveTrain().followTrajectory(deliver1);
        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.HIGH_BASKET);
        robot.getHorizontalSlide().contract(0);
        robot.waitForCommandsToFinish();
        currentEnd = deliver1.end();

        //Outtake first
        robot.getIntake().timedIntake(-0.5,250);
        robot.waitForCommandsToFinish();
    }

    public void park() {
        robot.getRobotContext().record += "Park in Ascent Zone \n";

        robot.getScoringSlide().moveToHeight(ScoringSlide.Positions.GROUND);
        TrajectorySequence trajectorySequence = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d(-47 * getAlliance().getTranslation(),-38 * getAlliance().getTranslation(), Math.toRadians(90 + getAlliance().getRotation())))
                .setTangent(Math.toRadians(90 + getAlliance().getRotation()))
                .splineTo(new Vector2d( -18 * getAlliance().getTranslation(),-11 * getAlliance().getTranslation()),Math.toRadians(0 + getAlliance().getRotation()))//,Math.toRadians(0 + getAlliance().getRotation()))
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
                    collectRegion(0.6);
                    break;
                //Center region
                case 1:
                    robot.getRobotContext().record += "aiming center..\n";
                    collectRegion(0.1);
                    break;
                //Right region
                case 2:
                    robot.getRobotContext().record += "aiming right..\n";
                    aimRightRegion();
                    collectRegion(0.6);
                    break;
            }
            

        } else{robot.getRobotContext().record += "No Elements Found on Field\n";}

        /*
        TrajectorySequence leaveSub = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(0 + getAlliance().getRotation()))
                .splineToSplineHeading(new Pose2d(-44 * getAlliance().getTranslation(),-15 * getAlliance().getTranslation(), Math.toRadians(90 + getAlliance().getRotation())), Math.toRadians(180 + getAlliance().getRotation()))
                .build();
        robot.getDriveTrain().followTrajectory(leaveSub);
        currentEnd = trajectorySequence.end();
        robot.waitForCommandsToFinish();
        */

        /*
        TrajectorySequence returnToSub = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                //.lineToLinearHeading(new Pose2d(-47 * getAlliance().getTranslation(),-38 * getAlliance().getTranslation(), Math.toRadians(90 + getAlliance().getRotation())))
                .setTangent(Math.toRadians(0 + getAlliance().getRotation()))
                .splineTo(new Vector2d( -18 * getAlliance().getTranslation(),-11 * getAlliance().getTranslation()),Math.toRadians(0 + getAlliance().getRotation()))//,Math.toRadians(0 + getAlliance().getRotation()))
                .build();
        robot.getDriveTrain().followTrajectory(returnToSub);
        currentEnd = trajectorySequence.end();
        robot.waitForCommandsToFinish();
        deliverPreload();

        robot.getLittleHanger().moveToHeight(LittleHanger.HangHeights.TOUCH);
         */
    }

    public void turnAround(){
        TrajectorySequence turnAround = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(180))
                .splineToLinearHeading(new Pose2d(-44 * getAlliance().getTranslation(),-10 * getAlliance().getTranslation(), Math.toRadians(180 + getAlliance().getRotation())), 180 + getAlliance().getRotation())
                .build();
        robot.getDriveTrain().followTrajectory(turnAround);
        currentEnd = turnAround.end();
        robot.waitForCommandsToFinish();
    }

    public void aimLeftRegion(){
        TrajectorySequence aimLeft = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d(-22 * getAlliance().getTranslation(),-11.5 * getAlliance().getTranslation(), Math.toRadians(20 + getAlliance().getRotation())))
                .build();
        robot.getDriveTrain().followTrajectory(aimLeft);
        currentEnd = aimLeft.end();
        robot.waitForCommandsToFinish();
    }
    public void aimRightRegion(){
        TrajectorySequence aimLeft = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineToLinearHeading(new Pose2d(-22 * getAlliance().getTranslation(),-11.5 * getAlliance().getTranslation(), Math.toRadians(342 + getAlliance().getRotation())))
                .build();
        robot.getDriveTrain().followTrajectory(aimLeft);
        currentEnd = aimLeft.end();
        robot.waitForCommandsToFinish();
    }

    public void collectRegion(double primaryExtension) {
        robot.getRobotContext().record += "collecting...\n";
        
        robot.getHorizontalSlide().extend(primaryExtension);
        robot.getIntake().timedIntake(1,500);
        robot.waitForCommandsToFinish();
        robot.getIntake().timedIntake(1,800);
        robot.getHorizontalSlide().linkageExtend();
        robot.waitForCommandsToFinish();
        boolean otherTeamCollected = getAlliance() == RobotContext.Alliance.BLUE ? robot.getIntake().isRedCollected() : robot.getIntake().isBlueCollected();
        if(otherTeamCollected) {
            robot.getIntake().timedIntake(-1, 800);
        }
        robot.getHorizontalSlide().contract(0);
        robot.waitForCommandsToFinish();
    }

}
