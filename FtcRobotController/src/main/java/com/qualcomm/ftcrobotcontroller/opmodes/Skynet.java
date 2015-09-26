package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Henry on 9/26/2015.
 */
public class Skynet extends LinearOpMode {
Servo Ed;
    Servo Bob;
    Servo Ted;
    DcMotor Betty;
    private void Doctor(){
        Ed=hardwareMap.servo.get("Ed");
      //  Bob=hardwareMap.servo.get("Bob");
       // Ted=hardwareMap.servo.get("Ted");
       // Betty=hardwareMap.dcMotor.get("Betty");
        //Sets things up
    }
    @Override
    public void runOpMode() throws InterruptedException {
        Doctor();
        waitForStart();

        while (opModeIsActive()) {
            //Twelve.setPosition(((gamepad1.left_stick_y+1)/2));
           /* Left.setPower(gamepad1.left_stick_y);
            Right.setPower(-gamepad1.right_stick_y);*/

            Ed.setPosition((gamepad1.left_stick_y+1)/2);
          //  Bob.setPosition((gamepad1.left_stick_y+1)/2);
           // Ted.setPosition((gamepad1.left_stick_y+1)/2);
            //Betty.setPosition((gamepad1.right_stick_y+1)/2);
            //five.setPosition((gamepad1.right_stick_y+1)/2);
           // six.setPosition((gamepad1.right_stick_y+1)/2);

            Telemetry();
            waitForNextHardwareCycle();
        }
    }
    public void Telemetry(){
        telemetry.addData("Value", gamepad1.left_stick_y);
        telemetry.addData("Value2", gamepad1.right_stick_y);
        //This Does Something.
    }

}