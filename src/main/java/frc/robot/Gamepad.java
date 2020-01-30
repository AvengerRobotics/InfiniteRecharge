package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Gamepad{
    Joystick controller;

    public Gamepad(Joystick controller){
        this.controller = controller;
    }

    public boolean getA(){
        return controller.getRawButton(1);
    }

    public boolean getB(){
        return controller.getRawButton(2);
    }

    public boolean getX(){
        return controller.getRawButton(3);
    }

    public boolean getY(){
        return controller.getRawButton(4);
    }

    public boolean getLB(){
        return controller.getRawButton(5);
    }

    public boolean getRB(){
        return controller.getRawButton(6);
    }

    public double getLT(){
        return -1 * controller.getRawAxis(2);
    }

    public double getRT(){
        return -1 * controller.getRawAxis(3);
    }

    public double getLJoystickX(){
        return -1 * controller.getRawAxis(0);
    }
    public double getLJoystickY(){
        return -1 * controller.getRawAxis(1);
    }

    public double getRJoystickX(){
        return -1 * controller.getRawAxis(4);
    }
    public double getRJoystickY(){
        return -1 * controller.getRawAxis(5);
    }

    public int getDPadAngle(){
        return controller.getPOV();
    }
}