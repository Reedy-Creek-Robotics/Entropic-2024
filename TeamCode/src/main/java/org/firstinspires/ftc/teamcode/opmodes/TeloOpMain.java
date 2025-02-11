package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.components.ScoringSlide.Positions.*;
import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.LEFT_STICK_X;
import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.LEFT_STICK_Y;
import static org.firstinspires.ftc.teamcode.game.Controller.AnalogControl.RIGHT_STICK_X;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.LittleHanger;
import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.ScoringSlide;
import org.firstinspires.ftc.teamcode.game.Controller;

@TeleOp(name = "_TeleOpMain")
public class TeloOpMain extends OpMode {

    Robot robot;

    double speed = 1;

    ElapsedTime timer = new ElapsedTime();
    Boolean pulling = false;
    double linkagePos = 0;

    protected Controller driver;

    public Controller controller2;

    public ScoringSlide.Positions[] slideUpPositions = {GROUND, HIGH_BASKET};
    public ScoringSlide.Positions[] slideDownPositions = {GROUND, HIGH_BAR, OVER_HIGH_BAR};
    public int slideUpPosIndex = 0;
    public int slideDownPosIndex = 0;

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
    public void start() {
        super.start();

        robot.getLittleHanger().moveToTicks(- robot.getLittleHanger().getInitialPosition());
        robot.getLittleHanger().resetSlideTicks();
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
        if (driver.isButtonDown(Controller.Button.TRIANGLE)){
            pulling = true;
            robot.getLittleHanger().rotate(1);
        } else if (driver.isPressed(Controller.Button.CROSS)) {
            robot.getLittleHanger().moveToHeight(LittleHanger.HangHeights.PULL);
        } else if (pulling){
            robot.getLittleHanger().rotate(0);
            pulling = false;
        }

        if (driver.isButtonDown(Controller.Button.DPAD_RIGHT)){
            if(timer.milliseconds()>=1 && linkagePos<1){
                timer.reset();
                linkagePos += 0.2;
            }
        } else if (driver.isButtonDown(Controller.Button.DPAD_LEFT)) {
            if(timer.milliseconds()>=1 && linkagePos>0){
                timer.reset();
                linkagePos -= 0.2;
            }
        }

        robot.getHorizontalSlide().linkageMove(linkagePos);

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

        if (driver.isPressed(Controller.Button.DPAD_UP)) {
            slideUpPosIndex = ((slideUpPosIndex + 1) <= (slideUpPositions.length - 1)) ? slideUpPosIndex + 1 : 0;
            robot.getScoringSlide().moveToHeight(slideUpPositions[slideUpPosIndex]);
        }else if(driver.isPressed(Controller.Button.DPAD_DOWN)) {
            slideDownPosIndex = ((slideDownPosIndex - 1) >= 0) ? slideDownPosIndex - 1 : slideDownPositions.length - 1;
            robot.getScoringSlide().moveToHeight(slideDownPositions[slideDownPosIndex]);
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
            speed = (speed == 1) ? 0.55 : 1;
        }
        telemetry.addData("left hang", robot.getLittleHanger().getLeftTicks());
        telemetry.addData("right hang", robot.getLittleHanger().getRightTicks());
        telemetry.addData("linkage", linkagePos);
        telemetry.addData("UP",slideUpPosIndex);
        telemetry.addData("DOWN",slideDownPosIndex);


        robot.update();
    }
}
