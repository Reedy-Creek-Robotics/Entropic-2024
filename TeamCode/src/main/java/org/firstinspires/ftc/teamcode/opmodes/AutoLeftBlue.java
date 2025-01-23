package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class AutoLeftBlue extends AutoLeft{
    @Override
    public Pose2d getStartPosition() {
        return new Pose2d(-12, 72-9, Math.toRadians(90));
    }

    @Override
    public double getAlliance() {
        return -1;
    }
}
