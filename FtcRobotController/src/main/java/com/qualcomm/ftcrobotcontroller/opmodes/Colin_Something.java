package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Test New Technology Servos
 * Created by Colin on 9/26/2015.
 */
public class Colin_Something extends LinearOpMode {
    //Servo Sethe;
    DcMotor Right;
    DcMotor Left;

    private void Ready4Action() {

        //Sethe = hardwareMap.servo.get("one");
        Right=hardwareMap.dcMotor.get("Right");
        Left=hardwareMap.dcMotor.get("Left");

    }

    @Override
    public void runOpMode() throws InterruptedException {

        Ready4Action();
        waitForStart();

        wait(2000);

        Left.setPower(.5);
        Right.setPower(.5);
        wait(2000);
        Left.setPower(0);
        Right.setPower(0);







        //while (opModeIsActive()) {

        //if (gamepad1.x) {
        //  Sethe.setPosition(1);
        //} else {

        //Sethe.setPosition((gamepad1.left_stick_y + 1)/2);
        //Brandywine.setPower((gamepad1.right_stick_y + 1) / 2);
        //Beloved.setPower(((gamepad1.right_stick_x + 1) / 2));

        //Telemetry();
        //}
        waitForNextHardwareCycle();
    }
}

