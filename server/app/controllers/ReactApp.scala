package controllers

import javax.inject._

import play.api.mvc._
import play.api.i18n._
import models.UserProjectsDatabaseModel
import play.api.libs.json._
import models._
import play.api.data._
import play.api.data.Forms._


import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.ExecutionContext
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Future
import scala.concurrent.duration.Duration


import java.nio.file.Paths
import java.io.File;
import java.nio.file.Files;
import java.io.FileOutputStream; 
import java.io.OutputStream; 
import java.io.PrintWriter




@Singleton
class ReactApp @Inject() (protected val dbConfigProvider: DatabaseConfigProvider, cc: ControllerComponents)(implicit ec: ExecutionContext)
  extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {

    private val model = new UserProjectsDatabaseModel(db)

    def load = Action.async { implicit request =>
        println("React App Loading")

        Future.successful(Ok(views.html.reactView()))
    }

    implicit val userDataReads = Json.reads[UserData]
    implicit val projectItemWrites = Json.writes[ProjectItem]
    implicit val recordingDataWrites = Json.writes[RecordingData]

    def withJsonBody[A](f: A => Future[Result])(implicit request: Request[AnyContent], reads: Reads[A]): Future[Result] = {
      request.body.asJson.map {body => 
            Json.fromJson[A](body) match {
                case JsSuccess(a, path) => f(a)
                case e @ JsError(_) => Future.successful(Redirect(routes.ReactApp.load()))
            }
         }.getOrElse(Future.successful(Redirect(routes.ReactApp.load())))

    }

    def withSessionUsername(f: String => Future[Result])(implicit request: Request[AnyContent]): Future[Result] = {
        request.session.get("username").map(f).getOrElse(Future.successful(Ok(Json.toJson(Seq.empty[String]))))
    }


    def withSessionUserid(f: Int => Future[Result])(implicit request: Request[AnyContent]): Future[Result] = {
        request.session.get("userid").map(userid => f(userid.toInt)).getOrElse(Future.successful(Ok(Json.toJson(Seq.empty[String]))))
    }


    def validate = Action.async { implicit request =>
        withJsonBody[UserData] { ud =>
                model.validateUser(ud.username, ud.password).map { ouserId =>
                    ouserId match {
                        case Some(userid) =>
                            Ok(Json.toJson(true))
                              .withSession("username" -> ud.username, "userid" -> userid.toString, "csrfToken" -> play.filters.csrf.CSRF.getToken.get.value)
                        case None =>
                            Ok(Json.toJson(false))
                }
            }
        }
    }

    def createUser = Action.async { implicit request =>
        withJsonBody[UserData] { ud => model.createUser(ud.username, ud.password).map { ouserId =>
            ouserId match {
            case Some(userid) =>
                Ok(Json.toJson(true))
                  .withSession("username" -> ud.username, "userid" -> userid.toString, "csrfToken" -> play.filters.csrf.CSRF.getToken.get.value)
            case None =>
                Ok(Json.toJson(false))
            }
        }   }
    }

    def getUsers = Action.async { implicit request => 
        withSessionUsername { username =>
            model.getUsers().map(tasks => Ok(Json.toJson(tasks)))
        }
    }

    /*def g  etUserIdBySongId = Action.async { implicit request =>
        withJsonBody[ProjectItem] { item => model.getUserIdBySongId(itemd.id).flatMap {userid =>
            userid match {
                case Some(itemd) =>
                    model.getUserIdBySongId(itemd.id).flatMap(userid => model.getUsernameById(userid(0)).map(user => Ok(Json.toJson(user))))
                case None =>
                    Ok(Json.toJson(false))
            }

        }
    }*/

    /*def getUsernameById = Action.async {
        withJsonBody[]
    }*/

    def recordingsList = Action.async { implicit request =>
        withSessionUsername { username =>
        model.getRecordings().map(recs =>
            Ok(Json.toJson(recs)))
    }

    }

    def loadAudio(id: Int)(implicit request: Request[AnyContent]): Boolean = {
        val bytes = scala.concurrent.Await.result(model.getRecAudio(id), Duration(50000, "millis"))
        var filePath:String = Paths.get("server/public/tmp/audio.mp3").toString()
        var file = new File(filePath)
        val os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close()
        return true
    }


    def projectsList = Action.async { implicit request => 
        withSessionUsername { username =>
            model.getProjects(username).map(tasks =>
                Ok(Json.toJson(tasks)))
        }
    }

    def publicProjectsList = Action.async { implicit request => 
        withSessionUsername { username =>
            model.getPublicProjects().map(tasks => Ok(Json.toJson(tasks)))
        }
    }

    def addProject = Action.async { implicit request =>
        withSessionUserid { userid =>
            withJsonBody[String] { task =>
                var parsed = task.split("####");

                model.addProject(userid, parsed(0), parsed(1)).map(count => Ok(Json.toJson(count > 0)))
            }
             
        }
    }
    

    def addPublicProject = Action.async { implicit request =>
        withSessionUserid { userid =>
            withJsonBody[String] { task =>
                var parsed = task.split("####");

                model.addProject(userid, parsed(0), "Public").map(count => Ok(Json.toJson(count > 0)))
            }
        }
    }
    def delete =  Action.async { implicit request =>
        withSessionUsername { username =>
            withJsonBody[String] { index =>
                var parsed = index.split("####")
                if(parsed(1) == "regular") {
                model.removeProject(parsed(0).toInt).map( removed => Ok(Json.toJson(removed)))
                } else {
                    model.removePublicProject(parsed(0).toInt).map( removed => Ok(Json.toJson(removed)))

                }
            }
        }
    }
    def playRecording = Action.async { implicit request =>
        println("Playing rec")
        withJsonBody[String] { songId =>
            println(songId)
            val b = loadAudio(songId.toInt)
            if(b) Future.successful(Ok(Json.toJson(true)))
            else Future.successful(Ok(Json.toJson(false)))
        }

        /*request.body.asJson.map {body => 
            Json.fromJson[String](body) match {
                case JsSuccess(a, path) => {
                    println(body)
                    loadAudio(body.toInt)
                    Future.successful(Ok(Json.toJson(true)))            
                }
                case e @ JsError(_) => Future.successful(Redirect(routes.ReactApp.load()))
            }
         }.getOrElse(Future.successful(Redirect(routes.ReactApp.load())))*/

    }


    def upload = Action(parse.multipartFormData) { implicit request =>

      request.body
        .file("picture")
        .map { picture =>
          // only get the last part of the filename
          // otherwise someone can send a path like ../../home/foo/bar.txt to write to other files on the system
          val filename    = Paths.get(picture.filename).getFileName
          val fileSize    = picture.fileSize
          println(fileSize.toString())
          //println(Paths.get(picture.ref.file.getAbsolutePath))
          val contentType = picture.contentType
          //val fileContent = Files.readAllBytes(filename);
          val fileBytes = Files.readAllBytes(Paths.get(picture.ref.file.getAbsolutePath))

          model.uploadResource(filename.toString, fileBytes, 3).map(added => Ok(Json.toJson("File uploaded")))
          //picture.ref.copyTo(Paths.get(s"/tmp/picture/$filename"), replace = true)
          Ok(Json.toJson("Recording uploaded"))

        }
        .getOrElse {

          Ok(Json.toJson("File not uploaded"))
        }
    
    }



    def logout = TODO /*Action { implicit request =>
        Ok(Json.toJson(true)).withNewSession
    }*/
}