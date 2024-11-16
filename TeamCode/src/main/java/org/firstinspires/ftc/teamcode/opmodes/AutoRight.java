package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Autonomous
public class AutoRight extends AutoMain{

    @Override
    public Pose2d getStartPosition() {
        return new Pose2d(12,-72+robot.getRobotContext().getDescriptor().ROBOT_DIMENSIONS_IN_INCHES.height/2,Math.toRadians(90));
    }

    @Override
    public TrajectorySequence autoTrajectory(Pose2d pos) {
        return robot.getDriveTrain().trajectoryBuilder(pos)
                //go to sub
                .splineToConstantHeading(new Vector2d(9,-24-9),Math.toRadians(90))
                //go to leftmost spike
                .setTangent(Math.toRadians(-30))
                .splineToConstantHeading(new Vector2d(48,-36),Math.toRadians(0))
                //go to parking
                .setTangent(Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(56, -60, Math.toRadians(-90)), Math.toRadians(-90))
                .build();
    }
}
