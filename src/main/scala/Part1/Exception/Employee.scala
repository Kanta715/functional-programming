package Part1.Exception

case class Employee(name: String, department: String) {

  def findByName(name: String): Option[Employee] =
    if (this.name == name) Some(this.copy()) else None

  def joeDepartment: Option[String] = findByName("Joe").map(_.department)
}
