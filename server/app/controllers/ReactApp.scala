package controllers

import javax.inject._

import play.api.mvc._
import play.api.i18n._
import models.UserProjectsDatabaseModel
import play.api.libs.json._
import models._
import play.api.data._
import play.api.data.Forms._
import scala.collection.mutable.ArrayBuffer


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
import java.io.PrintWriter;


//WebSocket Stuff
import play.api.libs.json._
import akka.actor.Actor
import play.api.libs.streams.ActorFlow
import akka.actor.ActorSystem
import akka.stream.Materializer
import actors.ChatActor
import akka.actor.Props
import actors.ChatManager





@Singleton
class ReactApp @Inject() (protected val dbConfigProvider: DatabaseConfigProvider, cc: MessagesControllerComponents)(implicit ec: ExecutionContext, system: ActorSystem, mat: Materializer)
  extends MessagesAbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {

    val manager = system.actorOf(Props[ChatManager], "Manager")

  def socket = WebSocket.accept[String, String] { request =>
    println("Getting socket")
    ActorFlow.actorRef { out =>
      ChatActor.props(out, manager)
    }
  }
    private val model = new UserProjectsDatabaseModel(db)

    val instrumentForm = Form(mapping(
     "id" -> number,
     "userid" -> text,
     "instrumentName" -> text(1),
     "description" -> text(0),

     "privacy"  -> text(5)

    )(InstrumentData.apply)(InstrumentData.unapply))



    def load = Action.async { implicit request =>
        println("React App Loading")

        Future.successful(Ok(views.html.reactView(instrumentForm)))
    }

    implicit val userDataReads = Json.reads[UserData]
    implicit val projectItemWrites = Json.writes[ProjectItem]
    implicit val recordingDataWrites = Json.writes[RecordingData]
    implicit val instrumentDataWrites = Json.writes[InstrumentData]

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

    def getInstrument = Action.async { implicit request =>
        withSessionUsername { username =>
        model.getInstruments().map(recs =>
            Ok(Json.toJson(recs)))
    }

    }

    def loadAudio(id: Int)(implicit request: Request[AnyContent]): Boolean = {
        val bytes = scala.concurrent.Await.result(model.getRecAudio(id), Duration(50000, "millis"))
        var filePath:String = Paths.get("server/public/audioSource/audio.mp3").toString()
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

    def loadInstrumentIntoSequencer = Action.async { implicit request =>
      println("loading notes")
      withJsonBody[String] { instrument =>
            println(instrument)
            val b = loadInstrumentAudio(instrument)
            if(b) Future.successful(Ok(Json.toJson(true)))
            else Future.successful(Ok(Json.toJson(false)))
      }

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


    def instrumentUpload = Action(parse.multipartFormData) { implicit request =>
      val instrums = Array("C4", "Db4", "D4", "Eb4", "E4", "F4", "Gb4", "G4", "Ab4", "A4", "Bb4", "B4", "C5", "Db5", "D5", "Eb5", "E5", "F5", "Gb5", "G5", "Ab5", "A5", "Bb5", "B5", "C6")
      var finBytes = ArrayBuffer[Array[Byte]]()
      instrums.foreach(elem =>
          request.body
            .file(elem)
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
              finBytes += fileBytes
              //model.uploadResource(filename.toString, fileBytes, 3).map(added => Ok(Json.toJson("File uploaded")))
              //picture.ref.copyTo(Paths.get(s"/tmp/picture/$filename"), replace = true)
              //Ok(Json.toJson("Recording uploaded"))

            }
            .getOrElse {
              finBytes += Files.readAllBytes(Paths.get("server/public/sounds/emptyAudio.mp3"))
              //Ok(Json.toJson("File not uploaded"))
            }
      )
      //println(finBytes(0))
      var instrumentName = ""
      var userid = ""
      var privacy = ""
      var description = ""

      val postVals = request.body.asFormUrlEncoded
      postVals.map { args =>
        println(args)
        println(args._2(0))
        println(args._1)
        //println(args._2(0)(0))
        if(args._1 == "instrumentName") instrumentName = args._2(0)
        if(args._1 == "userid") userid = args._2(0)
        if(args._1 == "Privacy") privacy = args._2(0)
        if(args._1 == "description") description = args._2(0)

        
        //Ok(Json.toJson(true))    
      }//.getOrElse(Redirect(routes.ReactApp.load5()))
      model.uploadInstrument(instrumentName, userid.toInt, privacy, description, finBytes(0), finBytes(1), finBytes(2), finBytes(3), finBytes(4), finBytes(5), finBytes(6), finBytes(7), finBytes(8), finBytes(9), finBytes(10), finBytes(11), finBytes(12), finBytes(13), finBytes(14), finBytes(15), finBytes(16), finBytes(17), finBytes(18), finBytes(19), finBytes(20), finBytes(21), finBytes(22), finBytes(23), finBytes(24))

      Ok(Json.toJson(true))
    }

    def loadInstrumentAudio(instName: String)(implicit request: Request[AnyContent]): Boolean = {
        val id = scala.concurrent.Await.result(model.getInstrumentIdByName(instName), Duration(50000, "millis"))
        //C4
        var bytes = scala.concurrent.Await.result(model.getInstrumentAudioC4(id(0)), Duration(50000, "millis"))
        var selectedAudio = "C4.mp3"
        var filePath:String = Paths.get("server/public/sounds/instrumentSources/" + selectedAudio).toString()
        var file = new File(filePath)
        var os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close()
        //C5
        bytes = scala.concurrent.Await.result(model.getInstrumentAudioC5(id(0)), Duration(50000, "millis"))
        selectedAudio = "C5.mp3"
        filePath = Paths.get("server/public/sounds/instrumentSources/" + selectedAudio).toString()
        file = new File(filePath)
        os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close()
        //C6
        bytes = scala.concurrent.Await.result(model.getInstrumentAudioC6(id(0)), Duration(50000, "millis"))
        selectedAudio = "C6.mp3"
        filePath = Paths.get("server/public/sounds/instrumentSources/" + selectedAudio).toString()
        file = new File(filePath)
        os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close()

        //Db4
        bytes = scala.concurrent.Await.result(model.getInstrumentAudioDb4(id(0)), Duration(50000, "millis"))
        selectedAudio = "Db4.mp3"
        filePath = Paths.get("server/public/sounds/instrumentSources/" + selectedAudio).toString()
        file = new File(filePath)
        os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close()
        //Db5
        bytes = scala.concurrent.Await.result(model.getInstrumentAudioDb5(id(0)), Duration(50000, "millis"))
        selectedAudio = "Db5.mp3"
        filePath = Paths.get("server/public/sounds/instrumentSources/" + selectedAudio).toString()
        file = new File(filePath)
        os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close()
        //D4
        bytes = scala.concurrent.Await.result(model.getInstrumentAudioD4(id(0)), Duration(50000, "millis"))
        selectedAudio = "D4.mp3"
        filePath = Paths.get("server/public/sounds/instrumentSources/" + selectedAudio).toString()
        file = new File(filePath)
        os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close()
        //D5
        bytes = scala.concurrent.Await.result(model.getInstrumentAudioD5(id(0)), Duration(50000, "millis"))
        selectedAudio = "D5.mp3"
        filePath = Paths.get("server/public/sounds/instrumentSources/" + selectedAudio).toString()
        file = new File(filePath)
        os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close()
        //Eb4
        bytes = scala.concurrent.Await.result(model.getInstrumentAudioEb4(id(0)), Duration(50000, "millis"))
        selectedAudio = "Eb4.mp3"
        filePath = Paths.get("server/public/sounds/instrumentSources/" + selectedAudio).toString()
        file = new File(filePath)
        os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close()
        //Eb5
        bytes = scala.concurrent.Await.result(model.getInstrumentAudioEb5(id(0)), Duration(50000, "millis"))
        selectedAudio = "Eb5.mp3"
        filePath = Paths.get("server/public/sounds/instrumentSources/" + selectedAudio).toString()
        file = new File(filePath)
        os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close()
        //E4
        bytes = scala.concurrent.Await.result(model.getInstrumentAudioE4(id(0)), Duration(50000, "millis"))
        selectedAudio = "E4.mp3"
        filePath = Paths.get("server/public/sounds/instrumentSources/" + selectedAudio).toString()
        file = new File(filePath)
        os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close()
        //E5
        bytes = scala.concurrent.Await.result(model.getInstrumentAudioE5(id(0)), Duration(50000, "millis"))
        selectedAudio = "E5.mp3"
        filePath = Paths.get("server/public/sounds/instrumentSources/" + selectedAudio).toString()
        file = new File(filePath)
        os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close()
        //f4
        bytes = scala.concurrent.Await.result(model.getInstrumentAudioF4(id(0)), Duration(50000, "millis"))
        selectedAudio = "F4.mp3"
        filePath = Paths.get("server/public/sounds/instrumentSources/" + selectedAudio).toString()
        file = new File(filePath)
        os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close()
        //f5
        bytes = scala.concurrent.Await.result(model.getInstrumentAudioF5(id(0)), Duration(50000, "millis"))
        selectedAudio = "F5.mp3"
        filePath = Paths.get("server/public/sounds/instrumentSources/" + selectedAudio).toString()
        file = new File(filePath)
        os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close()
        //Gb4
        bytes = scala.concurrent.Await.result(model.getInstrumentAudioGb4(id(0)), Duration(50000, "millis"))
        selectedAudio = "Gb4.mp3"
        filePath = Paths.get("server/public/sounds/instrumentSources/" + selectedAudio).toString()
        file = new File(filePath)
        os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close()
        //Gb5
        bytes = scala.concurrent.Await.result(model.getInstrumentAudioGb5(id(0)), Duration(50000, "millis"))
        selectedAudio = "Gb5.mp3"
        filePath = Paths.get("server/public/sounds/instrumentSources/" + selectedAudio).toString()
        file = new File(filePath)
        os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close()        
        //G4
        bytes = scala.concurrent.Await.result(model.getInstrumentAudioG4(id(0)), Duration(50000, "millis"))
        selectedAudio = "G4.mp3"
        filePath = Paths.get("server/public/sounds/instrumentSources/" + selectedAudio).toString()
        file = new File(filePath)
        os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close()
        //G5
        bytes = scala.concurrent.Await.result(model.getInstrumentAudioG5(id(0)), Duration(50000, "millis"))
        selectedAudio = "G5.mp3"
        filePath = Paths.get("server/public/sounds/instrumentSources/" + selectedAudio).toString()
        file = new File(filePath)
        os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close() 
        //Ab4
        bytes = scala.concurrent.Await.result(model.getInstrumentAudioAb4(id(0)), Duration(50000, "millis"))
        selectedAudio = "Ab4.mp3"
        filePath = Paths.get("server/public/sounds/instrumentSources/" + selectedAudio).toString()
        file = new File(filePath)
        os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close()
        //Ab5
        bytes = scala.concurrent.Await.result(model.getInstrumentAudioAb5(id(0)), Duration(50000, "millis"))
        selectedAudio = "Ab5.mp3"
        filePath = Paths.get("server/public/sounds/instrumentSources/" + selectedAudio).toString()
        file = new File(filePath)
        os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close()        
        //A4
        bytes = scala.concurrent.Await.result(model.getInstrumentAudioA4(id(0)), Duration(50000, "millis"))
        selectedAudio = "A4.mp3"
        filePath = Paths.get("server/public/sounds/instrumentSources/" + selectedAudio).toString()
        file = new File(filePath)
        os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close()
        //A5
        bytes = scala.concurrent.Await.result(model.getInstrumentAudioA5(id(0)), Duration(50000, "millis"))
        selectedAudio = "A5.mp3"
        filePath = Paths.get("server/public/sounds/instrumentSources/" + selectedAudio).toString()
        file = new File(filePath)
        os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close() 
        //Bb4
        bytes = scala.concurrent.Await.result(model.getInstrumentAudioBb4(id(0)), Duration(50000, "millis"))
        selectedAudio = "Bb4.mp3"
        filePath = Paths.get("server/public/sounds/instrumentSources/" + selectedAudio).toString()
        file = new File(filePath)
        os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close()
        //Bb5
        bytes = scala.concurrent.Await.result(model.getInstrumentAudioBb5(id(0)), Duration(50000, "millis"))
        selectedAudio = "Bb5.mp3"
        filePath = Paths.get("server/public/sounds/instrumentSources/" + selectedAudio).toString()
        file = new File(filePath)
        os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close()        
        //B4
        bytes = scala.concurrent.Await.result(model.getInstrumentAudioB4(id(0)), Duration(50000, "millis"))
        selectedAudio = "B4.mp3"
        filePath = Paths.get("server/public/sounds/instrumentSources/" + selectedAudio).toString()
        file = new File(filePath)
        os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close()
        //B5
        bytes = scala.concurrent.Await.result(model.getInstrumentAudioB5(id(0)), Duration(50000, "millis"))
        selectedAudio = "B5.mp3"
        filePath = Paths.get("server/public/sounds/instrumentSources/" + selectedAudio).toString()
        file = new File(filePath)
        os = new FileOutputStream(file)
        os.write(bytes(0))
        os.close() 

        return true
    }

    def logout = TODO /*Action { implicit request =>
        Ok(Json.toJson(true)).withNewSession
    }*/
}