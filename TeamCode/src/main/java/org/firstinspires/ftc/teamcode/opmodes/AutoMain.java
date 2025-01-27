package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.components.ScoringSlide;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

public abstract class AutoMain extends LinearOpMode {
    public enum Alliance{
        RED(0,1),
        BLUE(180,-1);

        double rotation;
        int translation;

        Alliance(double rotation, int translation) {
            this.rotation = rotation;
            this.translation = translation;
        }

        public double getRotation() {
            return rotation;
        }

        public int getTranslation() {
            return translation;
        }
    }

    protected Robot robot;

    protected Pose2d currentEnd;

    @Override
    public void runOpMode() throws InterruptedException {
        initRobot();

        telemetry.log().add("Wait for start", "");

        waitForStart();

        runPath();

        robot.savePositionToDisk();

    }

    public void initRobot(){
        robot = new Robot(this);
        robot.init();
        robot.getDriveTrain().getRoadRunner().setPoseEstimate(getStartPosition());
        currentEnd = getStartPosition();
    }

    public abstract void runPath();

    public abstract Pose2d getStartPosition();

    public abstract Alliance getAlliance();

    public abstract void deliverPreload();
    public abstract void park();

}
