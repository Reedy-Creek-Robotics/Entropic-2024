package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MinAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.ProfileAccelerationConstraint;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        double intakeSpeed = 20;

        RoadRunnerBotEntity option1 = new DefaultBotBuilder(meepMeep)
                .setConstraints(60,60,Math.toRadians(210),Math.toRadians(210),15.09)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(34-9,-72+9,Math.toRadians(90)))
                        ////Middle
                        .lineToConstantHeading(new Vector2d(58,-48))
                        .forward(7,new MecanumVelocityConstraint(intakeSpeed,15.5),new ProfileAccelerationConstraint(60))
                        //
                        .lineToConstantHeading(new Vector2d(54,-53))
                        .waitSeconds(0.200)


                        //Right
                        .splineToLinearHeading(new Pose2d(54,-48,Math.toRadians(47)),Math.toRadians(80))
                        .forward(8,new MecanumVelocityConstraint(intakeSpeed,15.09),new ProfileAccelerationConstraint(60))
                        //
                        .lineToLinearHeading(new Pose2d(48,-53,Math.toRadians(90)))
                        .waitSeconds(0.200)

                        //Left
                        .forward(10,new MecanumVelocityConstraint(intakeSpeed,15.09),new ProfileAccelerationConstraint(60))
                        .lineToConstantHeading(new Vector2d(48,-53))
                        .waitSeconds(0.200)



                        //1
                        .setTangent(Math.toRadians(120))
                        .splineToLinearHeading(new Pose2d(30,-42,Math.toRadians(315)), Math.toRadians(160))
                        .forward(6,new MecanumVelocityConstraint(intakeSpeed,15.09),new ProfileAccelerationConstraint(60))
                        //
                        .setTangent(Math.toRadians(175))
                        .splineToLinearHeading(new Pose2d(10,-36.5,Math.toRadians(-90)),Math.toRadians(130))


                        //2
                        .lineToLinearHeading(new Pose2d(30,-42,Math.toRadians(-45)))
                        .forward(6,new MecanumVelocityConstraint(intakeSpeed,15.09),new ProfileAccelerationConstraint(60))
                        //
                        .setTangent(Math.toRadians(175))
                        .splineToLinearHeading(new Pose2d(7,-36.5,Math.toRadians(-90)),Math.toRadians(140))

                        //3
                        .lineToLinearHeading(new Pose2d(30,-42,Math.toRadians(-45)))
                        .forward(6,new MecanumVelocityConstraint(15,15.09),new ProfileAccelerationConstraint(60))
                        //
                        .setTangent(Math.toRadians(175))
                        .splineToLinearHeading(new Pose2d(4,-36.5,Math.toRadians(-90)),Math.toRadians(150))

                        //4
                        .lineToLinearHeading(new Pose2d(30,-42,Math.toRadians(-45)))
                        .forward(6,new MecanumVelocityConstraint(15,15.09),new ProfileAccelerationConstraint(60))
                        //
                        .setTangent(Math.toRadians(175))
                        .splineToLinearHeading(new Pose2d(1,-36.5,Math.toRadians(-90)),Math.toRadians(155))

                        //5
                        .lineToLinearHeading(new Pose2d(30,-42,Math.toRadians(-45)))
                        .forward(6,new MecanumVelocityConstraint(15,15.09),new ProfileAccelerationConstraint(60))
                        //
                        .setTangent(Math.toRadians(175))
                        .splineToLinearHeading(new Pose2d(-2,-36.5,Math.toRadians(-90)),Math.toRadians(160))

                        //park
                        //.lineToLinearHeading(new Pose2d(28,-52,Math.toRadians(-45)))
                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(option1)
                .start();
    }
}

/*
RoadRunnerBotEntity test = new DefaultBotBuilder(meepMeep)
                .setConstraints(40,40,Math.toRadians(180),Math.toRadians(180),15)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(60, -33, Math.toRadians(54)))
                        .setTangent(Math.toRadians(-90))
                        .splineToLinearHeading(new Pose2d((48+9),-52,Math.toRadians(88)),Math.toRadians(310))
                        .build());


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