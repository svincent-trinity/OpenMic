package controllers

import javax.inject._

import edu.trinity.videoquizreact.shared.SharedMessages
import play.api.mvc._
import play.api.i18n._
import models.UserManager
import play.api.data._
import play.api.data.Forms._

case class LoginData(username: String, password: String, privacy: String)

case class ProjectData(user: String, name: String, privacy: String)//, midiNotes: String)


@Singleton
class Application @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {
  val loginForm = Form(mapping(
     "Username" -> text(3,10),
     "Password" -> text(8),
     "Privacy"  -> text(5)

    )(LoginData.apply)(LoginData.unapply))

  val createProjectForm = Form(mapping(
    "Username" -> text,
    "Name" -> nonEmptyText,
    "Privacy" -> text
    //"MidiNotes" -> text
  )(ProjectData.apply)(ProjectData.unapply))

  def login = Action { implicit request =>
    Ok(views.html.login(loginForm))
  }


    def logout = Action {
        Redirect(routes.Application.login()).withNewSession
    }

  def index = Action { implicit request =>
          val usernameOption = request.session.get("username")
        usernameOption.map { username =>

        Ok(views.html.index(username, "No current project selected"))
        }.getOrElse(Redirect(routes.Application.login()))
  }


    def validateLoginPost = Action { implicit request =>
      val postVals = request.body.asFormUrlEncoded
        postVals.map { args =>
            val username = args("username").head
            val password = args("password").head
            if(UserManager.validateUser(username, password)) {
                Redirect(routes.Application.home()).withSession("username" -> username)
            } else {
              Redirect(routes.Application.login()).flashing("error" -> "Invalid username/password combination, bud")
            }
        }.getOrElse(Redirect(routes.Application.login()))
    }

    def validateLoginForm = Action { implicit request =>
        loginForm.bindFromRequest.fold(

            formWithErrors => BadRequest(views.html.login(formWithErrors)),
            ld => 
                if(UserManager.validateUser(ld.username, ld.password)) {
                    Redirect(routes.Application.home()).withSession("username" -> ld.username)
                } else {
                    Redirect(routes.Application.login()).flashing("error" -> "Invalid username/password combination, bud")
                }
        )

    }


  def home = Action { implicit request =>

      val usernameOption = request.session.get("username")
        usernameOption.map { username =>
        val allUsers = UserManager.getUsers()
        val projects = UserManager.getProjects(username)
        Ok(views.html.home(username, allUsers, projects, createProjectForm))
        }.getOrElse(Redirect(routes.Application.login()))
  }

  def publicLobby = Action { implicit request =>
      val usernameOption = request.session.get("username")
        usernameOption.map { username =>
        val publicSessions = UserManager.getPublicProjectNames()
        Ok(views.html.publicLobby(username, publicSessions))
        }.getOrElse(Redirect(routes.Application.login()))
  }

  def recordings = Action { implicit request =>
      val usernameOption = request.session.get("username")
        usernameOption.map { username =>

        Ok(views.html.recordings(username))
        }.getOrElse(Redirect(routes.Application.login()))
  }

  def editProfile = Action { implicit request =>
      val usernameOption = request.session.get("username")
        usernameOption.map { username =>

        Ok(views.html.editProfile(username, UserManager.showPrivacy(username)))
        }.getOrElse(Redirect(routes.Application.login()))
  }

    def createUser = Action { implicit request =>

        //binding
        loginForm.bindFromRequest.fold(
            formWithErrors => BadRequest(views.html.login(formWithErrors)),
            ld => 
                if(UserManager.createUser(ld.username, ld.password, ld.privacy)) {
                    Redirect(routes.Application.home()).withSession("username" -> ld.username)
                } else {
                    Redirect(routes.Application.home()).flashing("error" -> "Invalid username/password combination, bud")
                }
        )

    }

    def createProject = Action { implicit request =>
                //binding
        createProjectForm.bindFromRequest.fold(

            formWithErrors => {
                val usernameOption = request.session.get("username")
                usernameOption.map { username =>
                val allUsers = UserManager.getUsers()
                val projects = UserManager.getProjects(username)
                BadRequest(views.html.home(username, allUsers, projects, formWithErrors))
                }.getOrElse(Redirect(routes.Application.login()))},
            ld => {
                val usernameOption = request.session.get("username")
                usernameOption.map { username =>
                if(UserManager.createProject(username, ld.name + "####" + ld.privacy)) {
                  Ok(views.html.index(username, ld.name))
                } else {
                    Redirect(routes.Application.home()).flashing("error" -> "Invalid username/password combination, bud")
                }
              }.getOrElse(Redirect(routes.Application.home()).flashing("error" -> "Something went wrong"))
              }
        )
    }

}
