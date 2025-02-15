package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.teamcode.unfinishedAutos.AutoRightLong;

@Autonomous
public class AutoSpecimenBlue extends AutoSpecimen3 {

    @Override
    public RobotContext.Alliance getAlliance() {
        return RobotContext.Alliance.BLUE;
    }
}
