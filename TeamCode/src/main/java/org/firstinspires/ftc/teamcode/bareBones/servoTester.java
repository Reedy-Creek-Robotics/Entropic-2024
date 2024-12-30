package org.firstinspires.ftc.teamcode.bareBones;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.game.Controller;

@TeleOp(group = "Barebone Component Testing")
public class servoTester extends OpMode {
    public Servo arm;
    public Servo linkage;

    public Controller controller;

    double linkagePos = 0, armPos = 0;

    @Override
    public void init() {
        arm = hardwareMap.servo.get("ArmRotation");
        linkage = hardwareMap.servo.get("Linkage");

        arm.setPosition(armPos);
        linkage.setPosition(linkagePos);
    }

    @Override
    public void loop() {
        if(controller.isPressed(Controller.Button.DPAD_UP)){
            armPos += 0.05;
        }else if (controller.isPressed(Controller.Button.DPAD_DOWN)){
            armPos -= 0.05;
        }

        if(controller.isPressed(Controller.Button.DPAD_RIGHT)){
            linkagePos += 0.05;
        }else if (controller.isPressed(Controller.Button.DPAD_LEFT)){
            linkagePos -= 0.05;
        }

        arm.setPosition(armPos);
        linkage.setPosition(linkagePos);
    }
}
