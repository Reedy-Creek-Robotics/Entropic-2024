package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.components.LittleHanger;
import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Config
@Autonomous
public class Autotest extends AutoMain{
    public static double X1 = 9,Y2 = -38, ANGLE1 = 30, ANGLE2 = 65,ANGLE3 = 23, ANGLE4 = 23, Y1 = -52;

    //public static double ANGLE1 = 45, ANGLE2 = 75, X1 = -49, X2 = -56, ANGLE3 = 45, ANGLE4= 75, ANGLE5 = 82;

    @Override
    public void runPath() {
        TrajectorySequence trajectorySequenceRight = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(90))
                .lineToConstantHeading(new Vector2d(X1,(Y2)))
                //deliver preload

                .setTangent(Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(48+9,Y1,Math.toRadians(ANGLE2)),Math.toRadians(ANGLE1))
                //throw out 73,23, intake, pull back
                .turn(Math.toRadians(ANGLE3))
                //outtake
                //throwout under 7323 over 7818, intake, pullback
                .turn(Math.toRadians(ANGLE4))
                //outtake
                //throwout 7323, intake, pullback
                //wait and outtake
                .build();

       /* TrajectorySequence trajectorySequenceLeft = robot.getDriveTrain().trajectoryBuilder(currentEnd)
                .setTangent(Math.toRadians(150))
                .splineToConstantHeading(new Vector2d(X1,-49),Math.toRadians(180))
                .turn(Math.toRadians(-45))

                .turn(Math.toRadians(ANGLE1))
                //7818
                .turn(Math.toRadians(-1 * ANGLE3))

                .turn(Math.toRadians(ANGLE2))
                //7818
                .turn(Math.toRadians(-1 * ANGLE4))

                .lineToLinearHeading(new Pose2d(X2,-49,Math.toRadians(45 + ANGLE5)))
                //7026
                //.lineToLinearHeading(new Pose2d(X1,-49,Math.toRadians(45)))
                .build();*/

        robot.getDriveTrain().followTrajectory(trajectorySequenceRight);
        robot.waitForCommandsToFinish();

        currentEnd = trajectorySequenceRight.end();
    }

    @Override
    public Pose2d getStartPosition() {
        return new Pose2d(-47 + 9, -72+9, Math.toRadians(90));//new Pose2d(23.5-9, -72+9, Math.toRadians(270));
    }

    @Override
    public RobotContext.Alliance getAlliance() {
        return RobotContext.Alliance.RED;
    }

    @Override
    public void park() {

    }
}
