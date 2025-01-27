package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class AutoRightRed extends AutoRight{

    @Override
    public Alliance getAlliance() {
        return Alliance.RED;
    }
}
