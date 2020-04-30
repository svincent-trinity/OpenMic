package models

case class UserData(username: String, password: String)
case class ProjectItem(id: Int, text: String, isPublic: String, midiData: String)
case class RecordingData(id: Int, name: String, description: String)