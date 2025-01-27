package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity left = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 40, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d((-47 + 9) * -1, (-72+9) * -1, Math.toRadians(90 + 180)))
                        //preload
                        .setTangent(Math.toRadians(150 * -1))
                        .splineToConstantHeading(new Vector2d(-49 * -1,-49 * -1),Math.toRadians(180 + 180))
                        .turn(Math.toRadians(-45))

                        .turn(Math.toRadians(40))
                        .turn(Math.toRadians(-40))

                        .turn(Math.toRadians(65))
                        .turn(Math.toRadians(-65))


                        .lineToLinearHeading(new Pose2d(-56 * -1,-49 * -1,Math.toRadians(115 + 180)))
                        .lineToLinearHeading(new Pose2d(-49 * -1,-49 * -1,Math.toRadians(45 + 180)))

                        .setTangent(Math.toRadians(90 * -1))
                        .splineToLinearHeading(new Pose2d(-24 * -1,-11 * -1,Math.toRadians(180  + 180)),Math.toRadians(0 + 180))

                        .build());

        RoadRunnerBotEntity right = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 40, Math.toRadians(180), Math.toRadians(180), 18)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d((23.5-9) * -1, (-72+9) * -1, Math.toRadians(270 * -1)))
                        .setTangent(Math.toRadians(90 + 180))
                        .splineToConstantHeading(new Vector2d(10 * -1,(-38 * -1)),Math.toRadians(90 + 180))
                        //deliver 3
                        .setTangent(Math.toRadians(-90 + 180))
                        .splineToLinearHeading(new Pose2d((48+9) * -1,-48 * -1,Math.toRadians(60 + 180)),Math.toRadians(0 + 180))
                        //
                        .turn(Math.toRadians(20))
                        .turn(Math.toRadians(20))
                        .lineToLinearHeading(new Pose2d((48+9) * -1,-60 * -1,Math.toRadians(90 + 180)))
                        .build());

        /*RoadRunnerBotEntity right2 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 40, Math.toRadians(180), Math.toRadians(180), 18)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(12, -(-72+9), Math.toRadians(90)))
                        //preload
                        .setTangent(Math.toRadians(-90))
                        .splineToConstantHeading(new Vector2d(10,-(-24-8)),Math.toRadians(-90))
                        //deliver 1
                        .setTangent(Math.toRadians(90))
                        .splineToLinearHeading(new Pose2d(38,30,Math.toRadians(-45)),Math.toRadians(-45))
                        .setTangent(Math.toRadians(90))
                        .splineToLinearHeading(new Pose2d(48,48,Math.toRadians(90)),Math.toRadians(90))
                        //.splineToConstantHeading(new Vector2d(48,-60),Math.toRadians(-90))
                        .setTangent(Math.toRadians(90))
                        .splineToConstantHeading(new Vector2d(10,-24-8),Math.toRadians(90))
                        //deliver 2
                        .setTangent(Math.toRadians(-90))
                        .splineToLinearHeading(new Pose2d(50,-30,Math.toRadians(45)),Math.toRadians(45))
                        .setTangent(Math.toRadians(-90))
                        .splineToLinearHeading(new Pose2d(48,-48,Math.toRadians(-90)),Math.toRadians(-90))
                        //deliver 3
                        .setTangent(Math.toRadians(-90))
                        .splineToLinearHeading(new Pose2d(56,-24,Math.toRadians(0)),Math.toRadians(0))
                        .setTangent(Math.toRadians(-90))
                        .splineToLinearHeading(new Pose2d(48,-48,Math.toRadians(-90)),Math.toRadians(-90))
                        .splineToConstantHeading(new Vector2d(48,-60),Math.toRadians(-90))
                        .setTangent(Math.toRadians(90))
                        .splineToConstantHeading(new Vector2d(10,-24-8),Math.toRadians(90))
                        //park
                        .setTangent(Math.toRadians(-90))
                        .splineToConstantHeading(new Vector2d(36,-60),Math.toRadians(-90))
                        .build());*/

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(left)
                .addEntity(right)
                //.addEntity(right2)
                .start();
    }
}