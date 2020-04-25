package models

case class UserData(username: String, password: String)
case class ProjectItem(id: Int, text: String, isPublic: String, midiData: String)