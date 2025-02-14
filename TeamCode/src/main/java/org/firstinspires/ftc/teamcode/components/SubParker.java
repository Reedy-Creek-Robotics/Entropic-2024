package org.firstinspires.ftc.teamcode.components;

import com.arcrobotics.ftclib.hardware.ServoEx;

public class SubParker extends BaseComponent{

    public ServoEx servo;

    public double start = 0, parked = 1;

    public SubParker(RobotContext context) {
        super(context);

        servo = (ServoEx) hardwareMap.servo.get("Sub");
    }

    @Override
    public void init() {
        servo.setPosition(start);
    }

    public void park(){
        servo.setPosition(parked);
    }
}
