package org.firstinspires.ftc.teamcode.bareBones;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.game.Controller;

@TeleOp(group = "Barebone Component Testing")
public class intakeTester extends OpMode {
    /**
     * Steps for tuning intake
     *  - Find matching START positions in Hardware Tester
     *  - Update START position in this opmode
     *  - Increment in this Opmode to find END positions
     *  - Update START and END in Intake class
     **/

    public Servo leftRotator;
    public Servo rightRotator;
    public Servo leftLinkage;
    public Servo rightLinkage;

    public Controller controller;

    double leftLinkagePos = LinkagePos.START.left;
    double rightLinkagePos = LinkagePos.START.right;
    double leftRotationPos = RotatorPos.START.left;
    double rightRotationPos = RotatorPos.START.right;

    boolean slow_mode = false;
    double incrementValue = 0.05;

    public enum LinkagePos{
        START(0.93,0.03),//0.9,0.04
        END(0.7,0.26);//0.55,0.29

        double left, right;
        LinkagePos(double left, double right) {
            this.left = left;
            this.right = right;
        }
    }

    public enum RotatorPos{
        START(0.73,0.4),
        END(0.22,0.91);

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
        if(controller.isPressed(Controller.Button.RIGHT_BUMPER)){
            slow_mode = !slow_mode;
        }

        incrementValue = slow_mode ?  0.01 : 0.05;
        if(controller.isPressed(Controller.Button.DPAD_UP)){
            leftLinkagePos += incrementValue;
            rightLinkagePos -= incrementValue;

        }else if (controller.isPressed(Controller.Button.DPAD_DOWN)){
            leftLinkagePos -= incrementValue;
            rightLinkagePos += incrementValue;
        }

        if(controller.isPressed(Controller.Button.DPAD_RIGHT)){
            leftRotationPos += incrementValue;
            rightRotationPos -= incrementValue;
        }else if (controller.isPressed(Controller.Button.DPAD_LEFT)){
            leftRotationPos -= incrementValue;
            rightRotationPos += incrementValue;
        }

        if(controller.isPressed(Controller.Button.CROSS)){
            leftRotationPos = RotatorPos.START.left;
            rightRotationPos = RotatorPos.START.right;
        }else if (controller.isPressed(Controller.Button.TRIANGLE)){
            leftRotationPos = RotatorPos.END.left;
            rightRotationPos = RotatorPos.END.right;
        }else if(controller.isPressed(Controller.Button.SQUARE)){
            leftLinkagePos = LinkagePos.START.left;
            rightLinkagePos = LinkagePos.START.right;
        }else if (controller.isPressed(Controller.Button.CIRCLE)){
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
