package org.firstinspires.ftc.teamcode.bareBones;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.game.Controller;

@TeleOp(group = "Barebone Component Testing")
public class intakeTester extends OpMode {
    public Servo leftRotator;
    public Servo rightRotator;
    public Servo leftLinkage;
    public Servo rightLinkage;

    public Controller controller;

    double leftLinkagePos = LinkagePos.START.left;
    double rightLinkagePos = LinkagePos.START.right;
    double leftRotationPos = RotatorPos.START.left;
    double rightRotationPos = RotatorPos.START.right;


    public enum LinkagePos{
        START(0.9,0.04),
        END(0.55,0.29);

        double left, right;
        LinkagePos(double left, double right) {
            this.left = left;
            this.right = right;
        }
    }

    public enum RotatorPos{
        START(0.75,0.1),
        END(0.2,0.65);

        double left, right;
        RotatorPos(double left, double right) {
            this.left = left;
            this.right = right;
        }
    }

    @Override
    public void init() {
        controller = new Controller(gamepad1);
        leftRotator = hardwareMap.servo.get("RotatorLeft");
        rightRotator = hardwareMap.servo.get("RotatorRight");
        leftLinkage = hardwareMap.servo.get("LinkageLeft");
        rightLinkage = hardwareMap.servo.get("LinkageRight");

        leftRotator.setPosition(leftRotationPos);
        rightRotator.setPosition(rightRotationPos);

        leftLinkage.setPosition(leftLinkagePos);
        rightLinkage.setPosition(rightLinkagePos);
    }

    @Override
    public void loop() {
        double incrementValue = 0.05;
        if(controller.isPressed(Controller.Button.DPAD_UP)){
            leftLinkagePos += incrementValue;
            rightLinkagePos -= incrementValue;

        }else if (controller.isPressed(Controller.Button.DPAD_DOWN)){
            leftLinkagePos -= incrementValue;
            rightLinkagePos += incrementValue;
        }

        if(controller.isPressed(Controller.Button.DPAD_RIGHT)){
            leftLinkagePos += incrementValue;
            rightLinkagePos -= incrementValue;
        }else if (controller.isPressed(Controller.Button.DPAD_LEFT)){
            leftLinkagePos -= incrementValue;
            rightLinkagePos += incrementValue;
        }

        if(controller.isPressed(Controller.Button.A)){
            leftRotationPos = RotatorPos.START.left;
            rightRotationPos = RotatorPos.START.right;
        }else if (controller.isPressed(Controller.Button.B)){
            leftRotationPos = RotatorPos.END.left;
            rightRotationPos = RotatorPos.END.right;
        }else if(controller.isPressed(Controller.Button.X)){
            leftLinkagePos = LinkagePos.START.left;
            rightLinkagePos = LinkagePos.START.right;
        }else if (controller.isPressed(Controller.Button.Y)){
            leftLinkagePos = LinkagePos.END.left;
            rightLinkagePos = LinkagePos.END.right;
        }

        leftRotator.setPosition(leftRotationPos);
        rightRotator.setPosition(rightRotationPos);

        leftLinkage.setPosition(leftLinkagePos);
        rightLinkage.setPosition(rightLinkagePos);

        telemetry.addData("left rot pos:", leftRotationPos);
        telemetry.addData("right rot pos:", rightRotationPos);
        telemetry.addData("left link pos:", leftLinkagePos);
        telemetry.addData("right link pos:", rightLinkagePos);
        telemetry.update();
    }
}
