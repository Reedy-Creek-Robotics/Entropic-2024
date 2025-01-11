package org.firstinspires.ftc.teamcode.bareBones;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.game.Controller;

@TeleOp(group = "Barebone Component Testing")
public class intakeTester extends OpMode {
    public Servo arm;
    public Servo linkage;

    public Controller controller;

    double startLinkagePos = 0.15;
    double endLinkagePos = 0.75;
    double startRotationPos = 0.65;
    double endRotationPos = 0.15;
    double linkagePos = startLinkagePos;
    double rotationPos = startRotationPos;

    @Override
    public void init() {
        controller = new Controller(gamepad1);
        arm = hardwareMap.servo.get("Rotator");
        linkage = hardwareMap.servo.get("Linkage");

        arm.setPosition(rotationPos);
        linkage.setPosition(linkagePos);
    }

    @Override
    public void loop() {
        double incrementValue = 0.05;
        if(controller.isPressed(Controller.Button.DPAD_UP)){
            rotationPos += incrementValue;
        }else if (controller.isPressed(Controller.Button.DPAD_DOWN)){
            rotationPos -= incrementValue;
        }

        if(controller.isPressed(Controller.Button.DPAD_RIGHT)){
            linkagePos += incrementValue;
        }else if (controller.isPressed(Controller.Button.DPAD_LEFT)){
            linkagePos -= incrementValue;
        }

        if(controller.isPressed(Controller.Button.A)){
            rotationPos = startRotationPos;
        }else if (controller.isPressed(Controller.Button.B)){
            rotationPos = endRotationPos;
        }else if(controller.isPressed(Controller.Button.X)){
            linkagePos = startLinkagePos;
        }else if (controller.isPressed(Controller.Button.Y)){
            linkagePos = endLinkagePos;
        }

        arm.setPosition(rotationPos);
        linkage.setPosition(linkagePos);

        telemetry.addData("arm pos:", rotationPos);
        telemetry.addData("linkage pos:", linkagePos);
        telemetry.update();
    }
}
