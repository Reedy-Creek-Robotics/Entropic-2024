package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.components.LittleHanger;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Autonomous
public class Autotest extends AutoMain{
    @Override
    public void runPath() {
        TrajectorySequence trajectorySequence = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineTo(new Vector2d(0,24))
                .build();

        robot.getDriveTrain().followTrajectory(trajectorySequence);
        robot.getLittleHanger().moveToHeight(LittleHanger.HangHeights.PULL);
        robot.waitForCommandsToFinish();

        currentEnd = trajectorySequence.end();

        TrajectorySequence trajectorySequence2 = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .lineTo(new Vector2d(0,50))
                .build();

        robot.getDriveTrain().followTrajectory(trajectorySequence2);
        robot.waitForCommandsToFinish();

        currentEnd = trajectorySequence.end();
    }

    @Override
    public Pose2d getStartPosition() {
        return new Pose2d(0,0,Math.toRadians(90));
    }

    @Override
    public double getAlliance() {
        return 0;
    }

    @Override
    public void deliverPreload() {

    }

    @Override
    public void park() {

    }
}
