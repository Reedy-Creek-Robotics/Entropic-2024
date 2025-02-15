package org.firstinspires.ftc.teamcode.components;

import android.annotation.SuppressLint;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.roadrunner.util.LynxModuleUtil;
import org.firstinspires.ftc.teamcode.util.ErrorUtil;
import org.firstinspires.ftc.teamcode.util.FileUtil;
import org.firstinspires.ftc.teamcode.util.TelemetryHolder;

import java.util.List;

public class Robot extends BaseComponent{
    private static final double VOLTAGE_WARNING_THRESHOLD = 12.0;

    private List<LynxModule> lynxModules;

    private DriveTrain driveTrain;
    private HorizontalSlide horizontalSlide;
    private LittleHanger littleHanger;
    private ScoringSlide scoreSlide;
    private Intake intake;
    protected MachineVisionSubmersible mvs;
    private ParkingStick parkingStick;

    private int updateCount;
    private ElapsedTime initTime;
    private ElapsedTime firstUpdateTime;

    public Robot(OpMode opMode) {
        super(createRobotContext(opMode));

        this.lynxModules = hardwareMap.getAll(LynxModule.class);

        driveTrain = new DriveTrain(context);
        horizontalSlide = new HorizontalSlide(context);
        littleHanger = new LittleHanger(context);
        scoreSlide = new ScoringSlide(context);
        intake = new Intake(context);
        mvs = new MachineVisionSubmersible(context);
        parkingStick = new ParkingStick(context);

        addSubComponents(driveTrain, horizontalSlide,littleHanger,scoreSlide, intake, mvs, parkingStick);

        TelemetryHolder.telemetry = telemetry;
    }

    public RobotContext getRobotContext() {
        return context;
    }

    @Override
    public void init() {
        super.init();

        double voltage = computeBatteryVoltage();
        if (voltage < VOLTAGE_WARNING_THRESHOLD) {
            telemetry.log().add("LOW BATTERY WARNING");
            telemetry.log().add("\"My battery is low and it's getting dark\" -Opportunity");
            telemetry.log().add("\"ding fries are done ding fries are done\" - Peter Griffin");
        }

        // Set the caching mode for reading values from Lynx components to manual. This means that when reading values
        // like motor positions, the code will grab all values at once instead of one at a time. It will also keep
        // these values and not update them until a manual call is made to clear the cache. We do this once per loop
        // in the Robot's update method.
        LynxModuleUtil.ensureMinimumFirmwareVersion(hardwareMap);
        for (LynxModule module : lynxModules) {
            //module.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        telemetry.log().add("Robot is initialized");
        telemetry.update();

        initTime = new ElapsedTime();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void update() {
        if (updateCount == 0) {
            firstUpdateTime = new ElapsedTime();
            onStart();
        }

        // Compute and print the updates per second
        computeUpdatesPerSecond();

        // Clear the bulk cache so that new values will be read for each component
        for (LynxModule lynxModule : lynxModules) {
            lynxModule.clearBulkCache();
        }

        // Allow all the subcomponents to do their work.
        super.update();

        // Update telemetry once per iteration after all components have been called.
        telemetry.addData("position",getDriveTrain().getRoadRunner().getPoseEstimate());
        telemetry.update();

    }

    public void savePositionToDisk() {
        savePositionToDisk("robot-position");
    }

    public void savePositionToDisk(String filename) {
        FileUtil.writeLines(
                filename,
                driveTrain.roadrunner.getPoseEstimate().getX(),
                driveTrain.roadrunner.getPoseEstimate().getY(),
                driveTrain.roadrunner.getPoseEstimate().getHeading(),
                littleHanger.getPosition()
        );
    }

    public void loadPositionFromDisk() {
        loadPositionFromDisk("robot-position");
    }

    public void loadPositionFromDisk(String filename) {
        List<String> lines = FileUtil.readLines(filename);
        if (!lines.isEmpty()) {
            try {
                if (lines.size() != 4) {
                    throw new IllegalArgumentException("Expected 4 lines but found [" + lines.size() + "]");
                }

                Pose2d pose2d = new Pose2d(
                        Double.parseDouble(lines.get(0)),
                        Double.parseDouble(lines.get(1)),
                        Double.parseDouble(lines.get(2))
                );

                driveTrain.roadrunner.getLocalizer().setPoseEstimate(pose2d);
                context.localizer.setPoseEstimate(pose2d);

                littleHanger.setInitialPosition(Integer.parseInt(lines.get(3)));
            } catch (Exception e) {
                telemetry.log().add("Error loading position: " + ErrorUtil.convertToString(e));
            }

            // Now that the position has been consumed, remove the file
            FileUtil.removeFile(filename);
        }
    }

    public void saveTimingRecord(String filename){
        FileUtil.writeLines(filename,context.record);
    }

    public String loadTimingRecord(String filename){
        String out = "";
        for (String string: FileUtil.readLines(filename)) {
            out += string + "\n";
        }

        return out;
    }

    public void resetFiles(String filename){
        FileUtil.removeFile(filename);
    }

    @SuppressLint("DefaultLocale")
    private void computeUpdatesPerSecond() {
        updateCount++;

        double updatesPerSecond = updateCount / firstUpdateTime.seconds();
        telemetry.addData("Updates / sec", String.format("%.1f", updatesPerSecond));
    }

    public void onStart() {
        // todo: Commenting this out for now since we no longer need to worry about moving the turrent from a diagonal
        // todo: start position to the front.  We may need to revisit this since the turret won't "lock" in place
        // todo: until the slide moves above a specific height.
        //slide.moveToHeight(TRAVEL);

        telemetry.log().clear();
    }

    public static RobotContext createRobotContext(OpMode opMode) {
        return new RobotContext(
                opMode,
                new RobotDescriptor()
        );
    }

    public void waitForCommandsToFinish() {
        waitForCommandsToFinish(Double.MAX_VALUE);
    }

    public void waitForCommandsToFinish(double maxTime) {
        // While the components are busy trying to execute a command, keep looping and giving
        // each of them a chance to update.
        ElapsedTime time = new ElapsedTime();
        while (!isStopRequested() && isBusy() && time.seconds() < maxTime) {
            update();
        }

    }

    public DriveTrain getDriveTrain() {
        return driveTrain;
    }

    public HorizontalSlide getHorizontalSlide() {
        return horizontalSlide;
    }

    public Intake getIntake(){return intake;}

    public LittleHanger getLittleHanger() {
        return littleHanger;
    }

    public ScoringSlide getScoringSlide() {
        return scoreSlide;
    };

    public MachineVisionSubmersible getMvs() {
        return mvs;
    }

    public ParkingStick getParkingStick() {
        return parkingStick;
    }

    private double computeBatteryVoltage() {
        double result = Double.POSITIVE_INFINITY;
        for (VoltageSensor sensor : hardwareMap.voltageSensor) {
            double voltage = sensor.getVoltage();
            if (voltage > 0) {
                result = Math.min(result, voltage);
            }
        }
        return result;
    }
}
