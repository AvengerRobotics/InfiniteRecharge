package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class IntakeSolenoid{
    private Gamepad controller;
    private DoubleSolenoid intakeSolenoid;
    
    public IntakeSolenoid(DoubleSolenoid solenoid, Gamepad gamepad){
      intakeSolenoid = solenoid;
      controller = gamepad;
    }

    public void teleOpRun() {
        //solenoid controls
        if(controller.getDPadAngle() > 135 && controller.getDPadAngle() < 225){ //dPad down
          intakeSolenoid.set(DoubleSolenoid.Value.kReverse);
        //If the up of the dpad is pressed, then the conveyor and intake motors speed will be set to -1
        } else if((controller.getDPadAngle() > 315 || controller.getDPadAngle() < 45) && controller.getDPadAngle() != -1){
          intakeSolenoid.set(DoubleSolenoid.Value.kForward);
        //If neither A or Y button is pressed, then the conveyor and intake motors speed will be set to 0
        } else{
          intakeSolenoid.set(DoubleSolenoid.Value.kOff);
        }
    }
}






