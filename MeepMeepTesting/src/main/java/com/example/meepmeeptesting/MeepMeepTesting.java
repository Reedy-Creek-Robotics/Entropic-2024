package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(750);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(12,-72+9 , Math.toRadians(90)))
                        //go to sub
                        .splineToConstantHeading(new Vector2d(9,-24-9),Math.toRadians(90))
                        //go to leftmost spike
                        .setTangent(Math.toRadians(-30))
                        .splineToConstantHeading(new Vector2d(48,-36),Math.toRadians(0))
                        //go to parking
                        .setTangent(Math.toRadians(-90))
                        .splineToLinearHeading(new Pose2d(56, -60, Math.toRadians(-90)), Math.toRadians(-90))
                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}