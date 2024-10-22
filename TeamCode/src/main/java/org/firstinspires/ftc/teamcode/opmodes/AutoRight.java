package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

public class AutoRight extends AutoMain{

    @Override
    public Pose2d getStartPosition() {
        return new Pose2d(0,robot.getRobotContext().getDescriptor().ROBOT_DIMENSIONS_IN_INCHES.height/2,0);
    }

    @Override
    public TrajectorySequence toPark() {
        return null;
    }
}
