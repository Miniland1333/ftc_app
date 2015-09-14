/* Copyright (c) 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Test Linear TeleOp Code
 * Created by Henry on 8/7/2015.
 */
public class LinearTeleTest extends LinearOpMode {
    //Motor Zone
    DcMotor LD1;
    DcMotor RD1;
    DcMotor LD2;
    DcMotor RD2;
    DcMotor Goal;
    DcMotor Spin;
    DcMotor LL;
    DcMotor RL;
    //Servo Zone
    Servo Front;
    Servo Back;
    Servo IR;
    //Sensor Zone
    IrSeekerSensor irSeeker;
    //variable zone
    private final double AA=0;			//Front Servo Open
    private final double BB=0;			//Front Servo Closed
    private final double CC=.314;			//Back Servo Open
    private final double DD=0;			//Back Servo Closed
    //private final double EE=1;			//IR Up
    private final double FF=.5;			//IR Down
    private final int GG=1000;			//Goal Time Up
    private final int HH=500;			//Goal Time Down
    //private int backOpen=0;
    private int frontOpen=1;
    private int goalUp=1;


    private void initialize(){
        //Sensors
        irSeeker = hardwareMap.irSeekerSensor.get("irSeeker");
        //Motors
        LD1 = hardwareMap.dcMotor.get("LD1");
        RD1 = hardwareMap.dcMotor.get("RD1");
        LD2 = hardwareMap.dcMotor.get("LD2");
        RD2 = hardwareMap.dcMotor.get("RD2");
        Goal = hardwareMap.dcMotor.get("Goal");
        Spin = hardwareMap.dcMotor.get("Spin");
        LL = hardwareMap.dcMotor.get("LL");
        RL = hardwareMap.dcMotor.get("RL");
        //Servos
        Front = hardwareMap.servo.get("Front");
        Back = hardwareMap.servo.get("Back");
        IR = hardwareMap.servo.get("IR");
        //Reverse
        RD1.setDirection(DcMotor.Direction.REVERSE);
        Goal.setDirection(DcMotor.Direction.REVERSE);
        Spin.setDirection(DcMotor.Direction.REVERSE);
        RD2.setDirection(DcMotor.Direction.REVERSE);
        //-------------------------------------------
        LD1.setPower(0);
        LD2.setPower(0);
        RD1.setPower(0);
        RD2.setPower(0);
        Goal.setPower(0);
        Spin.setPower(0);
        LL.setPower(0);
        RL.setPower(0);
        Front.setPosition(BB);//open
        Back.setPosition(DD);//closed
        IR.setPosition(FF);//down
    }

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();

        Front.setPosition(AA);
        frontOpen=1;
        goalReset();

        while(opModeIsActive()){
            LD1.setPower(gamepad1.left_stick_y/2);
            LD1.setPower(gamepad1.left_stick_y/2);
            RD1.setPower(gamepad1.left_stick_y/2);
            RD2.setPower(gamepad1.left_stick_y/2);
            LL.setPower(gamepad1.left_stick_y/2);
            RL.setPower(gamepad1.left_stick_y/2);
            backdoor();
            frontdoor();
            spinner();
            goalie();
            TeleMetry();
            waitOneHardwareCycle();
        }
    }

    /**
     * TeleOp Controls for the Back Servo Motor
     * Uses Gamepad2 Right Trigger
     * Auto-Closes
     */
    private void backdoor(){
        if (gamepad2.right_trigger>.5){
            Back.setPosition(CC);
        }
        else{
            Back.setPosition(DD);
        }
    }

    /**
     *     TeleOp Controls for the Front Servo Motor
     *     Uses Gamepad2 Left Trigger
     */
    private void frontdoor()throws InterruptedException{
        if (gamepad2.left_trigger>.5){
            //noinspection StatementWithEmptyBody
            do {
                //absolutely nothing
                waitOneHardwareCycle();
            }while (gamepad2.left_trigger>.5);
            switch (frontOpen) {
                case 1://close
                    Front.setPosition(BB);
                    frontOpen = 0;
                    break;
                case 0://open
                    Front.setPosition(AA);
                    frontOpen = 1;
                    break;
            }
        }
    }

    /**
     * Controls for the Spinner Motor
     */
    private void spinner(){
        if (gamepad2.right_stick_y>10||gamepad2.right_stick_y<10){
            Spin.setPower(gamepad2.right_stick_y);
        }
        else {
            Spin.setPower(0);
        }
    }
    /**
     * Controls for the Goal Mover Motor
     */
    private void goalie()throws InterruptedException{
        if (gamepad2.a){
            //noinspection StatementWithEmptyBody
            do {
                //absolutely nothing
                waitOneHardwareCycle();
            }while (gamepad2.a);
            switch (goalUp) {
                case 1://up
                    Goal.setPower(-.3);
                    wait(HH);
                    Goal.setPower(0);
                    goalUp = 0;
                    break;
                case 0://down
                    Goal.setPower(.3);
                    wait(GG);
                    Goal.setPower(0);
                    break;
            }
        }
    }
    /**
     * Sets Goal to original position
     */
    private void goalReset()throws InterruptedException{
        Goal.setPower(.3);
        wait(HH);
        Goal.setPower(0);
        goalUp=1;
    }

    /**
     * Adds Telemetry
     */
    private void TeleMetry(){
        String frontMessage;
        String backMessage;
        String goalMessage;
        if (frontOpen==1){
            frontMessage="Front: Open";
        }
        else{
            frontMessage="Front: Closed";
        }
        if (goalUp==1){
            goalMessage="Goal: Up";
        }
        else{
            goalMessage="Goal: Down";
        }
        if (gamepad2.right_trigger>.5){
            backMessage="Bucket: Dumping";
        }
        else{
            backMessage="Bucket: Storing";
        }
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("Front",frontMessage);
        telemetry.addData("Back",backMessage);
        telemetry.addData("Goal",goalMessage);
    }
}
