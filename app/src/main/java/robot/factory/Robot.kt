package robot.factory

import robot.exceptions.CreateRobotPartException
import robot.interfaces.IRobot
import robot.interfaces.IRobotPart
import robot.parts.RobotPart
import robot.util.RobotConstructionUtil

class Robot(
    private val head: IRobotPart, private val hand: IRobotPart, private val leg: IRobotPart
) : IRobot {

    private val robotConstructionUtil = RobotConstructionUtil()

    init {
        robotConstructionUtil.validateOfTheRobotPart(head)
        robotConstructionUtil.validateOfTheRobotPart(hand)
        robotConstructionUtil.validateOfTheRobotPart(leg)
    }

    override fun getPrice(): Int = head.getPrice() + hand.getPrice() + leg.getPrice()

    override fun action() {
        println("The Robot { ${head.info()} ${hand.info()} ${leg.info()} \n}")
    }

    override fun toString(): String {
        return "Robot{head: ${head.getBrandName()}, hand: ${hand.getBrandName()}, leg: ${leg.getBrandName()}}"
    }
}


