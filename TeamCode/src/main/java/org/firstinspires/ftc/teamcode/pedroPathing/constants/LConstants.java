package org.firstinspires.ftc.teamcode.pedroPathing.constants;

import com.pedropathing.localization.*;
import com.pedropathing.localization.constants.*;

/*
0.003
 */

public class LConstants {
    static {
        ThreeWheelConstants.forwardTicksToInches = .003;
        ThreeWheelConstants.strafeTicksToInches = .003;
        ThreeWheelConstants.turnTicksToInches = .003;
        ThreeWheelConstants.leftY = 7.6;
        ThreeWheelConstants.rightY = -7.6;
        ThreeWheelConstants.strafeX = -4.5;
        ThreeWheelConstants.leftEncoder_HardwareMapName = "leftFront";
        ThreeWheelConstants.rightEncoder_HardwareMapName = "rightFront";
        ThreeWheelConstants.strafeEncoder_HardwareMapName = "leftRear";
        ThreeWheelConstants.leftEncoderDirection = Encoder.REVERSE;
        ThreeWheelConstants.rightEncoderDirection = Encoder.REVERSE;
        ThreeWheelConstants.strafeEncoderDirection = Encoder.FORWARD;
    }
}




