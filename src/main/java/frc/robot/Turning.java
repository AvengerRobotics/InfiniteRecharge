/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Turning{
    private DifferentialDrive driveTrain;
    private Timer timer;
    private IntakeConveyer intakeConveyer;
    private double startTime;
    private boolean isFirstRun = true;
    
    public Turning(DifferentialDrive driveTrain, Timer timer, IntakeConveyer intakeConveyer){
        this.driveTrain = driveTrain;
        this.timer = timer;
        this.intakeConveyer = intakeConveyer;
    }

    public void turn90(){
        if (isFirstRun){
            startTime = timer.get();
            isFirstRun = false;
        }
        if (timer.get() < 2){
            driveTrain.tankDrive(-.5, .5);
        }
        if (timer.get() > 5){
            driveTrain.tankDrive(0, 0);
            isFirstRun = true;
        }
    } 
}
