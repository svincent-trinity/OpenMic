package models

object CodeGen extends App {
  slick.codegen.SourceCodeGenerator.run(
    "slick.jdbc.PostgresProfile", 
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost/openmic?user=mlampton&password=password",
    "/Software/OpenMic/server/app",
    "models", None, None, true, false
  )
}