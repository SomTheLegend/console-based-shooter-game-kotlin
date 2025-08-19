data class Point(var x: Int = 0, var y: Int = 0){

    fun setLocation(x: Int, y: Int){
        this.x = x
        this.y = y
    }

    fun distance(otherPoint: Point): Double {
        val dx = this.x - otherPoint.x
        val dy = this.y - otherPoint.y
        return Math.sqrt((dx * dx + dy * dy).toDouble())
    }
}
