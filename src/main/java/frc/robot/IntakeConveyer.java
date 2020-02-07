package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX; //motors

import edu.wpi.first.wpilibj.DigitalInput; //Sensors
import edu.wpi.first.wpilibj.Joystick; //controller
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard; //smartDashboard display

public class IntakeConveyer{
    private WPI_VictorSPX conveyerMotor; //creates the conveyer motor to move the balls
    private WPI_VictorSPX intakeMotor; //intake to bring the balls into the robot
    private DigitalInput prox1, prox2, prox3; //creates the proximity switches for conveyer and intake
    private Joystick buttonPanel; //breates the button panel
    private boolean isConveyerEnabled = true; //boolean to test if the conveyer is enabled 
    private boolean isDumping = false; //boolean to test if the conveter is enable ed to dump the balls

    public IntakeConveyer(WPI_VictorSPX conveyer, WPI_VictorSPX intake, Joystick buttonPanel, DigitalInput prox1, DigitalInput prox2, DigitalInput prox3){
        conveyerMotor = conveyer; //sets conveyerMoor to conveyer to use in code
        intakeMotor = intake; //sets intakeMotor to intake to usein code
        this.buttonPanel = buttonPanel; 
        this.prox1 = prox1;
        this.prox2 = prox2;
        this.prox3 = prox3;
    }

    public void teleOpRun() {
    //if switch one is triggered it will turn on the conveyer
    if (!prox1.get() && isConveyerEnabled){
      conveyerMotor.set(.5);
    }
    // if switch 2 is triggered, it will turn off the conveyer
    if (!prox2.get()){
      conveyerMotor.set(0);
    }
    // if switch 3 is triggered, it will turn off the intake and make the conveyer not trigger using switch 1
    if (!prox3.get() && !isDumping){
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


    if (isDumping = true && prox1.get() && prox2.get() && prox3.get()){
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
    if (isDumping = true && prox1.get() && prox2.get() && prox3.get()){
      isDumping = false;
      conveyerMotor.set(0);
      intakeMotor.set(1);
    }
  }
  public boolean isDumping(){
    return isDumping;
  }
}