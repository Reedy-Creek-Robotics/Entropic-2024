package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.components.RobotContext;

@Autonomous
public class AutoLeftRed extends AutoLeft{

    @Override
    public RobotContext.Alliance getAlliance() {
        return RobotContext.Alliance.RED;
    }
}
