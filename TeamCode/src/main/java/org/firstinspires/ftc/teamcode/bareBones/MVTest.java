package org.firstinspires.ftc.teamcode.bareBones;

import com.arcrobotics.ftclib.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.BaseComponent;
import org.firstinspires.ftc.teamcode.components.MachineVisionSubmersible;
import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.teamcode.game.Controller;

import java.util.List;

@TeleOp
public class MVTest extends OpMode {

    protected Controller driver;
    RobotContext robotContext;
    MachineVisionSubmersible mvs;

    List<List<Integer>> counts;

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

            counts = mvs.getElementCounts();
            telemetry.addData("region0 team: ", counts.get(0).get(0));
            telemetry.addData("region1 team: ", counts.get(0).get(1));
            telemetry.addData("region2 team: ", counts.get(0).get(2));
            telemetry.addData("region0 yellow: ", counts.get(1).get(0));
            telemetry.addData("region1 yellow: ", counts.get(1).get(1));
            telemetry.addData("region2 yellow: ", counts.get(1).get(2));
        }



        telemetry.update();

    }
}
