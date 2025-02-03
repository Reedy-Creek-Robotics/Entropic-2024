package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.components.ScoringSlide.Positions.*;
import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.LEFT_STICK_X;
import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.LEFT_STICK_Y;
import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.RIGHT_STICK_X;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.LittleHanger;
import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.teamcode.components.ScoringSlide;
import org.firstinspires.ftc.teamcode.game.Controller;

@TeleOp
public class TeloOpMain extends OpMode {

    Robot robot;

    double speed = 1;

    ElapsedTime timer = new ElapsedTime();
    Boolean extending = false;
    double linkagePos = 0;

    protected Controller driver;

    public Controller controller2;

    public ScoringSlide.Positions[] slidePositions = {GROUND, LOW_BASKET, HIGH_BASKET, HIGH_BAR, OVER_HIGH_BAR};
    public int slidePosIndex = 0;

    @Override
    public void init() {
        robot = new Robot(this);
        driver = new Controller(gamepad1);
        robot.loadPositionFromDisk();

        //add testing
        controller2 = new Controller(gamepad2);

        robot.init();
    }

    @Override
    public void loop() {
        if (driver.isPressed(LEFT_STICK_X, LEFT_STICK_Y, RIGHT_STICK_X) || !robot.getDriveTrain().isBusy()) {
            double drive = driver.leftStickY();
            double strafe = driver.leftStickX();
            double turn = driver.rightStickX();

            robot.getDriveTrain().driverRelative(drive, strafe, turn, speed);
        }

       /* if (driver.isPressed(Controller.Button.DPAD_UP)){
            robot.getHorizontalSlide().extend(1);
        } else if (driver.isPressed(Controller.Button.DPAD_DOWN)) {
            robot.getHorizontalSlide().contract();
        }*/
        if (driver.isPressed(Controller.Button.TRIANGLE)){
            robot.getLittleHanger().moveToHeight(LittleHanger.HangHeights.TOP);
        } else if (driver.isPressed(Controller.Button.CROSS)) {
            robot.getLittleHanger().moveToHeight(LittleHanger.HangHeights.PULL);
        }

        if (driver.isButtonDown(Controller.Button.DPAD_RIGHT)){
            if(timer.milliseconds()>=2 && linkagePos<1){
                timer.reset();
                linkagePos += 0.1;
            }
            robot.getHorizontalSlide().linkageMove(linkagePos);
        } else if (driver.isButtonDown(Controller.Button.DPAD_LEFT)) {
            if(timer.milliseconds()>=2 && linkagePos>0){
                timer.reset();
                linkagePos -= 0.1;
            }
            robot.getHorizontalSlide().linkageMove(linkagePos);
        }

        if (driver.rightTrigger() > 0.2){
            robot.getIntake().intake(1);
        } else if (driver.leftTrigger() > 0.2) {
            robot.getIntake().intake(-1);
        }else {
            robot.getIntake().intake(0);
        }

        if(driver.isPressed(Controller.Button.LEFT_BUMPER)) {
            robot.getHorizontalSlide().rotatorContract();
        }
        if(driver.isPressed(Controller.Button.RIGHT_BUMPER)) {
            robot.getHorizontalSlide().rotatorExtend();
        }

        if(driver.isPressed(Controller.Button.DPAD_DOWN)) {
            slidePosIndex = slidePosIndex++ < slidePositions.length - 1 ? slidePosIndex + 1 : 0;
            robot.getScoringSlide().moveToHeight(slidePositions[slidePosIndex]);
        } else if (driver.isPressed(Controller.Button.DPAD_UP)) {
            slidePosIndex = slidePosIndex-- > 0 ? slidePosIndex - 1 : slidePositions.length - 1;
            robot.getScoringSlide().moveToHeight(slidePositions[slidePosIndex]);
        }


        //testing
        if(controller2.isPressed(Controller.Button.CROSS)){
            robot.getHorizontalSlide().rotatorContract();
        }else if (controller2.isPressed(Controller.Button.TRIANGLE)){
            robot.getHorizontalSlide().rotatorExtend();
        }else if(controller2.isPressed(Controller.Button.SQUARE)){
            robot.getHorizontalSlide().linkageContract();
        }else if (controller2.isPressed(Controller.Button.CIRCLE)){
            robot.getHorizontalSlide().linkageExtend();
        }

        if (driver.isPressed(Controller.Button.START)){
            robot.getDriveTrain().roadrunner.setPoseEstimate(new Pose2d(0,0,Math.toRadians(90 + robot.getRobotContext().getAlliance().getTranslation())));
        }


        if(driver.isPressed(Controller.Button.LEFT_STICK_BUTTON) || driver.isPressed(Controller.Button.RIGHT_STICK_BUTTON)){
            speed = (speed == 1) ? 0.3 : 1;
        }
        telemetry.addData("left hang", robot.getLittleHanger().getLeftTicks());
        telemetry.addData("right hang", robot.getLittleHanger().getRightTicks());

        robot.update();
    }
}
