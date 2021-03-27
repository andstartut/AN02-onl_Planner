package robot.util

import robot.enums.PartsAndPrices
import robot.exceptions.CreateRobotPartException
import java.util.*

class RobotPartUtil {

    companion object {
        private val HEAD: String = convertingConstantToName(PartsAndPrices.HEAD.name)
        private val HAND: String = convertingConstantToName(PartsAndPrices.HAND.name)
        private val LEG: String = convertingConstantToName(PartsAndPrices.LEG.name)

        fun partOperation(partName: String, brandName: String): String {
            return when (partName) {
                LEG -> "\nThe $partName's $brandName goes"
                HEAD -> "\nThe $partName's $brandName turns"
                HAND -> "\nThe $partName's $brandName waving"
                else -> throw CreateRobotPartException("No such part")
            }
        }

        fun convertingConstantToName(constantName: String): String {
            val lowerName = constantName.substring(1).toLowerCase(Locale.ROOT)
            return constantName.first().toUpperCase() + lowerName
        }
    }
}
