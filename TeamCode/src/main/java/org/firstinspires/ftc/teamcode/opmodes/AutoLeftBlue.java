package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class AutoLeftBlue extends AutoLeft{
    @Override
    public Alliance getAlliance() {
        return Alliance.BLUE;
    }
}
