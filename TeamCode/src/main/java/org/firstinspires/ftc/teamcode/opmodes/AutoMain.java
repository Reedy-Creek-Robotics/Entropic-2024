package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.Robot;

public abstract class AutoMain extends LinearOpMode {
    protected Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        initRobot();

        telemetry.log().add("Wait for start", "");

        waitForStart();

        park();


    }

    public void initRobot(){
        robot = new Robot(this);
        robot.init();
    }

    public abstract void park();
}
