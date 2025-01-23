package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class AutoLeftRed extends AutoLeft{

    @Override
    public Pose2d getStartPosition() {
        return new Pose2d(-12, -72+9, Math.toRadians(270));
    }

    @Override
    public double getAlliance() {
        return 1;
    }
}
