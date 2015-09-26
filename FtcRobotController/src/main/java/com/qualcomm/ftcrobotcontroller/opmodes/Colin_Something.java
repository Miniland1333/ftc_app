package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robocol.Telemetry;

/**
 * Created by Colin on 9/26/2015.
 */
public class Colin_Something extends LinearOpMode {
    Servo Sethe;
    //DcMotor Brandywine;
    //DcMotor Beloved;

    private void Ready4Action() {

        Sethe = hardwareMap.servo.get("one");
        //Brandywine=hardwareMap.dcMotor.get("two");
        //Beloved=hardwareMap.dcMotor.get("three");

    }

    @Override
    public void runOpMode() throws InterruptedException {

        Ready4Action();
        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.x) {
                Sethe.setPosition(.5);
            } else {

                Sethe.setPosition((gamepad1.left_stick_y + 1)/2);
                //Brandywine.setPower((gamepad1.right_stick_y + 1) / 2);
                //Beloved.setPower(((gamepad1.right_stick_x + 1) / 2));

                //Telemetry();
                waitForNextHardwareCycle();
            }
        }
    }
}
