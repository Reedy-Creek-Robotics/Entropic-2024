package org.firstinspires.ftc.teamcode.bareBones;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.game.Controller;

@TeleOp(group = "Barebone Component Testing")
public class L2Tester extends OpMode {
    public DcMotor L2AscentLeft;
    public DcMotor L2AscentRight;



    public Controller controller;

    int height1 = 300, height2 = 550, height3 = 900;

    boolean manualMode = true;

    @Override
    public void init() {
        controller = new Controller(gamepad1);
        L2AscentLeft = hardwareMap.dcMotor.get("L2AscentLeft");
        L2AscentRight = hardwareMap.dcMotor.get("L2AscentRight");

        L2AscentLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        L2AscentRight.setDirection(DcMotorSimple.Direction.REVERSE);

        L2AscentLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        L2AscentLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        L2AscentRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        L2AscentRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    @Override
    public void loop() {
        double power = controller.leftStickY();
        if(Math.abs(power)>0.1 ) {manualMode = true;}

        if(manualMode) {
            L2AscentLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            L2AscentLeft.setPower(power * 0.5);
            L2AscentRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            L2AscentRight.setPower(power * 0.5);
        }else {
            L2AscentLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            L2AscentRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        if(controller.isPressed(Controller.Button.A)){
            manualMode = false;
            L2AscentLeft.setTargetPosition(height1);
            L2AscentLeft.setPower(.5);
            L2AscentRight.setTargetPosition(height1);
            L2AscentRight.setPower(.5);
        }else if (controller.isPressed(Controller.Button.B)){
            manualMode = false;
            L2AscentLeft.setTargetPosition(height2);
            L2AscentLeft.setPower(.5);
            L2AscentRight.setTargetPosition(height2);
            L2AscentRight.setPower(.5);
        }else if (controller.isPressed(Controller.Button.Y)){
            manualMode = false;
            L2AscentLeft.setTargetPosition(height3);
            L2AscentLeft.setPower(.5);
            L2AscentRight.setTargetPosition(height3);
            L2AscentRight.setPower(.5);

        }

        telemetry.addData("Left Position", L2AscentLeft.getCurrentPosition());
        telemetry.addData("Left Power", L2AscentLeft.getPower());
        telemetry.addData("Right Position", L2AscentRight.getCurrentPosition());
        telemetry.addData("Right Power", L2AscentRight.getPower());

        telemetry.update();
    }
}
