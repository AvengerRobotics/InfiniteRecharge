package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class AdvancedAuto1{
    private DifferentialDrive driveTrain;
    private Timer timer;
    private IntakeConveyer intakeConveyer;
    
    public AdvancedAuto1(DifferentialDrive driveTrain, Timer timer, IntakeConveyer intakeConveyer){
        this.driveTrain = driveTrain;
        this.timer = timer;
        this.intakeConveyer = intakeConveyer;
    }

    public void run(){
        // if (timer.get() < 2){
        //     driveTrain.tankDrive(-.5, -.5);
        // }
        // if (timer.get() > 2 && timer.get() < 4){
        //     driveTrain.tankDrive(0, 0);
        //     intakeConveyer.dump();
        // }
        // if (timer.get() > 3 && !intakeConveyer.isDumping()){
        //     driveTrain.tankDrive(.5, .5);
        // }
        // if (timer.get() > 7){
        //     driveTrain.tankDrive(0, 0);
        }
    }
