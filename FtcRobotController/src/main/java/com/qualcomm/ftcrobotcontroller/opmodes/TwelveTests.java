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
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import java.lang.Math;

/**
 * A Test Case
 * Created by Staff on 9/13/2015.
 */
public class TwelveTests extends LinearOpMode {
    //Servo Twelve;
    DcMotor LFront;
    DcMotor LBack;
    DcMotor RFront;
    DcMotor RBack;
    DcMotor Lift;
    DcMotor Bucket;//Bucket Encoder is on LBack

    Servo one;
    Servo two;
/*    Servo three;
    Servo four;
    Servo five;
    Servo six;*/



    private void initialize(){
        //Twelve = hardwareMap.servo.get("Twelve");
        LFront = hardwareMap.dcMotor.get("LFront");
        LBack = hardwareMap.dcMotor.get("LBack");
        RFront = hardwareMap.dcMotor.get("RFront");
        RBack = hardwareMap.dcMotor.get("RBack");
        Lift = hardwareMap.dcMotor.get("Lift");
        Bucket = hardwareMap.dcMotor.get("Bucket");

        LFront.setDirection(DcMotor.Direction.REVERSE); //Reversing based on default motor rotation direction
        LBack.setDirection(DcMotor.Direction.REVERSE);  //Reversing based on default motor rotation direction
        Lift.setDirection(DcMotor.Direction.REVERSE);   //Reversing based on default motor rotation direction


/*        one=hardwareMap.servo.get("one");
        two=hardwareMap.servo.get("two");
        three=hardwareMap.servo.get("three");
        four=hardwareMap.servo.get("four");
        five=hardwareMap.servo.get("five");
        six=hardwareMap.servo.get("six");*/

    }

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();

        while (opModeIsActive()) {
            //Twelve.setPosition(((gamepad1.left_stick_y+1)/2));
            LFront.setPower(gamepad1.left_stick_y);
            LBack.setPower(gamepad1.left_stick_y);
            RFront.setPower(gamepad1.right_stick_y);
            RBack.setPower(gamepad1.right_stick_y);
            Lift.setPower(gamepad2.left_stick_y);

/*            one.setPosition((gamepad1.left_stick_y+1)/2);
            two.setPosition((gamepad1.left_stick_y+1)/2);
            three.setPosition((gamepad1.left_stick_y+1)/2);
            four.setPosition((gamepad1.right_stick_y+1)/2);
            five.setPosition((gamepad1.right_stick_y+1)/2);
            six.setPosition((gamepad1.right_stick_y+1)/2);*/


            TiltMotor();




            Telemetry();
            waitForNextHardwareCycle();
        }
        waitOneFullHardwareCycle();
    }

    public void TiltMotor(){
        final int MAX_DEGREES = 25;
        final int TOLERANCE_DEGREES = 5;
        final int SLOW_RANGE = 15;

        double motorValue = gamepad2.right_stick_x*MAX_DEGREES;
        double currentValue= LBack.getCurrentPosition()/4f; //Bucket Encoder is on LBack
        double difference = Math.abs(motorValue-currentValue);
        if (difference<=TOLERANCE_DEGREES){Bucket.setPower(0);
        }
        else if (motorValue>currentValue){
            if (difference<=SLOW_RANGE) {Bucket.setPower(-.1);}
            else {Bucket.setPower(-.2);}
        }
        else{
            if (difference<=SLOW_RANGE){Bucket.setPower(.1);}
            else {Bucket.setPower(.2);}
        }
    }

    public void Telemetry(){
        telemetry.addData("Left",gamepad1.left_stick_y);
        telemetry.addData("Right",gamepad1.right_stick_y);
        telemetry.addData("Lift",gamepad2.left_stick_y);
        telemetry.addData("Bucket",LBack.getCurrentPosition()/4);
        telemetry.addData("Bucket joystick", gamepad2.right_stick_x);
    }
}
