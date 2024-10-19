package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.components.BaseComponent;
import org.firstinspires.ftc.teamcode.components.DriveTrain;
import org.firstinspires.ftc.teamcode.components.Hooker;
import org.firstinspires.ftc.teamcode.components.Intake;
import org.firstinspires.ftc.teamcode.components.RobotContext;

@Autonomous
public class AutoRight extends AutoMain{

    DriveTrain driveTrain;
    RobotContext robotContext;
    DcMotorEx leftFront, leftRear, rightRear, rightFront;

    @Override
    public void park() {
        leftFront = hardwareMap.get(DcMotorEx.class, "FrontLeft");
        leftRear = hardwareMap.get(DcMotorEx.class, "BackLeft");
        rightRear = hardwareMap.get(DcMotorEx.class, "BackRight");
        rightFront = hardwareMap.get(DcMotorEx.class, "FrontRight");
        leftFront.setDirection(DcMotorEx.Direction.REVERSE);
        leftRear.setDirection(DcMotorEx.Direction.REVERSE);
        rightRear.setDirection(DcMotorEx.Direction.FORWARD);
        rightFront.setDirection(DcMotorEx.Direction.FORWARD);


        double startTime = getRuntime();

        leftFront.setPower(-0.5);
        leftRear.setPower(-0.5);
        rightRear.setPower(-0.5);
        rightFront.setPower(-0.5);

        while(getRuntime() - startTime < 2){
            telemetry.addData("Waiting...", true);
        }

        leftFront.setPower(0);
        leftRear.setPower(0);
        rightRear.setPower(0);
        rightFront.setPower(0);
    }
}
