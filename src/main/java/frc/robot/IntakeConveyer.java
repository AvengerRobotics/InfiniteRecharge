package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX; //motors

import edu.wpi.first.wpilibj.DigitalInput; //Sensors
import edu.wpi.first.wpilibj.Joystick; //controller
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard; //smartDashboard display
import edu.wpi.first.wpilibj.DoubleSolenoid; //Solenoid controls

public class IntakeConveyer{
  private WPI_VictorSPX conveyerMotor; //creates the conveyer motor to move the balls
  private WPI_VictorSPX intakeMotor; //intake to bring the balls into the robot
  private DigitalInput prox1, prox2, prox3; //creates the proximity switches for conveyer and intake
  private Joystick buttonPanel; //creates the button panel
  private Gamepad controller; // creates the controller
  private DoubleSolenoid intakeSolenoid; //creates the double solenoid
  private boolean isConveyerEnabled = true; //boolean to test if the conveyer is enabled 
  private boolean isIntakeEnabled = true;
  private boolean isDumping = false; //boolean to test if the conveter is enable ed to dump the balls
  private boolean isAutoRunning = false;
  private boolean isHeld = false;

  public IntakeConveyer(WPI_VictorSPX conveyer, WPI_VictorSPX intake, Joystick buttonPanel, DigitalInput prox1, DigitalInput prox2, DigitalInput prox3, DoubleSolenoid solenoid, Gamepad controller){
      conveyerMotor = conveyer; //sets conveyerMoor to conveyer to use in code
      intakeMotor = intake; //sets intakeMotor to intake to usein code
      this.buttonPanel = buttonPanel; 
      this.prox1 = prox1;
      this.prox2 = prox2;
      this.prox3 = prox3;
      intakeSolenoid = solenoid;
      this.controller = controller;
  }

  public void extend(){
    intakeSolenoid.set(DoubleSolenoid.Value.kForward);
  }
  public void retract(){
    intakeSolenoid.set(DoubleSolenoid.Value.kReverse);
  }
  public void stop(){
    intakeSolenoid.set(DoubleSolenoid.Value.kOff);
  }


  public void teleOpRun() {
  if(controller.getDPadAngle() > 135 && controller.getDPadAngle() < 225 && isHeld == false){ //dPad down
    extend(); 
    isHeld = true;
    //if only 2 is triggered, the conveyer motor will stop
    if (prox1.get() == true && prox2.get() == false && prox3.get() == true){
      conveyerMotor.set(0);
      intakeMotor.set(1);
    }

    //if the bottom proximity switch is triggered OR the both lower switches are triggered, the intake motor will run
    else if (prox3.get() == false || (prox2.get() == false && prox3.get() == false)){
      conveyerMotor.set(0.5);
      intakeMotor.set(1); 
    }

    //if none of teh senors are triggered (meaning that there were no balls in the system), the intake motor will be on
    else if (prox1.get() == true && prox2.get() == true && prox3.get() == true){
      conveyerMotor.set(0);
      intakeMotor.set(1); 
    }

    //if all of the proximity switches are triggered OR just the top switch is triggered, then the conveyer motor and the intake motor stop.
    else if ((prox1.get() == false && prox2.get() == false && prox3.get() == false) || (prox1.get() == false)){
      conveyerMotor.set(0);
      intakeMotor.set(0); 
    }
    // the default  
    else {
      conveyerMotor.set(0);
      intakeMotor.set(0); 
    }

    // when button 11 on the control panel is pressed, the conveyer motor runs at half power.
    if (buttonPanel.getRawButton(11)){
      conveyerMotor.set(0.5);
      intakeMotor.set(0); 
    }
    // when button 12 on the control panel is pressed, the conveyer will run at half power in the other direction and the intake motor will be at full power in the other direction. 
    if (buttonPanel.getRawButton(12)){
      conveyerMotor.set(-0.5);
      intakeMotor.set(-1); 
    }
    //Displays the state of each sensor  
    SmartDashboard.putBoolean("Proximity Switch 1", prox1.get());
    SmartDashboard.putBoolean("Proximity Switch 2", prox2.get());
    SmartDashboard.putBoolean("Proximity Switch 3", prox3.get());   
  }

  else if((controller.getDPadAngle() > 315 || controller.getDPadAngle() < 45) && controller.getDPadAngle() != -1 && isHeld == true){
    retract();
    conveyerMotor.set(0);
    intakeMotor.set(0);
    isHeld = false;
  //If neither A or Y button is pressed, then the conveyor and intake motors speed will be set to 0
  } else{
    stop();
    conveyerMotor.set(0);
    intakeMotor.set(0);
    isHeld = false;
  }

//   public void dump(){
//     if (!isDumping){
//       conveyerMotor.set(1);
//       isDumping = true;
//     }
//     if (isDumping = true && prox1.get() && prox2.get() && prox3.get()){
//       isDumping = false;
//       conveyerMotor.set(0);
//       intakeMotor.set(1);
//     }
//   }
//   public boolean isDumping(){
//     return isDumping;
//   }
 }
}



  /*  // if (buttonPanel.getRawButton(10)){
    //   conveyerMotor.set(.5);
    //   isHeld = true;
    // } else if (buttonPanel.getRawButton(9)){
    //   conveyerMotor.set(-1);
    //   isHeld = true;
    // } else if (!isAutoRunning) {
    //   conveyerMotor.set(0);
    //   isHeld = false;
    // }

    // if (isIntakeEnabled){
    //   intakeMotor.set(1);0
    // }
    // //if switch one is triggered it will turn on the conveyer
    // if (!prox1.get() && isConveyerEnabled && !isHeld){
    //   conveyerMotor.set(.75);
    //   isAutoRunning = true;
    // }
    // // if switch 2 is triggered, it will turn off the conveyer
    // if (!prox2.get() && prox1.get() && !isHeld){
    //   conveyerMotor.set(0);
    //   isAutoRunning = false;
    // }
    // // if switch 3 is triggered, it will turn off the intake and make the conveyer not trigger using switch 1
    // if (!prox3.get() && !isDumping && !isHeld){
    //   isConveyerEnabled = false;
    //   intakeMotor.set(0);
    // }
    // // if btn 11 is pressed, it will turn the conveyer back on
    // if (buttonPanel.getRawButton(11) && !isHeld){
    //   isConveyerEnabled = true;
    //   isIntakeEnabled = true;
    // }
    // // if btn 12 is pressed, the conveyer sets to high power and dumps out the balls and makes sure switch 3 won't turn it off
    // if (buttonPanel.getRawButton(12) && !isHeld){
    //   conveyerMotor.set(1);
    //   isDumping = true;
    // }

    // if (isDumping = true && prox1.get() && prox2.get() && prox3.get()){
    //   isDumping = false;
    //   conveyerMotor.set(0);
    //   intakeMotor.set(1);
    // }
 */