package frc.robot;

import edu.wpi.first.wpilibj.SpeedControllerGroup; //Groups two speed controllers
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

// import edu.wpi.first.wpilibj.Joystick;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class WinchNLift {
    private SpeedControllerGroup winchMotors; //Creates the object for the winch motors
    private WPI_VictorSPX liftMotor;
    //private Joystick buttonPanel;
    private Gamepad controller;
    private int liftValue;


    public WinchNLift(WPI_VictorSPX winch1, WPI_VictorSPX winch2, WPI_VictorSPX liftMotor, Gamepad controller){
      winchMotors = new SpeedControllerGroup(winch1, winch2);
      this.liftMotor = liftMotor;
      this.controller = controller;
    }

    public void teleOpRun() {
        LiftControls(); //method for lift motor code below

        
        WinchMotor(); //method for winch motor below
      }
    
      private void WinchMotor() {
        //winch motor controls
        if (controller.getB()){
          winchMotors.set(controller.getRT());
        } else {
          winchMotors.set(controller.getRT() * -1);
        }
      }
    
      private void LiftControls() {
        if(controller.getLB()){  //If the LB Button is pressed, then the lift motor speed will be set to 1
            liftValue = 1;
        } else if(controller.getRB()){ //If the RB Button is pressed, then the lift motor speed will be set to -1
            liftValue = -1;
        } else if(!controller.getRB() && !controller.getLB()){  //If neither LB or RB button is pressed, then the lift motor speed will be set to 0
            liftValue = 0;
        }
          liftMotor.set(liftValue); //Sets the lift motor speed to the variable liftMotorValue
      }
    }
