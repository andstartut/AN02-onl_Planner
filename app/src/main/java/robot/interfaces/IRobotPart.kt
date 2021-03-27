package robot.interfaces

interface IRobotPart {
    fun info(): String
    fun getPrice(): Int
    fun getPartName(): String
    fun getBrandName(): String
}