package org.firstinspires.ftc.teamcode.roadrunner.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class OpticalAprilTagLocalizer extends OpticalLocalizer {

    private AprilTagProcessor front, side;

    private double range;

    private AprilTagDetection bestDetection;

    private Telemetry telemetry;

    Pose2d positionEstimate = new Pose2d(0,0,0);

    List<AprilTagDetection> currentDetections;

    public OpticalAprilTagLocalizer(RobotContext context) {
        super(context);
        this.front = context.frontAprilTagProcessor;
        this.side = context.sideAprilTagProcessor;
        this.telemetry = context.getOpMode().telemetry;
    }

    @Override
    public void update() {
        super.update();

        currentDetections.clear();
        currentDetections.addAll(front.getDetections());
        currentDetections.addAll(side.getDetections());

        int index = 0;
        if (!currentDetections.isEmpty()){
            try {
                double min = currentDetections.get(0).ftcPose.range;

                for (AprilTagDetection aprilTagDetection : currentDetections){
                    min = Math.min(min,aprilTagDetection.ftcPose.range);
                    index = currentDetections.indexOf(aprilTagDetection);
                }
                range = min;

                bestDetection = currentDetections.get(index);

                telemetry.addLine(String.format("xyx %6.1f %6.1f %6.1f  (inch)",
                        bestDetection.robotPose.getPosition().x,
                        bestDetection.robotPose.getPosition().y,
                        bestDetection.robotPose.getOrientation().getYaw(AngleUnit.RADIANS)));

            } catch (Exception e) {
                telemetry.addLine("wierd range thing happened again");
            }
        } else{
            range = -1;
        }

        if(hasGoodDetection()){
            positionEstimate = new Pose2d(bestDetection.robotPose.getPosition().x, bestDetection.robotPose.getPosition().y, bestDetection.robotPose.getOrientation().getYaw(AngleUnit.RADIANS));
            this.setPoseEstimate(positionEstimate);
        }

    }

    public boolean hasGoodDetection(){
        if(range < 48 && range != -1){
            return true;
        }
        return false;
    }

}
