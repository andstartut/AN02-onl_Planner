package robot

import robot.factory.Robot

class RobotPrice(private val robots: Array<Robot>) {
    fun findTheMostExpensive() {
        var maxPrice = 0
        var name = ""
        for (robot in robots) {
            if (robot.getPrice() > maxPrice) {
                maxPrice = robot.getPrice()
                name = robot.toString()
            }
        }
        return println("The most expensive robot is $name, and it costs: $maxPrice")
    }
}