package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.drive.Drive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.BaseComponent;
import org.firstinspires.ftc.teamcode.components.DriveTrain;
import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.teamcode.game.Controller;

@TeleOp
public class TestTeleOp extends OpMode {

    static double CLAW_OPEN_POSITION = 0.5; // Adjust as needed
    static double CLAW_CLOSED_POSITION = 0.05; // Adjust as needed

    DcMotor frontLeft, frontRight, backLeft, backRight;

    Servo claw;

    private DriveTrain driveTrain;
    Controller controller;

    ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    private double drive = 0, strafe = 0, turn = 0;

    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, "leftFront");
        frontRight = hardwareMap.get(DcMotor.class, "rightFront");
        backLeft = hardwareMap.get(DcMotor.class, "leftRear");
        backRight = hardwareMap.get(DcMotor.class, "rightRear");
        claw = hardwareMap.get(Servo.class, "claw");
    }

    public void loop(){

        double y = -gamepad1.left_stick_y; // Remember, Y stick is reversed!
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;

        frontLeft.setPower(y + x + rx);
        backLeft.setPower(y - x + rx);
        frontRight.setPower(y - x - rx);
        backRight.setPower(y + x - rx);


        if(gamepad1.a && timer.milliseconds() > 500) {
            timer.reset();
            if(claw.getPosition() == CLAW_OPEN_POSITION) {
                claw.setPosition(CLAW_CLOSED_POSITION);
                telemetry.addData("Claw Status", "Closed");
            }
            else{
                claw.setPosition(CLAW_OPEN_POSITION);
                    telemetry.addData("Claw Status", "Open");
            }
            telemetry.update();

        }
    }
}
