package robot.util

import robot.exceptions.CreateRobotPartException
import robot.interfaces.IRobotPart
import java.util.*

class RobotConstructionUtil {
    private var head: Boolean = false
    private var hand: Boolean = false
    private var leg: Boolean = false

    fun validateOfTheRobotPart(part: IRobotPart): IRobotPart {
        return if (!head && part.getPartName().toLowerCase(Locale.ROOT) == "head") {
            head = true
            part
        } else if (!hand && part.getPartName().toLowerCase(Locale.ROOT) == "hand") {
            hand = true
            part
        } else if (!leg && part.getPartName().toLowerCase(Locale.ROOT) == "leg") {
            leg = true
            part
        } else throw CreateRobotPartException("You're trying to create the robot with a several same parts")
    }
}
