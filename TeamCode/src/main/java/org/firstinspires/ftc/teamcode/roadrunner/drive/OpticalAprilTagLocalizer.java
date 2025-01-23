package org.firstinspires.ftc.teamcode.roadrunner.drive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.Localizer;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.components.RobotContext;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class OpticalAprilTagLocalizer extends OpticalLocalizer {

    private Position cameraPosition; //= new Position(DistanceUnit.INCH,-180.467, 160.292, 384.632, 0);
    private YawPitchRollAngles cameraOrientation; // = new YawPitchRollAngles(AngleUnit.DEGREES,0, -90, 0, 0);

    /**
     * The variable to store our instance of the AprilTag processor.
     */
    private AprilTagProcessor aprilTag;

    private double range;

    private boolean newDetection = false;

    /**
     * The variable to store our instance of the vision portal.
     */
    private VisionPortal visionPortal;

    /**
     * Initialize the AprilTag processor.
     */

    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera
    private final WebcamName webcamName;

    private Telemetry telemetry;

    OpticalLocalizer optical;
    Pose2d positionEstimate = new Pose2d(0,0,0);

    public OpticalAprilTagLocalizer(RobotContext context, Position cameraPosition, YawPitchRollAngles cameraOrientation, WebcamName webcamName) {
        super(context);
        this.cameraPosition = cameraPosition;
        this.cameraOrientation = cameraOrientation;
        this.webcamName = webcamName;
        this.telemetry = context.getOpMode().telemetry;

        initAprilTag();
    }

    @Override
    public void update() {
        super.update();

        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        int index = 0;
        if (!currentDetections.isEmpty()){
            try {
                double min = currentDetections.get(0).ftcPose.range;

                for (AprilTagDetection aprilTagDetection : currentDetections){
                    min = Math.min(min,aprilTagDetection.ftcPose.range);
                    index = currentDetections.indexOf(aprilTagDetection);
                }

                range = min;

                AprilTagDetection closestDetection = currentDetections.get(index);

                telemetry.addLine(String.format("xyx %6.1f %6.1f %6.1f  (inch)",
                        closestDetection.robotPose.getPosition().x,
                        closestDetection.robotPose.getPosition().y,
                        closestDetection.robotPose.getOrientation().getYaw(AngleUnit.RADIANS)));

                if(hasGoodDetection()){
                    positionEstimate = new Pose2d(closestDetection.robotPose.getPosition().x, closestDetection.robotPose.getPosition().y, closestDetection.robotPose.getOrientation().getYaw(AngleUnit.RADIANS));
                    this.setPoseEstimate(positionEstimate);
                }
            } catch (Exception e) {
                telemetry.addLine("wierd range thing happened again");
            }
        } else{
            range = -1;
        }
    }
    public List<AprilTagDetection> getDetections(){
        return aprilTag.getDetections();
    }

    public boolean hasGoodDetection(){
        if(range < 48 && range != -1){
            return true;
        }
        return false;
    }

    private void initAprilTag() {

        // Create the AprilTag processor.
        aprilTag = new AprilTagProcessor.Builder()

                // The following default settings are available to un-comment and edit as needed.
                //.setDrawAxes(false)
                //.setDrawCubeProjection(false)
                //.setDrawTagOutline(true)
                //.setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                //.setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .setCameraPose(cameraPosition, cameraOrientation)

                // == CAMERA CALIBRATION ==
                // If you do not manually specify calibration parameters, the SDK will attempt
                // to load a predefined calibration for your camera.
                .setLensIntrinsics(237.835, 237.835, 328.272, 237.727)
                //.setLensIntrinsics(458.511, 458.511, 308.875, 253.078)
                // ... these parameters are fx, fy, cx, cy.

                .build();

        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // eg: Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second (default)
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second (default)
        // Note: Decimation can be changed on-the-fly to adapt during a match.
        //aprilTag.setDecimation(3);

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        if (USE_WEBCAM) {
            builder.setCamera(this.webcamName);
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }

        // Choose a camera resolution. Not all cameras support all resolutions.
        //builder.setCameraResolution(new Size(640, 480));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableLiveView(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(aprilTag);
        builder.setStreamFormat(VisionPortal.StreamFormat.MJPEG);
        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Disable or re-enable the aprilTag processor at any time.
        //visionPortal.setProcessorEnabled(aprilTag, true);

        positionEstimate = new Pose2d(0,0,0);

    }   // end method initAprilTag()
}
