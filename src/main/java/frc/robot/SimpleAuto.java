package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class SimpleAuto{
    private Timer timer;
    private DifferentialDrive driveTrain;

    public SimpleAuto(Timer timer, DifferentialDrive driveTrain){
        this.timer = timer;
        this.driveTrain = driveTrain;
    }

    public void run(){
        if (timer.get() < 300){
            driveTrain.tankDrive(0.6,0.6);
            } else { 
            driveTrain.stopMotor();
        }
    }
}