package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.components.RobotContext;

@Autonomous
public class AutoSampleRed extends AutoSample {

    @Override
    public RobotContext.Alliance getAlliance() {
        return RobotContext.Alliance.RED;
    }
}
