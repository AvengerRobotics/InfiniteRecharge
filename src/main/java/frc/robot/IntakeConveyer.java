package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IntakeConveyer{
    private WPI_VictorSPX conveyerMotor;
    private WPI_VictorSPX intakeMotor;
    private DigitalInput prox1, prox2, prox3; //creates the proximity switches for conveyer and intake
    private Joystick buttonPanel;
    private boolean isConveyerEnabled = true;
    private boolean isDumping = false;

    public IntakeConveyer(WPI_VictorSPX conveyer, WPI_VictorSPX intake, Joystick buttonPanel, DigitalInput prox1, DigitalInput prox2, DigitalInput prox3){
        conveyerMotor = conveyer;
        intakeMotor = intake;
        this.buttonPanel = buttonPanel;
        this.prox1 = prox1;
        this.prox2 = prox2;
        this.prox3 = prox3;
    }

    public void teleOpRun() {
/*
    // //if prox1 and prox 2 are broken, then the conveyer motor will stop
    // if(!prox1.get() && !prox2.get()){  
    //   conveyerMotor.set(0);
    //   //if prox1 and prox 2 are true, then the conveyer motor power will be set to 0.5
    // } else {
    //   conveyerMotor.set(0.5);
    // }
    // //if prox2 and prox3 are broken, then the intake motor will stop
    // if(!prox2.get() && !prox3.get()){  
    //   intakeMotor.set(0);
    //   //if prox2 and prox3 are true, then the intake motor power will be set to 0.5
    // } else {
    //   intakeMotor.set(0.5);  
    // }
    // //If button 1 on the control panel is pressed, then the conveyer and intake motor power is set to 0.5
    // if(buttonPanel.getRawButton(1)){  
    //   conveyerMotor.set(0.5);
    //   intakeMotor.set(0.5);
    // } 
    // //If button 2 on the control panel is pressed, then the conveyer and intake motor power is set to -0.5
    // if(buttonPanel.getRawButton(2)){  
    //   conveyerMotor.set(-0.5);
    //   intakeMotor.set(-0.5);
    // }
*/
    //if switch one is triggered it will turn on the conveyer
    if (prox1.get() && isConveyerEnabled){
      conveyerMotor.set(.5);
    }
    // if switch 2 is triggered, it will turn off the conveyer
    if (prox2.get()){
      conveyerMotor.set(0);
    }
    // if switch 3 is triggered, it will turn off the intake and make the conveyer not trigger using switch 1
    if (prox3.get() && !isDumping){
      isConveyerEnabled = false;
      intakeMotor.set(0);
    }
    // if btn 11 is pressed, it will turn the conveyer back on
    if (buttonPanel.getRawButton(11)){
      isConveyerEnabled = true;
    }
    // if btn 12 is pressed, the conveyer sets to high power and dumps out the balls and makes sure switch 3 won't turn it off
    if (buttonPanel.getRawButton(12)){
      conveyerMotor.set(1);
      isDumping = true;
    }
    if (isDumping = true && !prox1.get() && !prox2.get() && !prox3.get()){
      isDumping = false;
      conveyerMotor.set(0);
      intakeMotor.set(1);
    }

    SmartDashboard.putBoolean("Proximity Switch 1", prox1.get());
    SmartDashboard.putBoolean("Proximity Switch 2", prox2.get());
    SmartDashboard.putBoolean("Proximity Switch 3", prox3.get());   
        
  }

  public void dump(){
    if (!isDumping){
      conveyerMotor.set(1);
      isDumping = true;
    }
    if (isDumping = true && !prox1.get() && !prox2.get() && !prox3.get()){
      isDumping = false;
      conveyerMotor.set(0);
      intakeMotor.set(1);
    }
  }
  public boolean isDumping(){
    return isDumping;
  }
}