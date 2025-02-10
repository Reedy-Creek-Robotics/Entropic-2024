package org.firstinspires.ftc.teamcode.bareBones;

import com.arcrobotics.ftclib.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.BaseComponent;
import org.firstinspires.ftc.teamcode.components.MachineVisionSubmersible;
import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.teamcode.game.Controller;

@TeleOp
public class MVTest extends OpMode {

    protected Controller driver;
    RobotContext robotContext;
    MachineVisionSubmersible mvs;

    Pose2d lastElemPos;

    @Override
    public void init() {
        driver = new Controller(gamepad1);
        robotContext = BaseComponent.createRobotContext(this);
        mvs = new MachineVisionSubmersible(robotContext);

        mvs.init();
    }

    @Override
    public void loop() {

        if(driver.isPressed(Controller.Button.CROSS)){

            lastElemPos = mvs.runPipeline();
            telemetry.addData("last element position: ", lastElemPos);
        }



        telemetry.update();

    }
}
