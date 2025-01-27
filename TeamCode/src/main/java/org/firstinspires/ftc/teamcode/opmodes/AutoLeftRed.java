package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class AutoLeftRed extends AutoLeft{

    @Override
    public Alliance getAlliance() {
        return Alliance.RED;
    }
}
